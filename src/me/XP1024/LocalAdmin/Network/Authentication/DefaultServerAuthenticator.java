package me.XP1024.LocalAdmin.Network.Authentication;

import me.XP1024.LocalAdmin.Network.NetworkConnection;

public class DefaultServerAuthenticator implements Authenticator {

	private NetworkConnection connection;
	String userName;
	
	@Override
	public boolean authenticate(NetworkConnection connection) {
		this.connection = connection;
		return false;
	}

	@Override
	public String getName() {
		return userName;
	}

}
