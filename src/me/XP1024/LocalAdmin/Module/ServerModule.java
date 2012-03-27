package me.XP1024.LocalAdmin.Module;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import me.XP1024.LocalAdmin.WrappedPacket;



public abstract class ServerModule extends Module {
	/**
	 * This method is called whenever a packet for this module is received
	 * @param packet The user-defined packet
	 * @param user The user who sent this packet
	 */
	public abstract void update(Serializable data, String user);
	
	/**
	 * Sends a packet to a specified user.
	 * <br>The module has to be registered before the first use.
	 * <br>This method is thread-safe.
	 * @see ModuleSystem#register(Module module)
	 * @param packet The packet to send
	 * @param target The target who should receive the packet
	 * @throws IOException If an error has occurred while sending the packet
	 */
	public synchronized void send(Serializable packet, String target) throws IOException {
		if(id == -1 && moduleSystem == null)
			throw new IllegalStateException("Module is not registered");
		
		moduleSystem.send(new WrappedPacket(id, target, packet));
	}
	
	/**
	 * Gives you all users that are currently successfully authenticated
	 * @return The list of online users
	 */
	public Set<String> getOnlineUsers(){
		return moduleSystem.getOnlineUsers();		
	}
	
	void update(WrappedPacket packet){
		update(packet.getData(), packet.getUser());
	}
}
