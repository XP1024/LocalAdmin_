package me.XP1024.LocalAdmin.Network.Authentication;

import me.XP1024.LocalAdmin.Network.NetworkConnection;


public interface Authenticator {
	/**
	 * 
	 * @param input The connection to authenticate
	 * @return true if the authentication was successful
	 */
	public boolean authenticate(NetworkConnection connection);
	
	/**
	 * @return The name of the user when successfully authenticated
	 */
	public String getName();
}
