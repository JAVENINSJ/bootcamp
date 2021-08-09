package stuntMan;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Set;
import javax.swing.*;

import inputClasses.*;
import passwords.Password;
import settings.*;

public class MainMenu implements ActionListener {
	public static Settings settings;
	public static boolean audio = true, trail = true;
	public static Audio music = new Audio();
	public static String fPath = "//MenuAssets//";
	public static String MenuButtonPos = "Upgrades";
	public static JFrame screen;
	public static Timer timer;
	static JTextField enterName;
	static JPasswordField enterPassword;
	public static int fWidth = 1000, fHeight = 500, coins = 7420, moveToX = -fWidth, moveToY = 0;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			buttons = new HashMap<String, JavaLabel>(), backgr = new HashMap<String, JavaLabel>();
	public static Set<String> backgrKeys;

	public MainMenu() {
		if (System.getProperty("os.name").startsWith("Windows"))
			screen = setupScreen(fWidth + 16, fHeight + 40);// +16 and +40 because JFrame has sizing issues on windows!
		else
			screen = setupScreen(fWidth, fHeight);
		setupLayers();
		setupLabels();
		enterName = (JTextField) setupInputFields(enterName, (int) (fWidth * 0.55), (int) (fHeight * 0.33), false);
		enterPassword = (JPasswordField) setupInputFields(enterPassword, (int) (fWidth * 0.55), (int) (fHeight * 0.6),
				true);
		timer = new Timer(5, this);
		timer.start();
	}

	public Object setupInputFields(Object field, int x, int y, boolean password) {
		if (password) {
			field = new JPasswordField();
		} else {
			field = new JTextField();
		}
		((Component) field).setBounds(x, y, 400, 32);
		((Component) field).setFont(new Font("Verdana", Font.BOLD, 26));
		layers.get("Login").add(((Component) field), (Integer) 20);
		return field;
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution
		int x = layers.get("Main").getX();
		int y = layers.get("Main").getY();
		if (x != moveToX || y != moveToY) {
			layers.get("Main").setLocation(moveScreens(layers.get("Main").getX(), moveToX),
				moveScreens(layers.get("Main").getY(), moveToY));
		}
	}

	public static int moveScreens(int pos, int destinationPos) {
		if (pos != destinationPos) {
			int direction = -1;
			if (pos - destinationPos < 0) {
				direction = 1;
			}
			if ((pos > destinationPos) == (direction == -1)) {
				pos += direction * 50;
			}
		}
		return pos;
	}

	public static void switchScreens(String screenName) {
		for (String backgrKey : backgrKeys) {
			if (screenName.equals(backgrKey)) {
				moveToX = -layers.get(backgrKey).getX();
				moveToY = -layers.get(backgrKey).getY();
			}
		}
		if (!screenName.equals("Menu") && !screenName.equals(MenuButtonPos)) {
			MenuButtonPos = screenName;
			layers.get(screenName).add(buttons.get("Main Menu"));
		}
	}

	public static void checkLogin(boolean createUser) {
		String name = enterName.getText();
		String password = enterPassword.getPassword().toString();
		boolean fail = false;
		labels.get("Enter Username:").setForeground(Color.white);
		labels.get("Enter Password:").setForeground(Color.white);
		labels.get("Username must be atleast 3 characters long!").setForeground(Color.white);
		labels.get("Password must be atleast 3 characters long!").setForeground(Color.white);
		labels.get("User not found!").setVisible(false);
		labels.get("Wrong Password!").setVisible(false);
		if (name.isBlank()) {
			fail = fail("Enter Username:");
		}
		if (password.isBlank()) {
			fail = fail("Enter Password:");
		}
		if (name.length() < 3) {
			fail = fail("Username must be atleast 3 characters long!");
		}
		if (password.length() < 3) {
			fail = fail("Password must be atleast 3 characters long!");
		}
		if (!fail) {
			if (createUser) {
				createUser(name, password);
			} else {
				loginUser(name, password);
			}
		}
	}

	static void updateSettings() {
		audio = settings.getAudio();
		settings.getDayTime();
		settings.getTrail();
		String resolution = settings.getResolution();
		fWidth = Integer.parseInt(resolution.substring(0, resolution.indexOf('x')));
		fHeight = Integer.parseInt(resolution.substring(resolution.indexOf('x') + 1));
	}

	static boolean fail(String labelName) {
		labels.get(labelName).setForeground(Color.red);
		return true;
	}

	static void loginUser(String username, String password) {
		System.out.println("Name = "+username+"; Pass = "+password);
		if (!Password.json.containsKey(username)) {
			labels.get("User not found!").setVisible(true);
			return;
		}
		
		if (!Password.checkPassword(username, password)) {
			labels.get("Wrong Password!").setVisible(true);
			return;
		}
		setupMenuSettings();
		settings = SettingsBuilder.getSettings(username);
		updateSettings();
	}

	public static void setupMenuSettings() {
		switchScreens("Menu");
		new SettingsBuilder();
		music.play(JavaLabel.fRoute + "//happyRock.wav");
	}

	static void createUser(String username, String password) {
		// TODO DO THIS WHEN REINIS IS DONE WITH DATABASE
	}

	static void setupLabels() {
		new JavaLabel("Skip", layers.get("Login"), (int) (fWidth * 0.2), 0, 200, 50, buttons);

		new JavaLabel("BackGroundLogin", layers.get("Login"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Log In", layers.get("Login"), (int) (fWidth * 0.1), (int) (fHeight * 0.2), (int) (fWidth * 0.4),
				(int) (fHeight * 0.2), buttons);
		new JavaLabel("Create User", layers.get("Login"), (int) (fWidth * 0.1), (int) (fHeight * 0.6),
				(int) (fWidth * 0.4), (int) (fHeight * 0.2), buttons);
		new JavaLabel("Enter Username:", layers.get("Login"), (int) (fWidth * 0.5), (int) (fHeight * 0.27),
				(int) (fWidth * 0.4), (int) (fHeight * 0.064), labels, 1, fPath, true);
		new JavaLabel("Enter Password:", layers.get("Login"), (int) (fWidth * 0.5), (int) (fHeight * 0.54),
				(int) (fWidth * 0.4), (int) (fHeight * 0.064), labels, 1, fPath, true);
		new JavaLabel("Username must be atleast 3 characters long!", layers.get("Login"), 0, (int) (fHeight * 0.43),
				(int) (fWidth * 0.52), (int) (fHeight * 0.064), labels, 1, fPath, true);
		new JavaLabel("Password must be atleast 3 characters long!", layers.get("Login"), 0, (int) (fHeight * 0.5),
				(int) (fWidth * 0.52), (int) (fHeight * 0.064), labels, 1, fPath, true);
		new JavaLabel("User not found!", layers.get("Login"), (int) (fWidth * 0.5), (int) (fHeight * 0.45),
				(int) (fWidth * 0.4), (int) (fHeight * 0.064), labels, 1, fPath, true);
		new JavaLabel("Wrong Password!", layers.get("Login"), (int) (fWidth * 0.5), (int) (fHeight * 0.7),
				(int) (fWidth * 0.4), (int) (fHeight * 0.064), labels, 1, fPath, true);

		new JavaLabel("BackGroundMenu", layers.get("Menu"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Play", layers.get("Menu"), (int) (fWidth * 0.3), (int) (fHeight * 0.1), (int) (fWidth * 0.4),
				(int) (fHeight * 0.16), buttons);
		new JavaLabel("Upgrades", layers.get("Menu"), (int) (fWidth * 0.3), (int) (fHeight * 0.3), (int) (fWidth * 0.4),
				(int) (fHeight * 0.16), buttons);
		new JavaLabel("Settings", layers.get("Menu"), (int) (fWidth * 0.3), (int) (fHeight * 0.5), (int) (fWidth * 0.4),
				(int) (fHeight * 0.16), buttons);
		new JavaLabel("Exit", layers.get("Menu"), (int) (fWidth * 0.3), (int) (fHeight * 0.7), (int) (fWidth * 0.4),
				(int) (fHeight * 0.16), buttons);

		new JavaLabel("BackGroundUpgrades", layers.get("Upgrades"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Main Menu", layers.get("Upgrades"), (int) (fWidth * 0.3), (int) (fHeight * 0.8),
				(int) (fWidth * 0.4), (int) (fHeight * 0.2), buttons);
		new JavaLabel("Coins", layers.get("Upgrades"), (int) (fWidth * 0.3), 0, (int) (fWidth * 0.4),
				(int) (fHeight * 0.076), labels, 1, fPath, true);

		new JavaLabel("BackGroundSettings", layers.get("Settings"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Audio", layers.get("Settings"), (int) (fWidth * 0.1), (int) (fHeight * 0.1),
				(int) (fWidth * 0.4), (int) (fHeight * 0.16), buttons);
		new JavaLabel("Trail", layers.get("Settings"), (int) (fWidth * 0.1), (int) (fHeight * 0.25),
				(int) (fWidth * 0.4), (int) (fHeight * 0.16), buttons);
		new JavaLabel("Day Time", layers.get("Settings"), (int) (fWidth * 0.1), (int) (fHeight * 0.4),
				(int) (fWidth * 0.4), (int) (fHeight * 0.16), buttons);
		new JavaLabel("Resolution", layers.get("Settings"), (int) (fWidth * 0.1), (int) (fHeight * 0.55),
				(int) (fWidth * 0.4), (int) (fHeight * 0.16), buttons);

		labels.get("User not found!").setVisible(false);
		labels.get("User not found!").setForeground(Color.red);
		labels.get("Wrong Password!").setVisible(false);
		labels.get("Wrong Password!").setForeground(Color.red);
		buttons.get("Main Menu").setVisible(false);
	}

	static void setupLayers() {
		new JavaLayeredPane("Main", screen, -fWidth, 0, fWidth * 3, fHeight * 3, layers, 0);
		new JavaLayeredPane("Login", layers.get("Main"), fWidth, 0, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Menu", layers.get("Main"), fWidth, fHeight, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Upgrades", layers.get("Main"), 0, fHeight, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Settings", layers.get("Main"), 2 * fWidth, fHeight, fWidth, fHeight, layers, 0);
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
