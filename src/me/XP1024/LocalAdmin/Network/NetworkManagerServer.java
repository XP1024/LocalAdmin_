package me.XP1024.LocalAdmin.Network;

import java.io.IOException;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Network.Authentication.Authenticator;



public class NetworkManagerServer extends NetworkManager {
	public NetworkManagerServer(Observer observer) {
		super(observer);
	}
	
	public void startServer(int port, Authenticator auth) throws IOException {
		factory.startServer(port);
		Thread serverThread = new ServerThread(factory, this, auth);
		startAndAdd(serverThread);
	}
		
	@Override
	public void send(WrappedPacket packet) throws IOException{
		NetworkConnection connection = connections.get(packet.getUser());
		if(connection == null)
			throw new IllegalArgumentException("There is no user whith this name logged in");
		try{
			connection.send(packet);
		}
		catch(IOException e){
			connection.close();
			connections.remove(packet.getUser());
			throw e;
		}
	}
}
