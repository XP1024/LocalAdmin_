package me.XP1024.LocalAdmin.Module.baseModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.minecraft.server.PlayerListBox;

import me.XP1024.LocalAdmin.Client.Localisation;
import me.XP1024.LocalAdmin.Module.ClientModule;

public class ClientBaseModule extends ClientModule {

	
	private DefaultListModel logListModel;
	private DefaultListModel playersListModel;
	private JPanel mainPanel;
	JLabel playersOnlineLabel;
	JProgressBar ramUsageBar;
	int maximumRam;
	
	
	@Override
	public void update(Serializable data) {
		try{
			BasePacket packet = (BasePacket)data;
			switch (packet.getType()) {
			case RAMUSAGE:
				setRamUsage(packet.getData());
				break;
			case PLAYERLIST:
				setPlayerlist(packet.getData());
				break;
			case LOGENTRY:
				addLogEntry(packet.getData());				
				break;
			default:
				break;
			}
			
		}catch(ClassCastException e){
			System.err.println("Wrong packet");
		}

	}

	private void addLogEntry(Serializable data) {
		// TODO Auto-generated method stub
		
	}

	private void setPlayerlist(Serializable dat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(JPanel panel) {
		mainPanel = panel;
		mainPanel.setLayout(new BorderLayout());

		mainPanel.setName(Localisation.getString("basePlugin.Name"));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		
		mainPanel.add(setupRightPanel(), BorderLayout.EAST);
		mainPanel.add(setupLogPanel(), BorderLayout.CENTER);
		mainPanel.add(setupInfoPanel(), BorderLayout.SOUTH);
		
		setMaxRamUsage(0);
		setRamUsage(0);

	}
	
	private JPanel setupInfoPanel() {
		JPanel infoPanel = new JPanel(new BorderLayout());
		
		//UIManager.put("ProgressBar.selectionForeground", Color.black);
		//UIManager.put("ProgressBar.selectionBackground", Color.black);
		
		JButton shutdownButton = new JButton(Localisation.getString("basePlugin.shutdown"));
		shutdownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shutdownServer();				
			}
		});
		
		JButton restartButton = new JButton(Localisation.getString("basePlugin.restart"));
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartServer();				
			}
		});
		
		ramUsageBar = new JProgressBar();
		ramUsageBar.setStringPainted(true);
			
		ramUsageBar.setMaximumSize(new Dimension(30, 13));
		JLabel ramUsageLabel = new JLabel("RAM:");
		
		Box upperBox = Box.createHorizontalBox();
		upperBox.add(ramUsageLabel);
		
		Box lowerBox = Box.createHorizontalBox();
		lowerBox.add(ramUsageBar);
		lowerBox.add(Box.createHorizontalGlue());
		lowerBox.add(shutdownButton);
		lowerBox.add(restartButton);
		
		infoPanel.add(upperBox, BorderLayout.NORTH);
		infoPanel.add(lowerBox, BorderLayout.SOUTH);
		
		
		return infoPanel;
	}


	private JPanel setupLogPanel() {
		JPanel logPanel = new JPanel(new BorderLayout());
		
		logListModel = new DefaultListModel();
		JList logList = new JList(logListModel);
		JScrollPane logScrollList = new JScrollPane(logList);
				
		logPanel.add(logScrollList, BorderLayout.CENTER);
		logPanel.add(setupSendCommandBox(), BorderLayout.SOUTH);
		
		return logPanel;
	}

	private Box setupSendCommandBox() {
		Box controlBox = Box.createHorizontalBox();
		
		JButton showFiltersButton = new JButton(Localisation.getString("basePlugin.showFilters"));
		final JTextField consoleInputField = new JTextField();
		
		JButton sendCommandButton = new JButton(Localisation.getString("basePlugin.sendCommand"));	
		sendCommandButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendConsoleCommand(consoleInputField.getText());
			}
		});
		
		controlBox.add(showFiltersButton);
		controlBox.add(Box.createHorizontalStrut(20));
		controlBox.add(consoleInputField);
		controlBox.add(sendCommandButton);
		controlBox.add(Box.createHorizontalGlue());
		
		return controlBox;
	}
	


	private JPanel setupRightPanel(){
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		rightPanel.add(setupPlayersList(), BorderLayout.CENTER);
		rightPanel.add(setupMakroBox(), BorderLayout.SOUTH);
		
		return rightPanel;
	}
	
	private JPanel setupPlayersList(){
		playersListModel = new DefaultListModel();
		JList playersList = new JList(playersListModel);
		JScrollPane playersScrollList = new JScrollPane(playersList);
		
		JPanel playersListPanel = new JPanel(new BorderLayout());
		
		
		playersOnlineLabel = new JLabel();
		Box box = Box.createHorizontalBox();
		box.add(playersOnlineLabel);
		box.add(Box.createHorizontalGlue());
		
		playersListPanel.add(box, BorderLayout.NORTH);
		playersListPanel.add(playersScrollList, BorderLayout.CENTER);
		
		setPlayersOnline(0);
		playersListPanel.setPreferredSize(new Dimension(1, 1));
		
		return playersListPanel;
	}
	
	private Box setupMakroBox(){
		Box box = Box.createHorizontalBox();
		
		//TODO macros
		JButton makro1Button = new JButton("1");
		JButton makro2Button = new JButton("2");
		JButton makro3Button = new JButton("3");
		JButton makro4Button = new JButton("4");
		
		box.add(Box.createHorizontalGlue());
		box.add(makro1Button);
		box.add(Box.createHorizontalGlue());
		box.add(makro2Button);
		box.add(Box.createHorizontalGlue());
		box.add(makro3Button);
		box.add(Box.createHorizontalGlue());
		box.add(makro4Button);
		box.add(Box.createHorizontalGlue());
		
		return box;
	}


	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	
	private void setPlayersOnline(int count){
		playersOnlineLabel.setText(Localisation.getString("basePlugin.Player") + ": " + count);
	}
	
	private void setMaxRamUsage(int maximum){
		ramUsageBar.setMaximum(maximum);
		maximumRam = maximum;
	}
	
	private void setRamUsage(Serializable data){
		MemoryUsage mem = (MemoryUsage)data;
		setMaxRamUsage(mem.getAvailable());
		setRamUsage(mem.getUsed());
	}
	
	private void setRamUsage(int current){	
		ramUsageBar.setValue(current);
		ramUsageBar.setString(current + "/" + maximumRam + " MB");
	}
	
	public void send(BasePacket packet){
		try {
			super.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected void sendConsoleCommand(String command) {
		send(new BasePacket(PacketType.CONSOLECOMMAND, command));
	}
	
	protected void restartServer() {
		send(new BasePacket(PacketType.RESTART, null));	
	}

	protected void shutdownServer() {
		send(new BasePacket(PacketType.SHUTDOWN, null));
	}


}
