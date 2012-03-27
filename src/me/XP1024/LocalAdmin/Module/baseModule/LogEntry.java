package me.XP1024.LocalAdmin.Module.baseModule;

import java.io.Serializable;
import java.util.logging.Level;

public class LogEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7577037646474819806L;
	
	private String entry;
	private Level level;
	
	public LogEntry(String entry, Level level) {
		super();
		this.entry = entry;
		this.level = level;
	}
	
	String getEntry() {
		return entry;
	}
	void setEntry(String entry) {
		this.entry = entry;
	}
	Level getLevel() {
		return level;
	}
	void setLevel(Level level) {
		this.level = level;
	}
	
	
	
}
