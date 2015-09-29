/**
 * 
 */
package com.blog.ixc;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import com.blog.ixc.zookeeper.DistributedStoreZooImpl;
import com.blog.mongodb.model.Comment;

/**
 * Distributed Store Test client. This class acts both as an RMI server and a
 * client. Each client will generate random document id's within a range and
 * push data to the datastore. The distributed data store will make sure that
 * data is send to the actual owner of that document.</br>
 * 
 * Run atleast two instances of this client to see the cluster nodes in action.
 *
 */
public class ClusterNode {

	private static String uuid;

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		uuid = UUID.randomUUID().toString();
		System.out.println(System.getProperty("java.rmi.server.codebase"));
		System.out.println("Starting cluster node: " + uuid);
		DistributedStoreZooImpl<String, Comment> impl = new DistributedStoreZooImpl<String, Comment>();
		DistributedStore<String, Comment> storeStub = (DistributedStore<String, Comment>) UnicastRemoteObject
				.exportObject(impl, 0);
		Registry registry = LocateRegistry.getRegistry();
		registry.bind(uuid, storeStub);

		Thread.sleep(1000);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Registry registry = LocateRegistry.getRegistry();
					DistributedStore<String, Comment> store = (DistributedStore<String, Comment>) registry
							.lookup(uuid);

					Random rnd = new Random();
					for (int i = 0; i < 10; i++) {
						Integer id = rnd.nextInt(10);
						Thread.sleep(5000);
						String blogId = "" + id;
						System.out.println("Commenting on blog: " + blogId);
						Comment c = new Comment();
						c.setBlogId(blogId);
						c.setUserId(uuid);
						store.add(blogId, c);
					}

					System.out.println("ClusterNode " + uuid + " own's : "
							+ Arrays.toString(store.getOwnedKeys().toArray()));

					Thread.sleep(5000);

					for (int id = 0; id < 10; id++) {
						String blogId = "" + id;
						System.out.println("blogId=" + blogId);
						System.out.println("comments="
								+ Arrays.toString(store.getValues(blogId)
										.toArray()));

					}

				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}
