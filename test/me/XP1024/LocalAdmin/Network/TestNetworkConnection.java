package me.XP1024.LocalAdmin.Network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import me.XP1024.LocalAdmin.WrappedPacket;

import org.junit.Test;

public class TestNetworkConnection {

	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPort() throws IOException {
		
		UnsecureConnectionFactory factory = new UnsecureConnectionFactory();
		factory.startServer(-1);
	}
	
	boolean success;
	@Test
	public void testSendRead() throws IOException, InterruptedException {
		final UnsecureConnectionFactory factory = new UnsecureConnectionFactory();
		success = true;
		
		factory.startServer(45000);
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				NetworkConnection connection = null;
				try {
					connection = factory.waitForNewConnection();
					WrappedPacket packet = connection.read();
					assertTrue(packet.getData().equals("Cookies"));
					assertTrue(packet.getModuleID() == 42);
					
					packet = connection.read();
					assertFalse(packet.getData().equals("Cookies"));
					
				} catch (Throwable t) {
					success = false;
				} finally {
					if(connection != null)
						connection.close();					
				}
			}
		});
		
		thread.start();
		Thread.sleep(10);
		NetworkConnection connection =  factory.connect("localhost", 45000);
		connection.send(new WrappedPacket(42, "blabla", "Cookies"));
		connection.send(new WrappedPacket(42, "blabla", "NoCookies"));
		thread.join();
		connection.close();
		factory.close();
		assertTrue(success);
		
	}	
}

