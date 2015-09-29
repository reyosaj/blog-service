/**
 * 
 */
package com.blog.ixc;

import java.rmi.Remote;
import java.util.List;
import java.util.Set;

/**
 * An interface for distributed Key/Value in-memory store.
 * 
 * @author reyos
 *
 */
public interface DistributedStore<K, V> extends Remote {

	/**
	 * add key/value to the store.
	 * 
	 * @param k
	 * @param v
	 * @throws Exception
	 */
	public void add(final K k, V v) throws Exception;

	/**
	 * Get list of keys stored in all instances as Set.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<K> getAllKeys() throws Exception;

	/**
	 * Get the list of keys owned by an instance as Set
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<K> getOwnedKeys() throws Exception;

	/**
	 * Get Value store for a key
	 * 
	 * @param k
	 * @return
	 * @throws Exception
	 */
	public List<V> getValues(K k) throws Exception;

}
