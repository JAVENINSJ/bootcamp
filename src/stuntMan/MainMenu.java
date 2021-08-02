package stuntMan;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import inputClasses.JavaLabel;
import inputClasses.JavaLayeredPane;

public class MainMenu implements ActionListener {
	public static String folderPath = "//MenuAssets//";
	public static JFrame screen;
	public static Timer timer;
	public static int fWidth = 1000, fHeight = 500, xBounds = fWidth / 2 - 180, yBounds = fHeight - 276;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>();
	public static HashMap<String, JavaLabel> buttons = new HashMap<String, JavaLabel>();

	public MainMenu() {
		screen = setupScreen(fWidth + 16, fHeight + 40);// +16 and +40 because JFrame has sizing issues
		setupLayers();
		setupLabels();
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution

	}

	static void setupLabels() {
		new JavaLabel("BackGround", layers.get("menuLayer"), 0, 0, fWidth, fHeight, labels, 0, folderPath);
		new JavaLabel("Play", layers.get("menuLayer"), xBounds * 9 / 10, yBounds * 8 / 10, 400, 100, buttons);
	}

	static void setupLayers() {
		new JavaLayeredPane("menuLayer", screen, 0, 0, fWidth, fHeight, layers, 0);
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
