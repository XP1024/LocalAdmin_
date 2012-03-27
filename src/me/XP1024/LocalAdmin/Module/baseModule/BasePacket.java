package me.XP1024.LocalAdmin.Module.baseModule;

import java.io.Serializable;

public class BasePacket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1750511009026932191L;
	
	private PacketType type;
	private Serializable data;
	
	public BasePacket(PacketType type, Serializable data) {
		super();
		this.type = type;
		this.data = data;
	}
	
	PacketType getType() {
		return type;
	}
	
	void setType(PacketType type) {
		this.type = type;
	}
	Serializable getData() {
		return data;
	}
	void setData(Serializable data) {
		this.data = data;
	}


}
