package me.XP1024.LocalAdmin;

import java.util.logging.Level;

import me.XP1024.LocalAdmin.Module.ModuleSystem;
import me.XP1024.LocalAdmin.Module.ServerModule;
import me.XP1024.LocalAdmin.Module.baseModule.BukkitHandler;
import me.XP1024.LocalAdmin.Module.baseModule.ServerBaseModule;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.LogLevel;



public class BukkitStarter extends JavaPlugin {
	
	ModuleSystem serverSystem;
	
	@Override
	public void onEnable() {
		serverSystem = new ModuleSystem();
		//Task for packet delivering
		if(getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
				@Override
				public void run() {
					serverSystem.deliver();				
				}
			}, 10, 1) == -1)
			System.err.println("Couldn't register task for delivering packets");
		//Start baseplugin
		
		ServerBaseModule module = new ServerBaseModule();
		BukkitHandler handler = new BukkitHandler(module, this);
		serverSystem.register(module, module.getName(), module.getVersion());
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("LocalAdmin version " + getDescription().getVersion() + " running");
		return true;
	}
	
	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		serverSystem.shutdown();
	}
	
	/**
	 * Registers a module at the server
	 * @param module The module to register. A module can only be registered once
	 * @param name The name of the module
	 * @param version The version
	 * @throws IllegalArgumentException when this module is already registered
	 */
	public void register(ServerModule module, String name, String version){
		serverSystem.register(module, name, version);
	}
}
