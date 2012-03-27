package me.XP1024.LocalAdmin.Module.baseModule;

import java.io.Serializable;

public class MemoryUsage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2453500218758872425L;
	
	int used, available;
	
	public MemoryUsage(int used, int available) {
		super();
		this.used = used;
		this.available = available;
	}
	
	int getUsed() {
		return used;
	}

	void setUsed(int used) {
		this.used = used;
	}

	int getAvailable() {
		return available;
	}

	void setAvailable(int available) {
		this.available = available;
	}



}
