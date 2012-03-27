package me.XP1024.LocalAdmin.Network;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Network.Authentication.Authenticator;



public class ConnectionThread extends Thread {
	
	private NetworkConnection connection;
	private NetworkManager manager;
	private String userName;
	private Authenticator auth;
	
	private int errorCount;
	
	public ConnectionThread(NetworkConnection connection, NetworkManager manager, Authenticator auth){
		super();
		this.connection = connection;
		this.manager = manager;
		errorCount = 0;
		this.auth = auth;		
	}
	

	public NetworkConnection getConnection(){
		return connection;
	}
	
	@Override
	public void run(){
		
		if(!auth.authenticate(connection))
			manager.disconnect(userName);
		else {
			userName = auth.getName(); 
			manager.addConnection(userName, connection);
		}
		
		int stage = 2;
			
		while(true){
		
			if(isInterrupted()){
				connection.close();
				return;
			}
			
			
			try {
				
				//TODO state machine
				WrappedPacket packet = connection.read();
				packet.setUser(userName);
				
				switch(stage){
				case 0:
					//if(auth.autenticate(packet))
					//	stage++;
					break;
				case 1:
					//if(sync.synchronize(packet))
					//	stage++;
					break;
				case 2:
					manager.notify(packet);
					break;
				}
				
				
			} catch (Exception e) {
				// TODO post error and disconnect
				System.err.println(e.getCause());
				errorCount++;
				//When a client is continuously sending wrong packets, then this might be an attack or a version conflict
				if(errorCount >= 5)
					manager.disconnect(userName);
			}
		}		
	}
}
