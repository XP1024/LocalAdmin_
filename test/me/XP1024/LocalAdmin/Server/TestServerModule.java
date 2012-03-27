package me.XP1024.LocalAdmin.Server;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;

import me.XP1024.LocalAdmin.Module.ServerModule;

import org.junit.Test;

public class TestServerModule {

	@Test(expected=IllegalStateException.class)
	public void testNotRegisteredModule() throws IOException {
		ServerModule module = new ServerModule() {
			
			@Override
			public void update(Serializable data, String user) {}
		};
		module.send(null, null);
	}
	
	boolean packetReceived;
	
	@Test
	public void testUpdate(){
		packetReceived = false;
		ServerModule module = new ServerModule() {
			
			@Override
			public void update(Serializable data, String user) {
				if(data.equals("Test") && user.equals("Dummy"))
					packetReceived = true;
				
			}
		};
		
		Serializable data = ((Serializable)("Test"));
		module.update(data, "Dummy");
		
		assertTrue(packetReceived);
	}
}
