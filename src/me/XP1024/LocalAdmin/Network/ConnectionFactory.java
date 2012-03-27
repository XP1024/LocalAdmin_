package me.XP1024.LocalAdmin.Network;

import java.io.IOException;

public interface ConnectionFactory {

	public abstract void startServer(int port) throws IOException;

	/**
	 * Connects to a server
	 * @see java.net.Socket#Socket(String host, int port)
	 * @param address The address of the server
	 * @param port The specific port
	 * @return A connection to the server
	 * @throws IOException If creating the connection failed
	 */
	public abstract NetworkConnection connect(String address, int port)
			throws IOException;

	/**
	 * @see java.net.ServerSocket#accept() 
	 * @return A connection to a client
	 * @throws IOException If creating the connection failed
	 */
	public abstract NetworkConnection waitForNewConnection() throws IOException;

	public abstract void close();

}