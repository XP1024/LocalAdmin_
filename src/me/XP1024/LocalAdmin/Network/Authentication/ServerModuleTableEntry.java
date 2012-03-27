package me.XP1024.LocalAdmin.Network.Authentication;

import java.io.Serializable;

public class ServerModuleTableEntry implements Serializable{

	private static final long serialVersionUID = 6481787570650817424L;

	private String name, version;
	private int moduleID;

	
	public ServerModuleTableEntry(String name, String version, int moduleID) {
		this.setName(name);
		this.setVersion(version);
		this.moduleID = moduleID;
	}
	public int getModuleID() {
		return moduleID;
	}
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
