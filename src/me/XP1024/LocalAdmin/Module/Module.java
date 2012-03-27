package me.XP1024.LocalAdmin.Module;

import me.XP1024.LocalAdmin.WrappedPacket;

public abstract class Module {

	protected int id = -1;
	protected ModuleSystem moduleSystem = null;

	protected final void initialize(int id, ModuleSystem moduleSystem) {
		this.id = id;
		this.moduleSystem = moduleSystem;
	}
	
	abstract void update(WrappedPacket packet);

}