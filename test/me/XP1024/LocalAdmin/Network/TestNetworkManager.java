package me.XP1024.LocalAdmin.Network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Network.Authentication.Authenticator;

import org.junit.Test;

public class TestNetworkManager {
	
	boolean packetReceivedClient, packetReceivedServer;

	@Test
	public void testTransmitPacket() throws IOException, InterruptedException {
		packetReceivedClient = packetReceivedServer = false;
		
		Authenticator auth = new Authenticator() {
			
			@Override
			public String getName() {
				return "BobTest1";
			}
			
			@Override
			public boolean authenticate(NetworkConnection connection) {
				return true;
			}
		};
		
		
		NetworkManagerServer server = new NetworkManagerServer(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				packetReceivedServer = ((WrappedPacket)arg).getData().equals("toServer");				
			}
		});
		
		server.startServer(45000, auth);
		
		NetworkManagerClient client = new NetworkManagerClient(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				packetReceivedClient = ((WrappedPacket)arg).getData().equals("toClient");		
				
			}
		});
	
		
		
		client.connect("localhost", 45000, auth);
		Thread.sleep(5);
		
		WrappedPacket packet = new WrappedPacket(42, "B", "toServer");
		client.send(packet);
		
		
		packet = new WrappedPacket(42, "BobTest1", "toClient");
		server.send(packet);
		
		Thread.sleep(5);
		server.shutdown();
		client.shutdown();
		
		assertTrue(packetReceivedClient);
		assertTrue(packetReceivedServer);	
	}
	
	
	boolean failTest;
	@Test()
	public void testReceiveCrapPackets() throws UnknownHostException, IOException  {
		failTest = false;
		Authenticator auth = new Authenticator() {
			
			@Override
			public String getName() {
				return "BobTest2";
			}
			
			@Override
			public boolean authenticate(NetworkConnection connection) {
				return true;
			}
		};
		
		
		NetworkManagerServer server = new NetworkManagerServer(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				failTest = true;			
			}
		});
		
		server.startServer(45001, auth);
		
		Socket socket = new Socket("localhost", 45001);
		
		Random random = new Random();
		byte[] data = new byte[2048];
		random.nextBytes(data);
		try{
			socket.getOutputStream().write(data);
		} catch(Exception e){
		} finally {
			server.shutdown();
			socket.close();
		}
		
		assertFalse(failTest);
		
	}
	
	int packetsReceived;
	Lock increaseLock;
	@Test(timeout=20000)
	public void testHeavyLoad() throws InterruptedException, IOException{
		increaseLock = new ReentrantLock();
		
		final Authenticator authClient = new Authenticator() {
			
			@Override
			public String getName() {
				return "Bob";
			}
			
			@Override
			public boolean authenticate(NetworkConnection connection) {
				return true;
			}
		};
		
		packetsReceived = 0;
		
		Observer serverObs = new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				increaseLock.lock();
				packetsReceived++;
				increaseLock.unlock();
				
			}
		};
		
		
		NetworkManagerServer server = new NetworkManagerServer(serverObs);
		
		server.startServer(45002, new TestServerAuthenticator());
		
		final int threadCount = 30, packets = 10000;
		
		Thread[] threads = new Thread[threadCount];
		
		for(int i = 0; i < threadCount; i++){
			
			threads[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					NetworkManagerClient client = new NetworkManagerClient(null);
					try {
						client.connect("localhost", 45002, authClient);
						for(int j = 0; j < packets; j++)
							client.send(new WrappedPacket(0, "", "Wanna have some delicious packets?"));
					
					} catch (Exception e) { client.shutdown();}
				}
			});
			
			threads[i].start();
			
		}
	
		
		
		for(Thread t : threads){
			t.join();
		}
		//grant some time for the main thread to manage all packets
		Thread.sleep(10);
		
		assertEquals(threadCount * packets, packetsReceived);		
		
	}
	
}


class TestServerAuthenticator implements Authenticator{
	
	static int index = 0;
	
	@Override
	public String getName() {
		return "BobTest3" + index++;
	}
	
	@Override
	public boolean authenticate(NetworkConnection connection) {
		return true;
	}
};
