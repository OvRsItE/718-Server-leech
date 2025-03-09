package com.rs.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.rs.game.world.entity.player.Player;
import com.rs.utility.SerializableFilesManager;

public class SaveEditor extends JFrame {

	private static final long serialVersionUID = 147116517709513176L;

	private static final int FRAME_WIDTH = 250;
	private static final int FRAME_HEIGHT = 300;
	private static final String TITLE = "718+ player save editor";
	private Player playerToEdit;
	
	private static Map<String, Component> components;
	
	public static Map<String, Component> getComponents1() {
		return components;
	}
	
	private Method[] playerMethods;
	
	private void addTextField(String name, Object value) {
		JFormattedTextField textField = new JFormattedTextField(value);
		textField.setColumns(15);
		addLabel(name + "label", name + ":");
		components.put(name, textField);
	}
	
	private JMenuBar addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePlayer();
			}
		});
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadPlayer();
			}
		});
		file.add(save);
		file.add(load);
		menuBar.add(file);
		return menuBar;
	}
	
	private void addLabel(String name, String text) {
		JLabel label = new JLabel(text);
		components.put(name, label);
	}
	
	private boolean validReturnType(String returnType) {
		return returnType.equals("int") ||
				returnType.equals("long") ||
				returnType.equals("boolean") ||
				returnType.equals("double") ||
				returnType.equals("float") ||
				returnType.equals("char") ||
				returnType.equals("byte") ||
				returnType.equals("short") ||
				returnType.equals("class java.lang.String");
	}
	
	private JLabel statusLabel = new JLabel();
	public static JPanel editorContainer = new JPanel();
	
	private void addComponents() {
		if (playerToEdit == null) {
			return;
		}
		statusLabel.setText("Currently editing: " + playerToEdit.getUsername());
		this.add(statusLabel, BorderLayout.NORTH);
		for (Method method : playerMethods) {
			try {
				Object methodValue = method.invoke(playerToEdit);
				String methodName = method.getName();
				if (methodName.equals("toString") || methodName.contains("packet") || methodName.equals("getUsername") 
					|| methodName.equals("getPassword") || methodName.contains("MapSize") || methodName.contains("getClientIndex")
					|| methodName.contains("getIndex") || methodName.contains("getNextFaceEntity")) {
					continue;
				}
				if (methodValue == null) {
					continue;
				}
				if (!validReturnType(method.getReturnType().toString())) {
					continue;
				}
				addTextField(methodName, method.invoke(playerToEdit));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				continue;
			}
		}
		editorContainer.setLayout(new GridLayout(0, 1, 0, 5));
		for (Map.Entry<String, Component> entry : components.entrySet()) {
			editorContainer.add(entry.getValue());
		}
		JScrollPane scrollPane = new JScrollPane(editorContainer);
		scrollPane.setBounds(1, 20, FRAME_WIDTH - 5, FRAME_HEIGHT - 5);
		this.add(scrollPane);
		this.add(new SearchPanel(), BorderLayout.SOUTH);
		this.revalidate();
	}
	
	private void updateComponents() {
		statusLabel.setText("Currently editing: " + playerToEdit.getUsername());
		for (Entry<String, Component> entry : components.entrySet()) {
			if (entry.getKey().contains("label")) {
				continue;
			}
			if (entry.getValue() instanceof JLabel) {
				continue;
			}
			for (Method method : playerMethods) {
				if (!entry.getKey().equals(method.getName())) {
					continue;
				}
				if (method.getParameterTypes().length > 0) {
					continue;
				}
				if (method.getName().equals("hasWalkSteps")) {
					continue;
				}
				try {
					((JFormattedTextField) entry.getValue()).setValue(method.invoke(playerToEdit));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public SaveEditor() {
		super(TITLE);
		this.setUndecorated(false);
		this.setResizable(false);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setLayout(new BorderLayout());
		this.setEnabled(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		components = new LinkedHashMap<String, Component>();
		this.setJMenuBar(addMenuBar());
		this.setVisible(true);
	}
	
	private void loadPlayer() {
		final String playerName = JOptionPane.showInputDialog(null, "Enter the name of the player you wish to edit: ", "718+ player save editor", JOptionPane.INFORMATION_MESSAGE);
		playerToEdit = SerializableFilesManager.loadPlayer(playerName);
		if (playerToEdit == null) {
			JOptionPane.showMessageDialog(null, "No such player save.");
			return;
		}
		playerMethods = playerToEdit.getClass().getMethods();
		if (components.size() == 0) {
			addComponents();
		} else {
			updateComponents();
		}
	}
	
	private void savePlayer() {
		if (playerToEdit == null) {
			return;
		}
		for (Method method : playerMethods) {
			String methodName = method.getName();
			if (!methodName.startsWith("set")) {
				continue;
			}
			String getter = methodName.replace("set", "get");
			if (!components.containsKey(getter)) {
				getter = methodName.replace("set", "is");
				if (!components.containsKey(getter)) {
					getter = methodName.replace("set", "has");
					if (!components.containsKey(getter)) {
						continue;
					}
				}
			}
			Object value = ((JFormattedTextField) components.get(getter)).getValue();
			try {
				if (value != null) {
					method.invoke(playerToEdit, value);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println("Incorrect parameter type: " + method.getName() + ", type: " + value.getClass() + ", required type: " + method.getParameterTypes()[0]);
				e.printStackTrace();
			}
		}
		SerializableFilesManager.savePlayer(playerToEdit);
		JOptionPane.showMessageDialog(null, playerToEdit.getUsername() + " has been saved!");
	}

	public static void main(String[] args) {
		new SaveEditor();
	}

}

class SearchPanel extends JPanel {

	private static final long serialVersionUID = -5097053212702527694L;

	public static final JTextField searchTextField = new JTextField(15);
	
	public SearchPanel() {
		super();
		this.add(new JLabel("Search: "), BorderLayout.EAST);
		searchTextField.addKeyListener(KEY_LISTENER);
		this.add(searchTextField, BorderLayout.WEST);
	}
	
	private final static KeyListener KEY_LISTENER = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent event) {

		}

		@Override
		public void keyReleased(KeyEvent event) {
			for (Entry<String, Component> entry : SaveEditor.getComponents1().entrySet()) {
				if (entry.getKey().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
					SaveEditor.editorContainer.add(entry.getValue());
				} else {
					SaveEditor.editorContainer.remove(entry.getValue());
				}
			}
			SaveEditor.editorContainer.revalidate();
		}

		@Override
		public void keyTyped(KeyEvent event) {
			
		}

	};

}