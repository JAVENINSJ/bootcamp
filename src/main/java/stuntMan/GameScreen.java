package stuntMan;

import javax.swing.*;

import inputClasses.*;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;

public class GameScreen implements ActionListener {
	public static String filePath = "//GameAssets//";
	public static JFrame screen;
	public static ImageIcon ground, stars;
	public static Timer gameTimer, preGameTimer;
	public static int xPlayer, yPlayer, horizontalDir, verticalDir;
	public static int frameWidth, frameHeight, xBounds = frameWidth / 2 - 180, yBounds = frameHeight - 276;
	public static double speedMove, playerSpeedX, playerSpeedY, xBackgr, yBackgr, minSpeed, maxSpeed;
	public static double drag, distanceX, distanceY, startSpeed, launchAngle;
	static boolean changeBG, paused, controllable, gameStarted;
	static boolean up = false, down = false, left = false, right = false;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			objects = new HashMap<String, JavaLabel>(), //
			buttons = new HashMap<String, JavaLabel>(), //
			backgrounds = new HashMap<String, JavaLabel>(); //
	public static Set<String> backgrKeys, objectKeys;

	static DecimalFormat df = new DecimalFormat("0.00");

	public GameScreen() {
		setupParameters();
		screen = MainMenu.setupScreen(frameWidth + 16, frameHeight + 40);
		new JavaLayeredPane("gameLayer", screen, 0, 0, frameWidth, frameHeight, layers, 0);
		setupLabels();
		new KeyInputListener();
		gameTimer = new Timer(5, this);
		preGameTimer = new Timer(5, this);
		preGameTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution
		if (e.getSource() == preGameTimer) { // CANNON TIME
			//
		} else { // FLYING TIME
			if (!controllable) {
				if (Math.abs(playerSpeedX) < maxSpeed && Math.abs(playerSpeedY) < maxSpeed) {
					controllable = true;
				}
				if (xPlayer < frameWidth / 4 + frameWidth / 8) {
					xPlayer -= playerSpeedX * launchAngle;
				}
				if (yPlayer > frameHeight / 2 + -1 * frameHeight / 4) {
					System.out.println("YP = " + yPlayer + " > " + (frameHeight / 2 + -1 * frameHeight / 4));
					yPlayer -= playerSpeedY * (1 - launchAngle);
				}
			}
			if (Math.abs(playerSpeedX) > maxSpeed + 5) {
				controllable = false;
			}
			if (Math.abs(playerSpeedY) > maxSpeed + 5) {
				controllable = false;
			}
			movePlayer();
			moveBackground();
			moveObjects();
			labels.get("Distance").setText("X = " + (int) distanceX + "; Y = " + (int) distanceY);
		}

	}

	public static void movePlayer() {
		if (Math.abs(playerSpeedX) < maxSpeed) {
			playerSpeedX -= horizontalDir;
			xPlayer += horizontalDir * (boundCheck(xPlayer + horizontalDir, frameHeight / 2, -horizontalDir));
			labels.get("Player").setLocation((int) (xPlayer + speedMove), (int) yPlayer);
		}
		if (Math.abs(playerSpeedY) < maxSpeed) {
			playerSpeedY -= verticalDir;
			yPlayer += verticalDir * (boundCheck(yPlayer + verticalDir, frameWidth / 4, -verticalDir));
			labels.get("Player").setLocation((int) xPlayer, (int) (yPlayer + speedMove));
		}
		if (yPlayer + 1 < frameHeight / 2 + frameHeight / 4 && verticalDir != -1 && controllable) {
			yPlayer += 1;
		}
		labels.get("Player").setLocation(xPlayer, yPlayer);
	}

	static double boundCheck(int pos, int bound, int direction) {
		if ((pos + speedMove * direction > bound + (-direction) * bound / 2) == (direction == -1)) {
			return 0;
		}
		return speedMove;
	}

	
			
	public static void moveBackground() {
		if (Math.abs(playerSpeedX) < minSpeed) {
			playerSpeedX = 0;
		}
		playerSpeedX += -playerSpeedX * drag;
		playerSpeedY += (-playerSpeedY * drag);
		if (controllable) {
			if (playerSpeedY - 0.5 > -maxSpeed && distanceY + playerSpeedY > 0) {
				playerSpeedY -= 0.5;
			} else {
				playerSpeedY = 0;
			}
		}
		distanceY += playerSpeedY;
		distanceX -= playerSpeedX;
		for (String backgroundKey : backgrKeys) {
			changeBG = false;
			JavaLabel background = backgrounds.get(backgroundKey);
			int tempX = setBackgroundPos((int) Math.round(-playerSpeedX), frameWidth, background.getX());
			int tempY = setBackgroundPos((int) Math.round(-playerSpeedY), frameHeight, background.getY());
			if (changeBG) {
				if (tempY >= frameHeight - 50 && distanceY < 100) {
					background.setIcon(ground);
				} else {
					background.setIcon(stars);
				}
			}
			background.setLocation(tempX, tempY);
		}
	}

	static int setBackgroundPos(int speed, int bound, int pos) {
		int direction = -1;
		boolean check = false;
		if (pos + speed > pos) {
			direction = 1;
			check = true;
		}
		pos -= speed;
		if ((pos < (-bound * direction)) == check) {
			pos = pos + (-bound * direction) + bound * 3 * direction;
			changeBG = true;
		}
		return pos;
	}

	public static void moveObjects() {
		for (String objectKey : objectKeys) {
			JavaLabel object = objects.get(objectKey);
			object.setLocation(object.startX + (int) -distanceX, object.startY + (int) distanceY);
		}
	}

	public static void keyInput(int key, boolean pressed) {
		if (controllable) {
			if (key == KeyEvent.VK_W) {
				if (pressed) {
					up = true;
				} else {
					verticalDir = 0;
					up = false;
				}
			}
			if (key == KeyEvent.VK_A) {
				if (pressed) {
					left = true;
				} else {
					horizontalDir = 0;
					left = false;
				}
			}
			if (key == KeyEvent.VK_S) {
				if (pressed) {
					down = true;
				} else {
					verticalDir = 0;
					down = false;
				}
			}
			if (key == KeyEvent.VK_D) {
				if (pressed) {
					right = true;
				} else {
					right = false;
					horizontalDir = 0;
				}
			}
		} else {
			up = false;
			left = false;
			down = false;
			right = false;
			verticalDir = 0;
			horizontalDir = 0;
		}
		if (key == KeyEvent.VK_ESCAPE && pressed) {
			Timer timer = gameTimer;
			if (!gameStarted) {
				timer = preGameTimer;
			}
			if (paused) {
				timer.start();
			} else {
				timer.stop();
			}
			buttons.get("Back").setVisible(!paused);
			paused = !paused;
		}
		if (preGameTimer.isRunning()) {
			if (key == KeyEvent.VK_RIGHT && pressed && launchAngle + 0.2 < 1) {
				launchAngle += 0.2;
			}
			if (key == KeyEvent.VK_LEFT && pressed && launchAngle - 0.2 > 0) {
				launchAngle -= 0.2;
			}
			objects.get("Indicator").setLocation(xPlayer + (int) (128 * launchAngle),
					yPlayer - (int) (128 * (1 - launchAngle)));
			System.out.println("X speed = " + startSpeed * launchAngle);
			System.out.println("Y speed = " + startSpeed * (1 - launchAngle));
			if (key == KeyEvent.VK_SPACE) {
				playerSpeedX = -(startSpeed * launchAngle);
				playerSpeedY = startSpeed * (1 - launchAngle);
				preGameTimer.stop();
				gameTimer.start();
				gameStarted = true;
			}
		}
		if (up) {
			verticalDir = -1;
		}
		if (down) {
			verticalDir = 1;
		}
		if (right) {
			horizontalDir = 1;
		}
		if (left) {
			horizontalDir = -1;
		}
	}

	static void setupParameters() { // GIVES VARIABLES THEIR START VALUES
		ground = new ImageIcon(JavaLabel.fileRoute + filePath + "BackgroundGrass.png");
		stars = new ImageIcon(JavaLabel.fileRoute + filePath + "BackgroundStars.png");
		paused = false;
		controllable = false;
		gameStarted = false;
		frameWidth = 1000;
		frameHeight = 500;
		speedMove = 5;
		xPlayer = 64;
		yPlayer = frameHeight - 96;
		xBounds = frameWidth / 2 - 180;
		yBounds = frameHeight - 276;
		distanceX = 0;
		distanceY = 0;
		playerSpeedX = 0;
		playerSpeedY = 0;
		startSpeed = 00;
		launchAngle = 0.5;
		horizontalDir = 0;
		verticalDir = 0;
		drag = 0.01;
		minSpeed = 0.5;
		maxSpeed = 30;
	}

	static void setupLabels() {
		new JavaLabel("Player", layers.get("gameLayer"), xPlayer, yPlayer, 32, 32, labels, 5, filePath);
		new JavaLabel("Cannon", layers.get("gameLayer"), xPlayer - 16, yPlayer - 16, 64, 64, objects, 4, filePath);
		new JavaLabel("Indicator", layers.get("gameLayer"), xPlayer + 64, yPlayer - 64, 32, 32, objects, 5, filePath);
		new JavaLabel("Backgr1", layers.get("gameLayer"), 0, frameHeight - 548, 1000, 500, backgrounds, 0, filePath);
		new JavaLabel("Backgr2", layers.get("gameLayer"), 1000, frameHeight - 548, 1000, 500, backgrounds, 0, filePath);
		new JavaLabel("Backgr3", layers.get("gameLayer"), 1000, frameHeight - 48, 1000, 500, backgrounds, 0, filePath);
		new JavaLabel("Backgr4", layers.get("gameLayer"), 0, frameHeight - 48, 1000, 500, backgrounds, 0, filePath);
		new JavaLabel("Distance", layers.get("gameLayer"), xBounds * 9 / 10, 0, 400, 50, labels, 10, filePath);
		new JavaLabel("Back", layers.get("gameLayer"), xBounds * 9 / 10, 0, 400, 100, buttons);
		backgrounds.get("Backgr1").setIcon(stars);
		backgrounds.get("Backgr2").setIcon(stars);
		backgrounds.get("Backgr3").setIcon(ground);
		backgrounds.get("Backgr4").setIcon(ground);
		buttons.get("Back").setVisible(false);
		backgrKeys = backgrounds.keySet();
		objectKeys = objects.keySet();
	}

	public static void gameClose() {
		GameScreen.gameTimer.stop();
		GameScreen.screen.removeAll();
		GameScreen.screen.dispose();
	}
}
