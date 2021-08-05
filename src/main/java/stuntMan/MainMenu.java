package stuntMan;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Set;

import javax.swing.*;

import inputClasses.JavaLabel;
import inputClasses.JavaLayeredPane;

public class MainMenu implements ActionListener {
	public static String folderPath = "//MenuAssets//";
	public static String mainButtonPos = "Upgrades";
	public static JFrame screen;
	public static Timer timer;
	public static int fWidth = 1000, fHeight = 500, xBounds = fWidth / 2 - 180, yBounds = fHeight - 276;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> buttons = new HashMap<String, JavaLabel>();
	public static HashMap<String, JavaLabel> backgrounds = new HashMap<String, JavaLabel>();
	public static Set<String> backgrKeys;

	public MainMenu() {
		screen = setupScreen(fWidth, fHeight);// +16 and +40 because JFrame has sizing issues
		setupLayers();
		setupLabels();
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

	static void setupLabels() {
		new JavaLabel("BackGroundMain", layers.get("Main"), 0, 0, fWidth, fHeight, backgrounds, 0, folderPath);
		new JavaLabel("BackGroundUpgrades", layers.get("Upgrades"), 0, 0, fWidth, fHeight, backgrounds, 0, folderPath);
		new JavaLabel("BackGroundSettings", layers.get("Settings"), 0, 0, fWidth, fHeight, backgrounds, 0, folderPath);
		new JavaLabel("Play", layers.get("Main"), xBounds * 9 / 10, 50 * 1, 400, 100, buttons);
		new JavaLabel("Upgrades", layers.get("Main"), xBounds * 9 / 10, 50 * 3, 400, 100, buttons);
		new JavaLabel("Settings", layers.get("Main"), xBounds * 9 / 10, 50 * 5, 400, 100, buttons);
		new JavaLabel("Main Menu", layers.get("Upgrades"), xBounds * 9 / 10, fHeight - 100, 400, 100, buttons);
		buttons.get("Main Menu").setVisible(false);
		new JavaLabel("Exit", layers.get("Main"), xBounds * 9 / 10, 50 * 7, 400, 100, buttons);
	}
	

	static void setupLayers() {
		new JavaLayeredPane("Main", screen, 0, 0, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Upgrades", screen, 0, -fHeight, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Settings", screen, 0, -fHeight, fWidth, fHeight, layers, 0);
		backgrKeys = layers.keySet();
	}

	static JFrame setupScreen(int frameWidth, int frameHeight) {// FUN FACT : JFRAME WIDTH IS SMALLER BY 16 PIXELS IN
		// WIDTH AND 40 PIXELS IN HEIGHT THAN THE GIVEN SIZE VALUES
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
