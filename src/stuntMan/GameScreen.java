package stuntMan;

import javax.swing.*;

import inputClasses.JavaLabel;
import inputClasses.JavaLayeredPane;
import inputClasses.KeyInputListener;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class GameScreen implements ActionListener {
	public static String folderPath = "//GameAssets//";
	public static JFrame screen;
	public static Timer timer;
	public static int fWidth, fHeight, fGap = 40, xBounds = fWidth / 2 - 180, yBounds = fHeight - 276;
	public static int xPlayer, yPlayer, horizontalDir, verticalDir;
	public static double speedMove = 5, playerSpeedX, playerSpeedY, xBackgr, yBackgr, minSpeed, maxSpeed;
	public static double windResistance;
	static boolean paused = false;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>();
	public static HashMap<String, JavaLabel> buttons = new HashMap<String, JavaLabel>();

	static DecimalFormat df = new DecimalFormat("0.00");

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
		movePlayer();
		moveBackground();
	}

	public static void moveBackground() {
		if (Math.abs(playerSpeedX) < minSpeed) {
			playerSpeedX = 0;
		}
		if (Math.abs(playerSpeedY) < minSpeed) {
			playerSpeedY = 0;
		}
		playerSpeedX += (-playerSpeedX * windResistance);
		playerSpeedY += (-playerSpeedY * windResistance);
		yBackgr += playerSpeedY;
		xBackgr += playerSpeedX;
		labels.get("BackGround").setLocation((int) Math.round(xBackgr), (int) Math.round(yBackgr));
		// FOR TESTING PURPOSES {
		int range = 100;
		if (xBackgr - xPlayer > range) {
			xBackgr = xPlayer - range;
		}
		if (yBackgr - yPlayer > range) {
			yBackgr = yPlayer - range;
		}
		if (xBackgr - xPlayer < -range) {
			xBackgr = xPlayer + range;
		}
		if (yBackgr - yPlayer < -range) {
			yBackgr = yPlayer + range;
		}
		// }
	}

	public static void movePlayer() {
		yPlayer = moveCheck(yPlayer, maxSpeed, false, verticalDir);
		xPlayer = moveCheck(xPlayer, maxSpeed, false, horizontalDir);
		playerSpeedY -= 1 * verticalDir;
		playerSpeedX -= 1 * horizontalDir;
		labels.get("Player").setLocation(xPlayer, yPlayer);
	}

	static int moveCheck(int pos, double speed, boolean check, int direction) {
		return pos += direction * (boundCheck(pos + direction, (int) (-speed * direction), check));
	}

	static double boundCheck(int pos, int bound, boolean check) {
		pos -= bound;
		switch (bound) {
		case 0: {
			if ((pos <= +100) == check) {
				return 0;
			}
			return speedMove;
		}
		default:
			if ((pos >= +100) == check) {
				return 0;
			}
			return speedMove;
		}
	}

	public static void keyInput(int key, boolean pressed) {
		switch (key) {
		case KeyEvent.VK_UP: {
			if (pressed) {
				verticalDir = -1;
			} else {
				verticalDir = 0;
			}
			break;
		}
		case KeyEvent.VK_LEFT: {
			if (pressed) {
				horizontalDir = -1;
			} else {
				horizontalDir = 0;
			}
			break;
		}
		case KeyEvent.VK_DOWN: {
			if (pressed) {
				verticalDir = 1;
			} else {
				verticalDir = 0;
			}
			break;
		}
		case KeyEvent.VK_RIGHT: {
			if (pressed) {
				horizontalDir = 1;
			} else {
				horizontalDir = 0;
			}
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

	static void setupParameters() {
		fWidth = 1000;
		fHeight = 600;
		xPlayer = 200;
		yPlayer = 200;
		xBounds = fWidth / 2 - 180;
		yBounds = fHeight - 276;
		playerSpeedX = 10;
		playerSpeedY = -10;
		horizontalDir = 0;
		verticalDir = 0;
		windResistance = 0.01;
		minSpeed = 0.5;
		maxSpeed = 20;
	}

	static void setupLabels() {
		new JavaLabel("Player", layers.get("gameLayer"), xPlayer, yPlayer, 32, 32, labels, 10, folderPath);
		new JavaLabel("BackGround", layers.get("gameLayer"), xPlayer, yPlayer, 16, 16, labels, 0, folderPath);
		new JavaLabel("Back", layers.get("gameLayer"), xBounds * 9 / 10, 0, 400, 100, buttons);
		buttons.get("Back").setVisible(false);
	}

	static void setupLayers() {
		new JavaLayeredPane("gameLayer", screen, 0, 0, fWidth, fHeight, layers, 0);
	}

}
