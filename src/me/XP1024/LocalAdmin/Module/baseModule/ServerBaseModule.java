package me.XP1024.LocalAdmin.Module.baseModule;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;

import me.XP1024.LocalAdmin.Module.ServerModule;

public class ServerBaseModule extends ServerModule {

	BukkitHandler handler;
	
	@Override
	public void update(Serializable data, String user) {
		BasePacket packet = (BasePacket)data;
		
		switch (packet.getType()) {
		case CONSOLECOMMAND:{
				String command = (String)packet.getData();
				handler.log(Level.INFO, user + " processed command " + command);
				handler.sendCommand(command);
			}
			break;
		case SHUTDOWN:
			handler.log(Level.INFO, "Server shutdown initiated by " + user);
			break;
		case RESTART:
			handler.log(Level.SEVERE, "Restart is not yet implemeted");
			break;
		case KICK:{
				String target = (String)packet.getData();
				handler.log(Level.INFO, user + " kicked " + target);
				handler.kick(target);
			}
			break;
		case BAN:{
				String target = (String)packet.getData();
				handler.log(Level.INFO, user + " banned " + target);
				handler.ban(target);
			}  
			break;
		default:
			break;
		
		}
		
	}

	public void sendToAll(Serializable data) throws IOException {
		for(String user : getOnlineUsers())
			send(data, user);
	}
	
	public void setHandler(BukkitHandler handler){
		this.handler = handler;
	}

	public String getVersion() {
		return "1.0";
	}

	public String getName() {
		return "BasePlugin";
	}
}
