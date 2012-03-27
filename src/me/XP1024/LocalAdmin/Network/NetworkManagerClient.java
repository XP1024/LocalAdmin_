package me.XP1024.LocalAdmin.Network;

import java.io.IOException;
import java.util.Observer;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Network.Authentication.Authenticator;

public class NetworkManagerClient extends NetworkManager {

	private NetworkConnection serverConnection;
	
	public NetworkManagerClient(Observer observer) {
		super(observer);
	}

	public void connect(String address, int port, Authenticator auth) throws IOException {
		serverConnection = factory.connect(address, port);
		Thread connection = new ConnectionThread(serverConnection, this, auth);
		startAndAdd(connection);
	}
	
	@Override
	public void send(WrappedPacket packet) throws IOException {
		if(serverConnection == null)
			throw new IllegalStateException("No connection to a server");
		
		serverConnection.send(packet);
	}
	
	void setServerConnectio(NetworkConnection serverConnection){
		this.serverConnection = serverConnection;
	}

}
