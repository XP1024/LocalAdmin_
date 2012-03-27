package me.XP1024.LocalAdmin.Network;

import java.io.IOException;

import me.XP1024.LocalAdmin.Network.Authentication.Authenticator;

public class ServerThread extends Thread {
	final ConnectionFactory factory;
	final NetworkManager manager;
	final Authenticator auth;
	
	public ServerThread(ConnectionFactory factory, NetworkManager manager, Authenticator auth){
		this.factory = factory;
		this.manager = manager;
		this.auth = auth; 
	}
	
	@Override
	public void run() {
		
		while(true){				
			try{
				if(isInterrupted())
					return;				
				
				NetworkConnection connection = factory.waitForNewConnection();
				Thread thread = new ConnectionThread(connection, manager, auth);
				manager.startAndAdd(thread);
				
			} catch (IOException e) {
				// TODO post error
				e.printStackTrace();
			}
		}
	}
}
