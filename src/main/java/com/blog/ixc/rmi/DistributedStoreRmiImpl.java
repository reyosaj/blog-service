/**
 * 
 */
package com.blog.ixc.rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import com.blog.ixc.DistributedStore;

/**
 * @author reyos
 * @param <V>
 * @param <K>
 *
 */
public class DistributedStoreRmiImpl<K, V> extends ReceiverAdapter implements
		DistributedStore<K, V> {

	private final JChannel channel;
	private final ConcurrentHashMap<K, Address> addressCache = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<K, List<V>> store = new ConcurrentHashMap<>();

	public DistributedStoreRmiImpl() throws Exception {
		System.setProperty("jgroups.bind_addr", "127.0.0.1");
		System.setProperty("java.net.preferIPv4Stack", "true");
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

		if (data.get(key) instanceof Address) {
			// cache data location
			addressCache.putIfAbsent(key, (Address) data.get(key));
		} else {
			synchronized (store) {
				if (store.get(key) == null) {
					store.put(key, new ArrayList<V>());
				}
			}
			// Someone updated my data. store it.
			store.get(key).add(data.get(key));
		}

	}

	@Override
	public synchronized void add(K key, V value) throws Exception {

		// check if it is a new entry?.
		if (addressCache.get(key) == null) {
			System.out
					.println("DistributedStoreZooImpl.add() Taking ownership of blog - "
							+ key);

			// broadcast ownership status
			Map<K, Address> accountLocation = new HashMap<>();
			accountLocation.put(key, channel.getAddress());
			Message msg = new Message(null, null, accountLocation);
			channel.send(msg);

			addressCache.put(key, channel.getAddress());

			if (store.get(key) == null) {
				store.put(key, new ArrayList<>());
			}

			// add to store
			store.get(key).add(value);
			return;
		}
		// am I the owner? then, lets store the data.
		if (addressCache.get(key).equals(channel.getAddressAsString())) {
			if (store.get(key) == null) {
				store.put(key, new ArrayList<>());
			}

			store.get(key).add(value);
		} else {
			// someone else has ownership. Broadcast the data.
			Map<K, V> dest = new HashMap<>();
			dest.put(key, value);
			Message msg = new Message(addressCache.get(key), null, dest);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<V> getValues(K k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
