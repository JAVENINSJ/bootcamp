package stuntMan;

import javax.swing.*;

import inputClasses.JavaLabel;
import inputClasses.JavaLayeredPane;
import inputClasses.KeyInputListener;

import java.awt.event.*;
import java.util.HashMap;

public class GameScreen implements ActionListener {
	public static String folderPath = "//GameAssets//";
	public static JFrame screen;
	public static Timer timer;
	public static int fWidth, fHeight, fGap = 40, xBounds = fWidth / 2 - 180, yBounds = fHeight - 276;
	public static int xPlayer, yPlayer, speedMove = 10, xBack = -fWidth, yBack = -fHeight;
	static boolean up = false, left = false, down = false, right = false, paused = false;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>();
	public static HashMap<String, JavaLabel> buttons = new HashMap<String, JavaLabel>();
	
	public static HashMap<String, String> players = new HashMap<String, String>();

	public GameScreen() {
		setupParameters();
		screen = MainMenu.setupScreen(fWidth, fHeight);
		setupLayers();
		setupLabels();
		new KeyInputListener();
		timer = new Timer(5, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution
		if (up == true) {
			yBack += speedMove;
			yPlayer -= boundCheck(yPlayer - 1, 0);
		}
		if (left == true) {
			xBack += speedMove;
			xPlayer -= boundCheck(xPlayer - 1, 0);
		}
		if (down == true) {
			yBack -= speedMove;
			yPlayer += boundCheck(yPlayer + 1, yBounds);
		}
		if (right == true) {
			xBack -= speedMove;
			xPlayer += boundCheck(xPlayer + 1, xBounds);
		}
		labels.get("Player").setLocation(xPlayer, yPlayer);
		labels.get("BackGround").setLocation(xBack, yBack);
	}

	public static void keyInput(int key, boolean pressed) {
		switch (key) {
		case KeyEvent.VK_UP: {
			up = pressed;
			break;
		}
		case KeyEvent.VK_LEFT: {
			left = pressed;
			break;
		}
		case KeyEvent.VK_DOWN: {
			down = pressed;
			break;
		}
		case KeyEvent.VK_RIGHT: {
			right = pressed;
			break;
		}
		case KeyEvent.VK_SPACE: {
			if (pressed) {
				if (paused) {
					buttons.get("Back").setVisible(false);
					paused = false;
					timer.start();
				} else {
					buttons.get("Back").setVisible(true);
					paused = true;
					timer.stop();
				}
			}
			break;
		}
		}
	}

	static int boundCheck(int pos, int bound) {
		pos -= bound;
		switch (bound) {
		case 0: {
			if (pos <= +100) {
				return 0;
			}
			return speedMove;
		}
		default:
			if (pos >= +100) {
				return 0;
			}
			return speedMove;
		}
	}

	static void setupParameters() {
		fWidth = 1000;
		fHeight = 600;
		xPlayer = 100;
		yPlayer = 100;
		xBounds = fWidth / 2 - 180;
		yBounds = fHeight - 276;
	}

	static void setupLabels() {
		new JavaLabel("Player", layers.get("gameLayer"), xPlayer, yPlayer, 32, 32, labels, 10, folderPath);
		new JavaLabel("BackGround", layers.get("gameLayer"), 0, 0, fWidth * 4, fHeight * 4, labels, 0, folderPath);
		labels.get("BackGround").setLocation(labels.get("BackGround").getWidth() / 2,
				labels.get("BackGround").getHeight() / 2);
		new JavaLabel("Back", layers.get("gameLayer"), xBounds * 9 / 10, 0, 400, 100, buttons);
		buttons.get("Back").setVisible(false);
	}

	static void setupLayers() {
		new JavaLayeredPane("gameLayer", screen, 0, 0, fWidth, fHeight, layers, 0);
	}

}
