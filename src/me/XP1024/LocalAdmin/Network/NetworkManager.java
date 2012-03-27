package me.XP1024.LocalAdmin.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import me.XP1024.LocalAdmin.WrappedPacket;

public abstract class NetworkManager {

	public abstract void send(WrappedPacket packet) throws IOException;

	protected List<Thread> threads;
	protected Map<String, NetworkConnection> connections;
	protected Observer observer;
	protected UnsecureConnectionFactory factory;
	
	public NetworkManager(Observer observer) {
		this.observer = observer;
		threads = new LinkedList<Thread>();
		connections = new HashMap<String, NetworkConnection>();
		factory = new UnsecureConnectionFactory();
	}

	public synchronized void disconnect(String user) {
		NetworkConnection connection = connections.get(user);
		connections.remove(user);
		
		if(connection != null){
			for(Thread thread : threads) {
				if(thread instanceof ConnectionThread){
					if(((ConnectionThread) thread).getConnection() == connection){
						thread.interrupt();
						threads.remove(thread);
						return;
					}
				}
			}
		}
	}

	protected void startAndAdd(Thread thread) {
		thread.start();
		threads.add(thread);	
	}

	/**
	 * Adds a connection after successful authentification
	 */
	synchronized void addConnection(String user, NetworkConnection connection) {
		if(connections.containsKey(user))
			throw new IllegalArgumentException("User already logged on");
		
		connections.put(user, connection);	
	}

	@SuppressWarnings("deprecation")
	public void shutdown() {
		for(Thread thread : threads){
			thread.interrupt();			
		}
	
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {}
		
		//if a thread is blocking just kill it
		for(Thread thread : threads){
			if(thread.isAlive())
				thread.stop();			
		}
	
	}

	protected void notify(WrappedPacket packet) {
		observer.update(null, packet);
	}
	
	public synchronized Set<String> getOnlineUsers() {
		return connections.keySet();
	}

}