package stuntMan;

import javax.swing.*;
import inputClasses.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GameScreen implements ActionListener {
	public static String fPath = "//GameAssets//";
	public static JFrame screen;
	public static ImageIcon ground, stars;
	public static Timer gameTimer, preGameTimer;
	public static int xPlayer, yPlayer, horizontalDir, verticalDir;
	public static int frameWidth, frameHeight, xBounds = frameWidth / 2 - 180, yBounds = frameHeight - 276;
	public static double speedMove, playerSpeedX, playerSpeedY, xBackgr, yBackgr, minSpeed, maxSpeed;
	public static double drag, distanceX, distanceY, startSpeed, launchAngle;
	public static boolean changeBG, paused, controllable, gameStarted;
	public static boolean up = false, left = false, right = false;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			objects = new HashMap<String, JavaLabel>(), //
			buttons = new HashMap<String, JavaLabel>(), //
			backgr = new HashMap<String, JavaLabel>(); //
	static ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	public static Set<String> backgrKeys, objectKeys;
	public static Rectangle playerHitbox;

	static DecimalFormat df = new DecimalFormat("0.00");
	
	public GameScreen() {
		gameTimer = new Timer(5, this);
		preGameTimer = new Timer(5, this);
	}

	public static void startGame() {
		setupParameters();
		screen = MainMenu.setupScreen(frameWidth + 16, frameHeight + 40); // + 16 and + 40 because of JFrame sizing
		new JavaLayeredPane("gameLayer", screen, 0, 0, frameWidth, frameHeight, layers, 0);
		setupLabels();
		new KeyInputListener();
		preGameTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution
		if (e.getSource() == preGameTimer) { // CANNON TIME
			//
		} else { // FLYING TIME
			checkPlayerMovement();
			movePlayer();
			moveBackground();
			moveObjects();
			
			Graphics2D g = (Graphics2D) labels.get("Player").getGraphics();
			if(playerSpeedX > 0) {
				System.out.println(1);
				g.rotate(Math.atan2(playerSpeedY, playerSpeedX));
			}else if(playerSpeedX < 0) {
				System.out.println(2);
				g.rotate(Math.atan2(playerSpeedY, playerSpeedX) + Math.PI);
			}else {
				if(playerSpeedY < 0) {
					System.out.println(3);
					g.rotate(Math.PI/2);
				}
				System.out.println(4);
				g.rotate(Math.PI/2);
			}
			
			labels.get("Distance").setText("X = " + (int) distanceX + "; Y = " + (int) distanceY);
			labels.get("Player").setLocation(xPlayer, yPlayer);
		}
	}

	public static void checkPlayerMovement() {
		if (!controllable) {
			if (Math.abs(playerSpeedX) < maxSpeed && Math.abs(playerSpeedY) < maxSpeed) {
				controllable = true;
			}
			if (xPlayer < frameWidth / 4 + frameWidth / 8) {
				xPlayer -= playerSpeedX * launchAngle;
			}
			if (yPlayer > frameHeight / 2 + -1 * frameHeight / 4) {
				yPlayer -= playerSpeedY * (1 - launchAngle);
			}
		}
		if (Math.abs(playerSpeedX) > maxSpeed + 5) {
			controllable = false;
		}
		if (Math.abs(playerSpeedY) > maxSpeed + 5) {
			controllable = false;
		}
	}

	public static void movePlayer() {
		collisionCheck();
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
		if (yPlayer + 1 < frameHeight / 2 + frameHeight / 4 && verticalDir != -1 && controllable) { // PLAYER GRAVITI
			if (playerSpeedY == 0) {
				yPlayer += 9;
			}
			yPlayer += 1;
		}
		
	}

	static double boundCheck(int pos, int bound, int direction) {
		if ((pos + speedMove * direction > bound + (-direction) * bound / 2) == (direction == -1)) {
			return 0;
		}
		return speedMove;
	}

	static void collisionCheck() {
		playerHitbox = labels.get("Player").getBounds();
		for (Cloud cloud : clouds) {
			cloud.hitbox = cloud.getBounds();
			if (playerHitbox.intersects(cloud.hitbox)) {
				cloud.speedX = 0.5 * playerSpeedX;
				cloud.speedY = 0.5 * playerSpeedY;
				playerSpeedX = 0.95 * playerSpeedX;
				playerSpeedY = 0.95 * playerSpeedY;
			}
		}
	}

	public static void moveBackground() {
		if (Math.abs(playerSpeedX) < minSpeed) {
			playerSpeedX = 0;
		}
		playerSpeedX -= playerSpeedX * drag;
		playerSpeedY -= playerSpeedY * drag;
		if (controllable) {
			if (playerSpeedY - 0.5 > -maxSpeed) { // BACKGROUND GRAVITY
				playerSpeedY -= 0.5;
			}
			if (distanceY + playerSpeedY <= 0) {
				playerSpeedY = 0;
			}
		}
		distanceY += playerSpeedY;
		distanceX -= playerSpeedX;
		for (String backgroundKey : backgrKeys) {
			changeBG = false;
			JavaLabel background = backgr.get(backgroundKey);
			int tempX = setBackgroundPos((int) Math.round(-playerSpeedX), frameWidth, background.getX());
			int tempY = setBackgroundPos((int) Math.round(-playerSpeedY), frameHeight, background.getY());
			if (changeBG) {
				if (tempY >= frameHeight - 50 && distanceY < 100) {
					background.setIcon(ground);
				} else {
					background.setIcon(stars);
					new Cloud(tempX, tempY, background.getWidth(), background.getHeight(), -distanceX, distanceY);
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
		for (int i = 0; i < clouds.size(); i++) {
			clouds.get(i).move();
			clouds.get(i).setPosition(-distanceX, distanceY);
			if (clouds.get(i).getX() < -2 * frameWidth || clouds.get(i).getX() > 4 * frameWidth
					|| clouds.get(i).getY() < -2 * frameHeight || clouds.get(i).getY() > 4 * frameHeight) {
				layers.get("gameLayer").remove(clouds.get(i));
				clouds.remove(clouds.get(i));
				i--;
			}
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
			buttons.get("Reset").setVisible(!paused);
			paused = !paused;
		} else if (key == KeyEvent.VK_R && pressed) {
			GameScreen.gameClose();
			GameScreen.startGame();
		}
		if (preGameTimer.isRunning()) {
			if (key == KeyEvent.VK_SPACE) {
				playerSpeedX = -(startSpeed * launchAngle);
				playerSpeedY = startSpeed * (1 - launchAngle);
				preGameTimer.stop();
				gameTimer.start();
				gameStarted = true;
			} else if (key == KeyEvent.VK_RIGHT && pressed && launchAngle + 0.2 < 1) {
				launchAngle += 0.2;
			} else if (key == KeyEvent.VK_LEFT && pressed && launchAngle - 0.2 > 0) {
				launchAngle -= 0.2;
			}
			objects.get("Angle").setLocation(xPlayer + (int) (128 * launchAngle),
					yPlayer - (int) (128 * (1 - launchAngle)));
			System.out.println("X speed = " + startSpeed * launchAngle);
			System.out.println("Y speed = " + startSpeed * (1 - launchAngle));
			
		}
		if (up) {
			verticalDir = -1;
		}
		if (right) {
			horizontalDir = 1;
		}
		if (left) {
			horizontalDir = -1;
		}
	}

	static void setupParameters() { // GIVES VARIABLES THEIR START VALUES
		ground = new ImageIcon(JavaLabel.fRoute + fPath + "BackgroundGrass.png");
		stars = new ImageIcon(JavaLabel.fRoute + fPath + "BackgroundStars.png");
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
		startSpeed = 200;
		launchAngle = 0.5;
		horizontalDir = 0;
		verticalDir = 0;
		drag = 0.01;
		minSpeed = 0.5;
		maxSpeed = 30;
	}

	static void setupLabels() {
		new JavaLabel("Player", layers.get("gameLayer"), xPlayer, yPlayer, 32, 32, labels, 5, fPath, false);
		new JavaLabel("Cannon", layers.get("gameLayer"), xPlayer - 16, yPlayer - 16, 64, 64, objects, 4, fPath, false);
		new JavaLabel("Angle", layers.get("gameLayer"), xPlayer + 64, yPlayer - 64, 16, 16, objects, 5, fPath, false);

		new JavaLabel("Backgr1", layers.get("gameLayer"), 0, frameHeight - 548, 1000, 500, backgr, 0, fPath, false);
		new JavaLabel("Backgr2", layers.get("gameLayer"), 1000, frameHeight - 548, 1000, 500, backgr, 0, fPath, false);
		new JavaLabel("Backgr3", layers.get("gameLayer"), 1000, frameHeight - 48, 1000, 500, backgr, 0, fPath, false);
		new JavaLabel("Backgr4", layers.get("gameLayer"), 0, frameHeight - 48, 1000, 500, backgr, 0, fPath, false);

		new JavaLabel("Distance", layers.get("gameLayer"), xBounds * 9 / 10, 0, 400, 50, labels, 10, fPath, true);
		new JavaLabel("Back", layers.get("gameLayer"), xBounds * 18 / 10, 0, 200, 50, buttons);
		new JavaLabel("Reset", layers.get("gameLayer"), xBounds * 6 / 10, 0, 200, 50, buttons);
		backgr.get("Backgr1").setIcon(stars);
		backgr.get("Backgr2").setIcon(stars);
		backgr.get("Backgr3").setIcon(ground);
		backgr.get("Backgr4").setIcon(ground);
		buttons.get("Back").setVisible(false);
		buttons.get("Reset").setVisible(false);
		backgrKeys = backgr.keySet();
		objectKeys = objects.keySet();
	}

	public static void gameClose() {
		GameScreen.gameTimer.stop();
		GameScreen.screen.removeAll();
		GameScreen.screen.dispose();
		layers.clear();
		labels.clear();
		objects.clear();
		buttons.clear();
		backgr.clear();
		clouds.clear();
		backgrKeys.clear();
		objectKeys.clear();
	}
}
