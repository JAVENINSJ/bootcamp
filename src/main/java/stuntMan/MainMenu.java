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
	public static int[] resolution = { 750, 1000, 1500 };
	public static Settings settings;
	public static boolean audio = true, trail = true;
	public static String fPath = "//MenuAssets//";
	public static String MenuButtonPos = "Settings";
	public static JFrame screen;
	public static Timer timer;
	static JTextField enterName;
	static JPasswordField enterPassword;
	public static int fWidth = resolution[1], fHeight = fWidth / 2, coins = 7420, moveToX = -fWidth, moveToY = 0,
			resolutionNR = 1;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			buttons = new HashMap<String, JavaLabel>(), backgr = new HashMap<String, JavaLabel>();
	public static Set<String> backgrKeys;

	public MainMenu() {
		JavaLabel.setSizing(fWidth);
		screen = setupScreen(fWidth, fHeight);
		setupLayers();
		setupLabels();
		enterName = (JTextField) setupField(enterName, fWidth * 55 / 100, fHeight * 33 / 100, false);
		enterPassword = (JPasswordField) setupField(enterPassword, fWidth * 55 / 100, fHeight * 6 / 10, true);
		timer = new Timer(5, this);
		timer.start();
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
				pos += direction * (Math.abs((pos - destinationPos) / 10) + 1);
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
		String password = new String(enterPassword.getPassword());
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
				System.out.println(password);
				loginUser(name, password);
			}
		}
	}

	static void updateSettings() {
		// audio = settings.getAudio();
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
		Audio.play(JavaLabel.fRoute + "//happyRock.wav");
		//Audio.setAudio(); // TODO REMOVE THIS FOR INSTANT MUSIC
	}

	static void createUser(String username, String password) {
		// TODO DO THIS WHEN REINIS IS DONE WITH DATABASE
	}

	public static void setResolution() {
		resolutionNR += 1;
		if (resolutionNR == resolution.length) {
			resolutionNR = 0;
		}
		buttons.get("Resolution").setText(resolution[resolutionNR] + "");
	}

	public static void resetScreen() {
		timer.stop();
		buttons.clear();
		labels.clear();
		layers.clear();
		backgr.clear();
		screen.removeAll();
		screen.dispose();
		fWidth = resolution[resolutionNR];
		fHeight = fWidth / 2;
		new MainMenu();
		switchScreens("Settings");
		timer.start();
		moveToX = -fWidth;
		moveToY = -fHeight;
	}

	public Object setupField(Object field, int x, int y, boolean password) {
		if (password) {
			field = new JPasswordField();
		} else {
			field = new JTextField();
		}
		((Component) field).setBounds(x, y, (int) (fWidth * 0.3), (int) (fHeight * 0.064));
		((Component) field).setFont(new Font("Verdana", Font.BOLD, 26));
		layers.get("Login").add(((Component) field), (Integer) 20);
		return field;
	}

	static void setupLabels() {
		new JavaLabel("BackGroundLogin", layers.get("Login"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Enter As Guest", layers.get("Login"), 250, 0, 500, 75, buttons);
		new JavaLabel("Log In", layers.get("Login"), 100, 125, 400, 75, buttons);
		new JavaLabel("Create User", layers.get("Login"), 100, 300, 400, 75, buttons);
		new JavaLabel("Enter Username:", layers.get("Login"), 500, 135, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Enter Password:", layers.get("Login"), 500, 270, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Username must be atleast 3 characters long!", layers.get("Login"), 0, 215, 520, 32, labels, 1,
				fPath, true);
		new JavaLabel("Password must be atleast 3 characters long!", layers.get("Login"), 0, 250, 520, 32, labels, 1,
				fPath, true);
		new JavaLabel("User not found!", layers.get("Login"), 500, 225, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Wrong Password!", layers.get("Login"), 500, 350, 400, 32, labels, 1, fPath, true);

		new JavaLabel("BackGroundMenu", layers.get("Menu"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Play", layers.get("Menu"), 300, 20, 400, 75, buttons);
		new JavaLabel("Upgrades", layers.get("Menu"), 300, 115, 400, 75, buttons);
		new JavaLabel("Settings", layers.get("Menu"), 300, 210, 400, 75, buttons);
		new JavaLabel("Logout", layers.get("Menu"), 300, 305, 400, 75, buttons);
		new JavaLabel("Exit", layers.get("Menu"), 300, 400, 400, 75, buttons);

		new JavaLabel("BackGroundUpgrades", layers.get("Upgrades"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);

		new JavaLabel("Coins", layers.get("Upgrades"), 300, 0, 400, 38, labels, 1, fPath, true);

		new JavaLabel("BackGroundSettings", layers.get("Settings"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Audio", layers.get("Settings"), 100, 50, 300, 75, buttons);
		new JavaLabel("Trail", layers.get("Settings"), 600, 50, 300, 75, buttons);
		new JavaLabel("Theme", layers.get("Settings"), 100, 125, 300, 75, buttons);
		new JavaLabel("Resolution", layers.get("Settings"), 600, 125, 300, 75, buttons);
		new JavaLabel("Accept Settings", layers.get("Settings"), 250, 275, 500, 75, buttons);
		new JavaLabel("Main Menu", layers.get("Settings"), 300, 400, 400, 100, buttons);

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
		if (System.getProperty("os.name").startsWith("Windows")) {
			frameWidth += 16;
			frameHeight += 40;
		}
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
