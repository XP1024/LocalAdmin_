package me.XP1024.LocalAdmin.Server;
import static org.junit.Assert.*;

import java.io.Serializable;

import junit.framework.Assert;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Module.ModuleSystem;
import me.XP1024.LocalAdmin.Module.ServerModule;

import org.junit.Test;


public class TestServerSystem {
	
	boolean packetReceived = false;
	
	
	
	@Test
	public void testModuleExceptionStability(){
		packetReceived = false;
		
		ModuleSystem server = new ModuleSystem();
		server.register(new ServerModule() {
			
			@Override
			public void update(Serializable data, String user) {
				throw new RuntimeException();			
			}
		}, "SomePlugin", "1.0" );
		
		server.register(new ServerModule() {
			
			@Override
			public void update(Serializable data, String user) {
				packetReceived = true;
			}
		}, "SomeOtherPlugin", "1.0" );
		
		WrappedPacket packet = new WrappedPacket(0, "Dummy", null);
		server.update(null, packet);
		
		packet = new WrappedPacket(1, "Dummy", null);
		server.update(null, packet);
		
		server.deliver();
		
		assertTrue(packetReceived);		
	}
	
	@Test
	public void testDoubleRegisteredModule(){
		
		ModuleSystem server = new ModuleSystem();
		
		ServerModule module = new ServerModule() {	
			@Override
			public void update(Serializable data, String user) {}
		};
		
		ServerModule module2 = new ServerModule() {
			@Override
			public void update(Serializable data, String user) {}
		};
		
		ServerModule module3 = new ServerModule() {
			@Override
			public void update(Serializable data, String user) {}
		};
		
		boolean exceptioOccured = false;
		
		server.register(module, "SomePlugin", "1.0" );
		try{
			server.register(module, "OtherPlugin", "1.0" );
		}catch(IllegalArgumentException e){
			exceptioOccured = true;
		}
		assertTrue(exceptioOccured);
		
		exceptioOccured = false;
		try{
			server.register(module2, "SomePlugin", "1.0" );
		}catch(IllegalArgumentException e){
			exceptioOccured = true;
		}
		assertTrue(exceptioOccured);
		
		exceptioOccured = false;
		try{
			server.register(module3, "SomeOtherPlugin", "1.0" );
		}catch(IllegalArgumentException e){
			exceptioOccured = true;
		}
		assertFalse(exceptioOccured);
	}
	
	
}
