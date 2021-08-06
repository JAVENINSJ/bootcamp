package stuntMan;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Set;
import javax.swing.*;

import inputClasses.Audio;
import inputClasses.JavaLabel;
import inputClasses.JavaLayeredPane;
import passwords.Password;
import settings.Settings;
import settings.SettingsBuilder;

public class MainMenu implements ActionListener {
	public static Settings settings;
	public static boolean audio = true, trail = true;
	public static Audio music = new Audio();
	public static String fPath = "//MenuAssets//";
	public static String mainButtonPos = "Upgrades";
	public static JFrame screen;
	public static Timer timer;
	static JTextArea enterName, enterPassword;
	public static int fWidth = 1000, fHeight = 500, xBounds = fWidth / 2 - 180, yBounds = fHeight - 276;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			buttons = new HashMap<String, JavaLabel>(), //
			backgr = new HashMap<String, JavaLabel>(); //
	public static Set<String> backgrKeys;

	public MainMenu() {
		screen = setupScreen(fWidth + 16, fHeight + 40);// +16 and +40 because JFrame has sizing issues
		setupLayers();
		setupLabels();
		enterName = setupInputBox(enterName, "Enter Username", xBounds * 16 / 10, 168);
		enterPassword = setupInputBox(enterPassword, "Enter Password", xBounds * 16 / 10, 50 * 6);
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution

	}

	public static void switchScreens(String screenName) {
		for (String backgrKey : backgrKeys) {
			if (backgrKey.equals(screenName)) {
				layers.get(backgrKey).setLocation(0, 0);
			} else {
				layers.get(backgrKey).setLocation(0, -fHeight);
			}
		}
		if (!screenName.equals("Main") && !screenName.equals(mainButtonPos)) {
			mainButtonPos = screenName;
			layers.get(screenName).add(buttons.get("Main Menu"));
		}
	}

	public static void checkLogin(boolean createUser) {
		String name = enterName.getText();
		String password = enterPassword.getText();
		boolean fail = false;
		labels.get("Enter Username").setForeground(Color.white);
		labels.get("Enter Password").setForeground(Color.white);
		labels.get("Username must be atleast 3 characters long!").setForeground(Color.white);
		labels.get("Password must be atleast 3 characters long!").setForeground(Color.white);
		labels.get("User not found!").setVisible(false);
		labels.get("Wrong Password!").setVisible(false);
		if (name.isBlank()) {
			fail = fail("Enter Username");
		}
		if (password.isBlank()) {
			fail = fail("Enter Password");
		}
		if (name.length() < 3) {
			fail = fail("Username must be atleast 3 characters long!");
		}
		if (password.length() < 3) {
			fail = fail("Password must be atleast 3 characters long!");
		}
		if (!fail) {
			if (createUser) {
				createUser(name,password);
			} else {
				loginUser(name,password);
			}
		}
	}
	
	static void updateSettings() {
		audio = settings.getAudio();
		settings.getDayTime();
		settings.getTrail();
		String resolution = settings.getResolution();
		fWidth = Integer.parseInt(resolution.substring(0,resolution.indexOf('x')));
		fHeight = Integer.parseInt(resolution.substring(resolution.indexOf('x')+1));
	}
	
	static boolean fail(String labelName) {
		labels.get(labelName).setForeground(Color.red);
		return false;
	}

	static void loginUser(String username, String password) {
		if (!Password.json.containsKey(username)) {
			labels.get("User not found!").setVisible(true);
			return;
		}
		if (!Password.checkPassword(username, password)) {
			labels.get("Wrong Password!").setVisible(true);
			return;
		}
		switchScreens("Main");
		settings = new SettingsBuilder().getSettings(username);
		updateSettings();
		music.play("happyRock.wav");
	}

	static void createUser(String username, String password) {

	}

	static JTextArea setupInputBox(JTextArea field, String text, int x, int y) {
		field = new JTextArea();
		field.setBounds(x, y, 400, 32);
		field.setFont(new Font("Verdana", Font.BOLD, 26));
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		layers.get("Login").add(field, (Integer) 20);
		return field;
	}

	static void setupLabels() {
		new JavaLabel("Skip", layers.get("Login"), xBounds * 6 / 10, 50 * 0, 200, 50, buttons);
		
		new JavaLabel("BackGroundLogin", layers.get("Login"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Log In", layers.get("Login"), xBounds * 3 / 10, 100, 400, 100, buttons);
		new JavaLabel("Create User", layers.get("Login"), xBounds * 3 / 10, 300, 400, 100, buttons);
		new JavaLabel("Enter Username", layers.get("Login"), xBounds * 16 / 10, 136, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Enter Password", layers.get("Login"), xBounds * 16 / 10, 268, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Username must be atleast 3 characters long!", 
				layers.get("Login"), xBounds * 3 / 10, 216, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Password must be atleast 3 characters long!",
				layers.get("Login"), xBounds * 3 / 10, 250, 400, 32, labels, 1, fPath, true);
		new JavaLabel("User not found!", layers.get("Login"), xBounds * 16 / 10, 200, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Wrong Password!", layers.get("Login"), xBounds * 16 / 10, 332, 400, 32, labels, 1, fPath, true);
		
		new JavaLabel("BackGroundMain", layers.get("Main"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Play", layers.get("Main"), xBounds * 9 / 10, 50 * 1, 400, 80, buttons);
		new JavaLabel("Upgrades", layers.get("Main"), xBounds * 9 / 10, 50 * 3, 400, 80, buttons);
		new JavaLabel("Settings", layers.get("Main"), xBounds * 9 / 10, 50 * 5, 400, 80, buttons);
		new JavaLabel("Exit", layers.get("Main"), xBounds * 9 / 10, 50 * 7, 400, 80, buttons);
		
		new JavaLabel("BackGroundUpgrades", layers.get("Upgrades"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Main Menu", layers.get("Upgrades"), xBounds * 9 / 10, fHeight - 100, 400, 100, buttons);
		
		new JavaLabel("BackGroundSettings", layers.get("Settings"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		
		labels.get("User not found!").setVisible(false);
		labels.get("User not found!").setForeground(Color.red);
		labels.get("Wrong Password!").setVisible(false);
		labels.get("Wrong Password!").setForeground(Color.red);
		buttons.get("Main Menu").setVisible(false);
		
	}

	static void setupLayers() {
		new JavaLayeredPane("Login", screen, 0, 0, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Main", screen, 0, -fHeight, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Upgrades", screen, 0, -fHeight, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Settings", screen, 0, -fHeight, fWidth, fHeight, layers, 0);
		backgrKeys = layers.keySet();
	}

	static JFrame setupScreen(int frameWidth, int frameHeight) {
		JFrame frame = new JFrame("Game");
		frame.setSize(frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.black);
		frame.setIconImage(new ImageIcon("logo.png").getImage());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return frame;
	}
}
