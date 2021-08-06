package stuntMan;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Set;

import javax.swing.*;

import inputClasses.JavaLabel;
import inputClasses.JavaLayeredPane;
import passwords.Password;

public class MainMenu implements ActionListener {
	public static String fPath = "//MenuAssets//";
	public static String mainButtonPos = "Upgrades";
	public static JFrame screen;
	public static Timer timer;
	static JTextField enterName;
	static JPasswordField enterPassword;
	public static int fWidth = 1000, fHeight = 500;
	public static int coins = 7420;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			buttons = new HashMap<String, JavaLabel>(), //
			backgr = new HashMap<String, JavaLabel>(); //
	public static Set<String> backgrKeys;

	public MainMenu() {
		screen = setupScreen(fWidth + 16, fHeight + 40);// +16 and +40 because JFrame has sizing issues
		setupLayers();
		setupLabels();
		enterName = (JTextField) setupInputFields(enterName, fWidth * 11 / 20, 168, false);
		enterPassword = (JPasswordField) setupInputFields(enterPassword, fWidth * 11 / 20, 50 * 6, true);
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

	@SuppressWarnings("deprecation")
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
				createUser(name, password);
			} else {
				loginUser(name, password);
			}
		}
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
		switchScreens("Main");
	}

	static void createUser(String username, String password) {

	}

	static void setupLabels() {
		new JavaLabel("Skip", layers.get("Login"), fWidth *2/ 10, 50 * 0, 200, 50, buttons);

		new JavaLabel("BackGroundLogin", layers.get("Login"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Log In", layers.get("Login"), fWidth / 10, 50 * 2, 400, 100, buttons);
		new JavaLabel("Create User", layers.get("Login"), fWidth /10, 50 * 6, 400, 100, buttons);
		new JavaLabel("Enter Username", layers.get("Login"), fWidth * 11 / 20, 136, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Enter Password", layers.get("Login"), fWidth * 11 / 20, 268, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Username must be atleast 3 characters long!", layers.get("Login"), 0, 216, 520, 32, labels, 1,
				fPath, true);
		new JavaLabel("Password must be atleast 3 characters long!", layers.get("Login"), 0, 250, 520, 32, labels, 1,
				fPath, true);
		new JavaLabel("User not found!", layers.get("Login"), fWidth * 11 / 20, 200, 400, 32, labels, 1, fPath, true);
		new JavaLabel("Wrong Password!", layers.get("Login"), fWidth * 11 / 20, 332, 400, 32, labels, 1, fPath, true);

		new JavaLabel("BackGroundMain", layers.get("Main"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Play", layers.get("Main"), fWidth * 3 / 10, 50 * 1, 400, 80, buttons);
		new JavaLabel("Upgrades", layers.get("Main"), fWidth * 3 / 10, 50 * 3, 400, 80, buttons);
		new JavaLabel("Settings", layers.get("Main"), fWidth * 3 / 10, 50 * 5, 400, 80, buttons);
		new JavaLabel("Exit", layers.get("Main"), fWidth * 3 / 10, 50 * 7, 400, 80, buttons);

		new JavaLabel("BackGroundUpgrades", layers.get("Upgrades"), 0, 0, fWidth, fHeight, backgr, 0, fPath, false);
		new JavaLabel("Main Menu", layers.get("Upgrades"), fWidth * 3 / 10, fHeight - 100, 400, 100, buttons);
		new JavaLabel("Coins", layers.get("Upgrades"),fWidth * 3 / 10, 0, 400, 38, labels, 1, fPath, true);

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
