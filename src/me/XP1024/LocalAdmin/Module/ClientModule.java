package me.XP1024.LocalAdmin.Module;

import java.io.IOException;
import java.io.Serializable;

import javax.swing.JPanel;

import me.XP1024.LocalAdmin.WrappedPacket;

public abstract class ClientModule extends Module {

	/**
	 * This method is called whenever a packet for this module is received
	 * @param packet The user-defined packet
	 * @param user The user who sent this packet
	 */
	public abstract void update(Serializable data);
	
	/**
	 * Sends a packet to a specified user.
	 * <br>The module has to be registered before the first use.
	 * <br>This method is thread-safe.
	 * @see ModuleSystem#register(Module module, String name, String version)
	 * @param packet The packet to send
	 * @param target The target who should receive the packet
	 * @throws IOException If an error has occurred while sending the packet
	 */
	public synchronized void send(Serializable packet) throws IOException {
		if(id == -1 && moduleSystem == null)
			throw new IllegalStateException("Module is not registered");
		
		//You send to the server whatever is specified in target. This is an unexpected behavior and i'm not happy with it 
		moduleSystem.send(new WrappedPacket(id, "Server", packet));
	}
	
	void update(WrappedPacket packet){
		update(packet.getData());
	}
	
	public abstract void draw(JPanel panel);
	public abstract void destroy();
}
