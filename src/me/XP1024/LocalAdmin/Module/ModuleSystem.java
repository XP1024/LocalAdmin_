package me.XP1024.LocalAdmin.Module;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.XP1024.LocalAdmin.WrappedPacket;
import me.XP1024.LocalAdmin.Network.NetworkManager;




public class ModuleSystem implements Observer {
	private Map<Integer, ModuleTableEntry> modules;
	private int nextModuleID;
	
	private NetworkManager network;
	private ConcurrentLinkedQueue<WrappedPacket> packetQueue;
	
	public ModuleSystem(){
		modules = new HashMap<Integer, ModuleTableEntry>();	
		nextModuleID = 0;
		
		network = null;
		packetQueue = new ConcurrentLinkedQueue<WrappedPacket>();
		
	}
	
	public void setNetworkManager(NetworkManager network){
		this.network = network;
	}
	
	
	/**
	 * Registers a module at the server
	 * @param module The module to register. A module can only be registered once
	 * @param name The name of the module
	 * @param version The version
	 * @throws IllegalArgumentException when this module is already registered
	 */
	public void register(Module module, String name, String version) {
		register(module, nextModuleID, name, version);
	}
	
	public void register(Module module, int id,String name, String version) {
		
		if(id < 0)
			throw new IllegalArgumentException("Invalid ID");
		
		for(int ID : modules.keySet()){
			ModuleTableEntry entry = modules.get(ID);
			if(entry.getModule() == module || entry.getName().equals(name) && entry.getVersion().equals(version))
				throw new IllegalArgumentException("Module is already registered");
		}

		modules.put(id, new ModuleTableEntry(module, name, version));
		module.initialize(id, this);
		
		if(nextModuleID >= id)
			nextModuleID++;
		else
			nextModuleID = id + 1;
	}
	
	public void unregister(Module module){
		for(ModuleTableEntry entry : modules.values())
			if(entry.getModule() == module)
				modules.remove(entry);		
	}
	
	
	void send(WrappedPacket packet) throws IOException {
		if(network == null)
			throw new IllegalStateException("NetworkManager not set");
		
		network.send(packet);
	}
	
	public void deliver() {
		for(WrappedPacket packet : packetQueue){
			ModuleTableEntry entry = modules.get(packet.getModuleID());
			
			if(entry == null)
				//TODO post error to log and maybe also to the connected clients
				System.err.println("Received packet with non-existing moduleID");
			else
				try{
					entry.getModule().update(packet);
				} catch (Throwable t) {
					//TODO post error
					t.printStackTrace();
				}
			
			packetQueue.remove(packet);
		}
	}
	
	public void shutdown() {
		network.shutdown();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!(arg instanceof WrappedPacket))
			throw new IllegalArgumentException("Argument is not a WrappedPacket");
		
		packetQueue.add((WrappedPacket) arg);		
	}

	Set<String> getOnlineUsers() {
		return network.getOnlineUsers();
	}	
}


class ModuleTableEntry{
	private Module module;
	private String name;
	private String version;
	
	
	public ModuleTableEntry(Module module, String name, String version) {
		super();
		this.module = module;
		this.name = name;
		this.version = version;
	}
	
	public Module getModule() {
		return module;
	}
	public void setModule(ServerModule module) {
		this.module = module;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
