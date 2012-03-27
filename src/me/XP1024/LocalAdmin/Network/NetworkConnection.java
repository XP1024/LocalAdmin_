package me.XP1024.LocalAdmin.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.XP1024.LocalAdmin.WrappedPacket;




public class NetworkConnection {
	
	protected Socket socket;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	
	protected Lock recvLock;
	protected Lock sendLock;
	
	public NetworkConnection(Socket socket) throws IOException{
		this.socket = socket;

		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());
		
		sendLock = new ReentrantLock();
		recvLock = new ReentrantLock();
	}
	
	public void send(WrappedPacket packet) throws IOException{
		sendLock.lock();
		out.writeObject(packet);
		sendLock.unlock();
	}
	
	public WrappedPacket read() throws IOException, ClassNotFoundException{
		recvLock.lock();
		WrappedPacket packet = (WrappedPacket) in.readObject();
		recvLock.unlock();
		return packet;
	}
	
	public void close(){
		try {			
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {}
	}
	
	@Override 
	protected void finalize() throws Throwable {
		try {
			close();	
		} finally {
			super.finalize();
		}
	}
}
