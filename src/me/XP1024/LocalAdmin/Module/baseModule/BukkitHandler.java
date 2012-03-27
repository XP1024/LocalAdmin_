package me.XP1024.LocalAdmin.Module.baseModule;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitHandler extends Handler implements Runnable{
	ServerBaseModule module;
	JavaPlugin plugin;

	public BukkitHandler(ServerBaseModule module, JavaPlugin plugin){
		this.module = module;
		this.plugin = plugin;
		plugin.getLogger().addHandler(this);
	}
	
	public void kick(String name){
		sendCommand("/kick " + name);
	}
	
	public void ban(String name){
		sendCommand("/ban " + name);
	}
	
	public void shutdown(){
		plugin.getServer().shutdown();
	}
	
	
	public void sendCommand(String command) {
		plugin.getServer().getConsoleSender().sendMessage(command);
	}
	
	
	public void log(Level level, String msg){
		plugin.getLogger().log(level, "[" + plugin.getName() + "]" +  msg);		
	}

	@Override
	public void publish(LogRecord record) {
		BasePacket packet = new BasePacket(PacketType.PLAYERLIST, new LogEntry(record.getMessage(), record.getLevel()));
		
		try {
			module.sendToAll(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void flush() {}

	@Override
	public void close() throws SecurityException {}

	@Override
	public void run() {
		long available = Runtime.getRuntime().totalMemory();
		long used = available - Runtime.getRuntime().freeMemory();	
		
		//Byte -> MegaByte
		available /= 1024;
		available /= 1024;
		
		used /= 1024;
		used /= 1024;
		
		MemoryUsage memory = new MemoryUsage((int)used, (int)available);
		BasePacket packet = new BasePacket(PacketType.RAMUSAGE, memory);
		
		try {
			module.sendToAll(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





}
