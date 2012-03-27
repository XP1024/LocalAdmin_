package me.XP1024.LocalAdmin.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class UnsecureConnectionFactory implements ConnectionFactory {
	
	ServerSocket serverSocket;
	
	public UnsecureConnectionFactory()
	{
		serverSocket = null;
	}
	
	/* (non-Javadoc)
	 * @see me.XP1024.LocalAdmin.Network.ConnectionFactory#start(int)
	 */
	@Override
	public void startServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);		
	}
	
	/* (non-Javadoc)
	 * @see me.XP1024.LocalAdmin.Network.ConnectionFactory#connect(java.lang.String, int)
	 */
	@Override
	public NetworkConnection connect(String address, int port)throws IOException{
		Socket client = new Socket(address, port);
		try{
			return new NetworkConnection(client);
		} catch(IOException e){
			client.close();
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see me.XP1024.LocalAdmin.Network.ConnectionFactory#waitForNewConnection()
	 */
	@Override
	public NetworkConnection waitForNewConnection() throws IOException{
		
		Socket client = null;
		client = serverSocket.accept();
		try{
			return new NetworkConnection(client);
		} catch(IOException e){
			client.close();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see me.XP1024.LocalAdmin.Network.ConnectionFactory#close()
	 */
	@Override
	public void close() {
		if(serverSocket != null){
			try {
				serverSocket.close();
			} catch (IOException e) {}
		}
	}


}
