package me.XP1024.LocalAdmin.Client.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import me.XP1024.LocalAdmin.Client.Localisation;
import me.XP1024.LocalAdmin.Client.PluginManager;
import me.XP1024.LocalAdmin.Module.ClientModule;
import me.XP1024.LocalAdmin.Module.ModuleSystem;

public class MainWindow {
	
	final String version = "0.1";
	final String title = "LocalAdmin Client v" + version;
	
	JFrame frame;
	
	
	private JMenuItem connectMenuItem;
	private JMenuItem disconnectMenuItem;
	private JMenuBar menuBar;
	
	private JTabbedPane pluginsPane;

	
	PluginManager plugins;
	ModuleSystem moduleSystem;

	
	
	public MainWindow(){
		
		moduleSystem = new ModuleSystem();		
		
		frame = new JFrame(title);
		
		JMenuItem connectMenuItem = new JMenuItem(Localisation.getString("menuConnectTo"));
		connectMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();				
			}

		});
		
		JMenuItem disconnectMenuItem = new JMenuItem(Localisation.getString("menuDisconnect"));
		disconnectMenuItem.setEnabled(false);
		disconnectMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				disconnect();
			}
		});
		
		JMenuItem pluginManagerMenuItem = new JMenuItem(Localisation.getString("menuPluginManager"));
		pluginManagerMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showPluginManager();
			}
		});
		
		JMenu connectionMenu = new JMenu(Localisation.getString("menuConnection"));
		connectionMenu.add(connectMenuItem);
		connectionMenu.add(disconnectMenuItem);
		
		JMenu pluginsMenu = new JMenu(Localisation.getString("menuPlugins"));
		pluginsMenu.add(pluginManagerMenuItem);
		
		menuBar = new JMenuBar();
		menuBar.add(connectionMenu);
		menuBar.add(pluginsMenu);
		
		
		pluginsPane = new JTabbedPane(JTabbedPane.TOP);

		plugins = new PluginManager(moduleSystem);
		plugins.load();
		setupPluginTabs();
		
		
		frame.setJMenuBar(menuBar);	
		frame.getContentPane().add(pluginsPane);
		frame.setSize(1024, 600);
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		
		frame.setVisible(true);
	}

	
	
	
	
	
	
	private void setupPluginTabs() {		
		JPanel pluginPanel = null;	
		List<ClientModule> modules = plugins.getModules();
		for(ClientModule module : modules){
			pluginPanel = new JPanel();
			module.draw(pluginPanel);
	
			pluginsPane.add(pluginPanel);			
		}
	}







	protected void showPluginManager() {
		// TODO Auto-generated method stub
		
	}

	protected void connect() {
		// TODO Auto-generated method stub
		
	}

	protected void disconnect() {
		// TODO Auto-generated method stub
		
	}

}
