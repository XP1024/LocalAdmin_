package me.XP1024.LocalAdmin.Module;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import junit.framework.Assert;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Network.NetworkManager;
import me.XP1024.LocalAdmin.Network.NetworkManagerClient;
import me.XP1024.LocalAdmin.Network.NetworkManagerServer;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Test;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.validation.AssertFalse;


public class TesModuleSystem {

	@Test(expected=IllegalStateException.class)
	public void testNoNetwork() throws IOException {
		ModuleSystem moduleSystem = new ModuleSystem();
		moduleSystem.send(new WrappedPacket(0, null, null));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testNetworkNotReady() throws IOException{
		ModuleSystem moduleSystem = new ModuleSystem();
		NetworkManager network = new NetworkManagerClient(moduleSystem);
		moduleSystem.setNetworkManager(network);
		moduleSystem.send(new WrappedPacket(0, null, null));
	}
	
	boolean packetReceived;
	
	@Test
	public void testDeliver() {
		
		packetReceived = false;
		
		ModuleSystem moduleSystem = new ModuleSystem();
		moduleSystem.register(new ServerModule() {
			
			@Override
			public void update(Serializable data, String user) {
				if(user.equals("Dummy"))
					packetReceived = true;
			}
		}, "SomePlugin", "1.0" );
		
		WrappedPacket packet = new WrappedPacket(1, "Dummy", null);
		
		moduleSystem.update(null, packet);
		moduleSystem.deliver();
		
		Assert.assertFalse(packetReceived);
		
		
		packet = new WrappedPacket(0, "Dummy", null);
		
		moduleSystem.update(null, packet);
		moduleSystem.deliver();
				
		Assert.assertTrue(packetReceived);
	}
	
	@Test
	public void testRegisterWithID(){
		packetReceived = false;
		
		ModuleSystem moduleSystem = new ModuleSystem();
		moduleSystem.register(new ServerModule() {
			
			@Override
			public void update(Serializable data, String user) {
				if(user.equals("Dummy"))
					packetReceived = true;
			}
		}, 42, "SomePlugin", "1.0" );
		
		
		moduleSystem.update(null,  new WrappedPacket(1, "Dummy", null));
		moduleSystem.deliver();
		
		assertFalse(packetReceived);
		
		moduleSystem.update(null, new WrappedPacket(42, "Dummy", null));
		moduleSystem.deliver();
		
		assertTrue(packetReceived);
		
		boolean exceptionOccured = false;
		try{
			moduleSystem.register(new ServerModule() {				
				@Override
				public void update(Serializable data, String user) {}
			}, -1, "OtherPlugin", "4.2");
		}catch (IllegalArgumentException e){
			exceptionOccured = true;
		}
		assertTrue(exceptionOccured);
		
	}

}
