package me.XP1024.LocalAdmin.Client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.XP1024.LocalAdmin.Module.ClientModule;
import me.XP1024.LocalAdmin.Module.ModuleSystem;
import me.XP1024.LocalAdmin.Module.baseModule.ClientBaseModule;
import me.XP1024.LocalAdmin.Network.Authentication.ServerModuleTableEntry;

public class PluginManager {
	
	Set<ModuleTableEntry> modulesTable;
	ModuleSystem moduleSystem;

	public PluginManager(ModuleSystem moduleSystem){
		modulesTable =  new HashSet<ModuleTableEntry>();
		this.moduleSystem = moduleSystem;
	}
	
	public void load() {
		ClientBaseModule base = new ClientBaseModule();
		modulesTable.add(new ModuleTableEntry(base, Localisation.getString("basePlugin.Name"), "1.0", 0));	
	}

	public List<ClientModule> getModules() {
		
		ClientModule[] modules = new ClientModule[modulesTable.size()]; 
		
		for(ModuleTableEntry entry : modulesTable){
			modules[entry.getPosition()] = entry.getModule();
		}
				
		return Arrays.asList(modules);
	}
	
	public void setServerPluginList(List<ServerModuleTableEntry> serverModules){
		for(ServerModuleTableEntry serverEntry : serverModules){
			for(ModuleTableEntry localEntry : modulesTable){
				if(serverEntry.getName().equals(localEntry.getName()) && serverEntry.getVersion().equals(localEntry.getVersion())){
					localEntry.setServerModuleID(serverEntry.getModuleID());
				}
			}
		}
	}
	
	public void registerModules(){
		for(ModuleTableEntry entry : modulesTable){
			if(entry.isEnabled()){
				int moduleID = entry.getServerModuleID();
				moduleSystem.register(entry.getModule(), moduleID, entry.getName(), entry.getVersion());
			}
		}
	}
 }

class ModuleTableEntry {
	
	private ClientModule module;
	private String name;
	private String version;
	private int position;
	private int serverModuleID;
	private boolean enabled;
	
	public ModuleTableEntry(ClientModule module, String name, String version,
			int position) {
		super();
		this.module = module;
		this.name = name;
		this.version = version;
		this.position = position;
		this.serverModuleID = -1;
		this.setEnabled(true);
	}
	

	ClientModule getModule() {
		return module;
	}
	void setModule(ClientModule module) {
		this.module = module;
	}
	String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	String getVersion() {
		return version;
	}
	void setVersion(String version) {
		this.version = version;
	}
	int getPosition() {
		return position;
	}
	void setPosition(int position) {
		this.position = position;
	}
	int getServerModuleID(){
		return serverModuleID;
	}
	void setServerModuleID(int serverModuleID){
		this.serverModuleID = serverModuleID;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}