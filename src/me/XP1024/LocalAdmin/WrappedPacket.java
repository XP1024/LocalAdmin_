package me.XP1024.LocalAdmin;

import java.io.Serializable;


public class WrappedPacket implements Serializable {

	private static final long serialVersionUID = -8201198669655713750L;
	
	private int moduleID;
	private transient String user;
	private Serializable data;
	
	public WrappedPacket(int moduleID, String user, Serializable data){
		this.moduleID = moduleID;
		this.user = user;
		this.data = data;
	}
	
	public int getModuleID() {
		return moduleID;
	}
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Serializable getData() {
		return data;
	}
	public void setData(Serializable data) {
		this.data = data;
	}
}
