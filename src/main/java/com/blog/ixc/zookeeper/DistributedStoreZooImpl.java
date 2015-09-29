/**
 * 
 */
package com.blog.ixc.zookeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.UUID;

import com.blog.Constants;
import com.blog.ixc.DistributedStore;

/**
 * A Distributed store that replicates data across all instances. The data store
 * is implemented using Zookeeper and JGroups. Zookeeper is used to store the
 * ownership metadata of objects stored in each instances. JGroups is used as a
 * mechanism to transmit data across multiple instances of the data store.
 * 
 * <PRE>
 *  When a client request to store some data, following possibilities are handled:
 * #.The instance which receives the request would  * try to take ownership
 * of the data. If successful, data is cached locally and broadcast messages
 * to eventually replicate the data to all instances.
 * 
 * #.Request to update data owned by a different instance. Data store would
 * identify the actual owner and broadcast messages to eventually replicate the
 * data to all instances.
 * 
 * <PRE>
 * @author reyos
 * @param <V>
 * @param <K>
 *
 */
public class DistributedStoreZooImpl<K, V> extends ReceiverAdapter implements
		DistributedStore<K, V> {

	private final JChannel channel;
	private final ConcurrentHashMap<K, List<V>> store = new ConcurrentHashMap<>();
	private final List<K> ownership = new ArrayList<K>();
	private ZooKeeper zk;

	public DistributedStoreZooImpl() throws Exception {
		System.setProperty("jgroups.bind_addr", "127.0.0.1");
		System.setProperty("java.net.preferIPv4Stack", "true");

		zk = new ZooKeeper(Constants.ZK_CLIENT_CONN,
				Constants.ZK_CLIENT_TIMEOUT, watchedEvent -> {
					System.out.println("Watched event path: "
							+ watchedEvent.getPath());
					System.out.println("Watched event: " + watchedEvent);
				});

		channel = new JChannel(); // use the default config, udp.xml
		channel.connect("ServiceNodesCluster");
		channel.setReceiver(this);
	}

	public void send(String message) throws Exception {
		Message m = new Message(null, null, message);
		channel.send(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgroups.ReceiverAdapter#receive(org.jgroups.Message)
	 */
	@Override
	public void receive(Message msg) {

		if (channel.getAddress().equals(msg.getSrc())) {
			// I broadcasted this message. Do nothing.
			return;
		}

		System.out.println(channel.getAddressAsString() + " Received message: "
				+ msg.getObject() + " from host: " + msg.getSrc());

		if (!(msg.getObject() instanceof Map)) {
			System.out
					.println("DistributedStoreZooImpl.receive() Cannot Accept : "
							+ msg.getObject().getClass());
			return;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<K, V> data = (Map) msg.getObject();

		K key = data.keySet().iterator().next();

		synchronized (store) {
			if (store.get(key) == null) {
				store.put(key, new ArrayList<V>());
			}
		}
		// Someone updated the data. store it.
		store.get(key).add(data.get(key));

		try {
			byte[] addressBytes = zk.getData("/" + key, false, null);
			Address addressOfAccount = UUID
					.fromString(new String(addressBytes));
			// am i the owner? then, replicate data.
			if (addressOfAccount.equals(channel.getAddress())) {
				// Broadcast to other replicas.
				Map<K, V> dest = new HashMap<>();
				dest.put(key, data.get(key));
				Message to = new Message(null, channel.getAddress(), dest);
				channel.send(to);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void add(K key, V value) throws Exception {

		Address addressOfAccount = channel.getAddress();

		// check if it is a new data?.
		if (zk.exists("/" + key, false) == null) {
			try {
				// claim ownership
				zk.create("/" + key, ((UUID) channel.getAddress())
						.toStringLong().getBytes(),
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} catch (KeeperException e) {
				System.out.println("Error: " + e.getMessage());
				// Some other node got hold of this first. So just use that node
				// to send
				byte[] addressBytes = zk.getData("/" + key, false, null);
				addressOfAccount = UUID.fromString(new String(addressBytes));
			}
		} else {
			byte[] addressBytes = zk.getData("/" + key, false, null);
			addressOfAccount = UUID.fromString(new String(addressBytes));
		}

		// am I the owner? then, lets store the data.
		if (addressOfAccount.equals(channel.getAddress())) {
			// Now store it locally
			if (store.get(key) == null) {
				store.put(key, new ArrayList<>());
			}

			// add data to store
			store.get(key).add(value);
			// update ownership list
			ownership.add(key);

			// Broadcast to other replicas.
			Map<K, V> dest = new HashMap<>();
			dest.put(key, value);
			Message msg = new Message(null, channel.getAddress(), dest);
			channel.send(msg);
		} else {
			// someone else has ownership. send to the owner.
			System.out.println("Account stored on: " + addressOfAccount);

			// someone else has ownership. Broadcast the data.
			Map<K, V> dest = new HashMap<>();
			dest.put(key, value);
			Message msg = new Message(addressOfAccount, channel.getAddress(),
					dest);
			channel.send(msg);
		}

	}

	@Override
	public Set<K> getAllKeys() throws Exception {
		Set<K> retVal = new HashSet<>();
		retVal.addAll(store.keySet());
		return retVal;
	}

	@Override
	public Set<K> getOwnedKeys() throws Exception {
		Set<K> retVal = new HashSet<>();
		retVal.addAll(ownership);
		return retVal;
	}

	@Override
	public List<V> getValues(K key) throws Exception {

		List<V> values;

		if (store.get(key) == null) {
			values = new ArrayList<>();
		} else {
			values = store.get(key);
		}

		return values;

	}

}
