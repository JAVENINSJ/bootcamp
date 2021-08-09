package stuntMan;

import javax.swing.*;
import inputClasses.*;
import objects.Cloud;
import objects.Coin;

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
	public static int xPlayer, yPlayer, horizontalDir, verticalDir, coinCount, frameWidth, frameHeight, time,
			groundLevel;
	public static double speedMove, playerSpeedX, playerSpeedY, xBackgr, yBackgr, minSpeed, maxSpeed;
	public static double drag, gravity, distanceX, distanceY, startSpeed, launchAngle, powerLevel, powerGain;
	public static boolean changeBG, paused, controllable, gameStarted;
	public static boolean up, left, right, down;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			objects = new HashMap<String, JavaLabel>(), //
			buttons = new HashMap<String, JavaLabel>(), //
			backgrs = new HashMap<String, JavaLabel>(); //
	static JavaLabel player;
	public static ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	public static ArrayList<Coin> coins = new ArrayList<Coin>();
	public static Set<String> backgrKeys, objectKeys;
	public static Rectangle playerHitbox;

	static DecimalFormat df = new DecimalFormat("0.00");

	public GameScreen() {
		gameTimer = new Timer(14, this);
		preGameTimer = new Timer(14, this);
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
			if (powerLevel + powerGain > 101 || powerLevel + powerGain < 0) {
				powerGain *= -1;
			}
			powerLevel += powerGain;
			labels.get("PowerSlider").setLocation((int) (labels.get("PowerSlider").getWidth() / 75 * powerLevel), 0);
			System.out.println("POWAH =" + powerLevel);
		} else { // FLYING TIME
			movePlayerIcon();
			moveBackground();
			moveObjects();
			updateLabels();
		}
	}

	public static void movePlayerIcon() {
		checkPlayerMovement();
		if (Math.abs(playerSpeedX) < maxSpeed) {
			playerSpeedX -= horizontalDir;
			xPlayer += horizontalDir * (boundCheck(xPlayer + horizontalDir, frameHeight / 2, horizontalDir));
			player.setLocation((int) (xPlayer + speedMove), (int) yPlayer);
		}
		if (Math.abs(playerSpeedY) < maxSpeed) {
			if (verticalDir == -1) {
				playerSpeedY -= verticalDir;
			}
			yPlayer += verticalDir * (boundCheck(yPlayer + verticalDir, frameWidth / 4, verticalDir));
			player.setLocation((int) xPlayer, (int) (yPlayer + speedMove));
		}
		if (yPlayer + 1 < frameHeight / 2 + frameHeight / 4 && verticalDir != -1 && controllable && gravity != 0) {
			if (playerSpeedY == 0) { // Drop player icon faster when background is stationary
				yPlayer += 9;
			}
			yPlayer += 1;
		}
		player.setLocation(xPlayer, yPlayer);
		playerHitbox = player.getBounds();
	}

	static double boundCheck(int pos, int bound, int direction) {
		if ((pos + speedMove * direction > bound + direction * bound / 2) == (direction == 1)) {
			return 0;
		}
		return speedMove;
	}

	public static void checkPlayerMovement() {
		if (!controllable) {
			if (Math.abs(playerSpeedX) < maxSpeed && Math.abs(playerSpeedY) < maxSpeed) {
				controllable = true;
			}
			if (xPlayer < frameWidth / 4 + frameWidth / 8) { // FOR LAUNCH
				xPlayer -= playerSpeedX * launchAngle;
			}
			if (yPlayer > frameHeight / 2 + -1 * frameHeight / 4) { // FOR LAUNCH
				yPlayer -= playerSpeedY * (1 - launchAngle);
			}
		}
		if (Math.abs(playerSpeedX) > maxSpeed + 5) { // + 5 is not specific
			controllable = false;
		}
		if (Math.abs(playerSpeedY) > maxSpeed + 5) { // + 5 is not specific
			controllable = false;
		}
	}

	public static void moveBackground() {
		gravity = 1 * 0.5;
		boolean setToGround = false;
		for (String backgroundKey : backgrKeys) {
			JavaLabel background = backgrs.get(backgroundKey);
			if (time > 10) {
				if (background.getIcon().equals(ground) && verticalDir != -1 && background.getY() <= groundLevel) {
					verticalDir = 0;
					gravity = 0;
					playerSpeedY = 0;
					yPlayer = groundLevel - 32;
					distanceY = 0;
					setToGround = true;
					break;
				}
			}

		}
		for (String backgroundKey : backgrKeys) {
			changeBG = false;
			JavaLabel background = backgrs.get(backgroundKey);
			int tempX = setBackgroundPos((int) Math.round(-playerSpeedX), frameWidth, background.getX());
			int tempY = setBackgroundPos((int) Math.round(-playerSpeedY), frameHeight, background.getY());
			if (setToGround) {
				tempY = groundLevel;
				if (background.getIcon().equals(stars)) {
					tempY = groundLevel - 500;
				}

			}
			if (changeBG) {
				if (tempY >= groundLevel && distanceY < 500) {
					background.setIcon(ground);
				} else {
					background.setIcon(stars);
					if (distanceY > 2500) {
						new Cloud(tempX, tempY, background.getWidth(), background.getHeight(), -distanceX, distanceY);
					}
					new Coin(tempX, tempY, 64, 64, background.getWidth(), background.getHeight(), -distanceX, distanceY,
							"CoinBase");
				}
			}
			background.setLocation(tempX, tempY);
		}
		if (Math.abs(playerSpeedX) < minSpeed) {
			playerSpeedX = 0;
		}
		drag = 0.01;
		if (distanceY < 1) {
			drag *= 10;
		}
		playerSpeedX -= playerSpeedX * drag;
		playerSpeedY -= playerSpeedY * drag;
		if (controllable && playerSpeedY - gravity > -maxSpeed) {// BACKGROUND GRAVITY
			playerSpeedY -= gravity;
		}
		distanceX -= playerSpeedX;
		distanceY += playerSpeedY;
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
			if (clouds.get(i).checkForDespawn(-distanceX, distanceY)) {
				i -= clouds.get(i).despawn();
				continue;
			}
			if (clouds.get(i).colisionCheck()) {
				clouds.get(i).speedX = 0.5 * playerSpeedX;
				clouds.get(i).speedY = 0.5 * playerSpeedY;
				playerSpeedX = 0.95 * playerSpeedX;
				playerSpeedY = 0.95 * playerSpeedY;
			}
		}
		for (int i = 0; i < coins.size(); i++) {
			coins.get(i).animate();
			if (coins.get(i).checkForDespawn(-distanceX, distanceY)) {
				i -= coins.get(i).despawn();
				continue;
			}
			if (coins.get(i).colisionCheck()) {
				coinCount++;
				i -= coins.get(i).despawn();
			}
		}
		Coin.setAnimationFrame();
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
				if (pressed && gravity != 0) {
					down = true;
				} else {
					down = false;
					verticalDir = 0;
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
			down = false;
			verticalDir = 0;
			horizontalDir = 0;
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
		if (down) {
			verticalDir = 1;
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
				playerSpeedX = -(startSpeed * launchAngle / 100 * powerLevel);
				playerSpeedY = startSpeed * (1 - launchAngle) / 100 * powerLevel;
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
		}
	}

	static void updateLabels() {
		time += 1000 / gameTimer.getDelay();
		labels.get("Distance").setText("X:" + df.format(distanceX / 100) + " m;Y:" + df.format(distanceY / 100) + " m");
		labels.get("Speed").setText("X = " + df.format(Math.abs(playerSpeedX * 3600 / 1000)) + " km/h; Y = "
				+ df.format(Math.abs(playerSpeedY * 3600 / 1000)) + " km/h");
		labels.get("Coins").setText("Coins = " + coinCount);
	}

	static void setupParameters() { // GIVES VARIABLES THEIR START VALUES
		ground = new ImageIcon(JavaLabel.fRoute + fPath + "BackgroundGrass.png");
		stars = new ImageIcon(JavaLabel.fRoute + fPath + "BackgroundStars.png");
		paused = false;
		controllable = false;
		gameStarted = false;
		frameWidth = 1000;
		frameHeight = 500;
		groundLevel = frameWidth / 4 + frameWidth / 4 / 2;
		speedMove = 5;
		xPlayer = 64;
		yPlayer = groundLevel - 32;
		distanceX = 0;
		distanceY = 16;
		playerSpeedX = 0;
		playerSpeedY = 0;
		startSpeed = 200;
		launchAngle = 0.5;
		powerLevel = 1;
		powerGain = 2;
		horizontalDir = 0;
		verticalDir = 0;
		minSpeed = 0.5;
		maxSpeed = 30;
		time = 0;
		coinCount = 0;
	}

	static void setupLabels() {
		player = new JavaLabel("Player", layers.get("gameLayer"), xPlayer, yPlayer, 32, 32, labels, 2, fPath, false);
		new JavaLabel("Cannon", layers.get("gameLayer"), xPlayer - 16, yPlayer - 32, 64, 64, objects, 3, fPath, false);
		new JavaLabel("PowerBase", layers.get("gameLayer"), xPlayer - 16, yPlayer + 32, 196, 32, objects, 1, fPath,
				false);
		new JavaLabel("PowerFrame", objects.get("PowerBase"), 0, 0, 196, 32, labels, 1, fPath, false);
		new JavaLabel("PowerSlider", objects.get("PowerBase"), 0, 0, 196, 32, labels, 1, fPath, false);
		new JavaLabel("Angle", layers.get("gameLayer"), xPlayer + 64, yPlayer - 64, 16, 16, objects, 1, fPath, false);

		new JavaLabel("Backgr1", layers.get("gameLayer"), 0, groundLevel - 500, 1000, 500, backgrs, 0, fPath, false);
		new JavaLabel("Backgr2", layers.get("gameLayer"), 1000, groundLevel - 500, 1000, 500, backgrs, 0, fPath, false);
		new JavaLabel("Backgr3", layers.get("gameLayer"), 1000, groundLevel, 1000, 500, backgrs, 0, fPath, false);
		new JavaLabel("Backgr4", layers.get("gameLayer"), 0, groundLevel, 1000, 500, backgrs, 0, fPath, false);

		new JavaLabel("Distance", layers.get("gameLayer"), frameWidth * 3 / 10, 0, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Speed", layers.get("gameLayer"), frameWidth * 3 / 10, 32, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Coins", layers.get("gameLayer"), frameWidth * 3 / 10, 64, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Back", layers.get("gameLayer"), frameWidth * 1 / 10, 0, 200, 50, buttons);
		new JavaLabel("Reset", layers.get("gameLayer"), frameWidth * 7 / 10, 0, 200, 50, buttons);
		backgrs.get("Backgr1").setIcon(stars);
		backgrs.get("Backgr2").setIcon(stars);
		backgrs.get("Backgr3").setIcon(ground);
		backgrs.get("Backgr4").setIcon(ground);
		buttons.get("Back").setVisible(false);
		buttons.get("Reset").setVisible(false);
		backgrKeys = backgrs.keySet();
		objectKeys = objects.keySet();
		labels.get("Distance").setText("\"W A S D\" FOR MOVEMENT!");
		labels.get("Speed").setText("ARROW KEYS ADJUST ANGLE!");
		labels.get("Coins").setText("\"ENTER\" LAUNCHES PLAYER!");
	}

	public static void gameClose() {
		GameScreen.gameTimer.stop();
		GameScreen.screen.removeAll();
		GameScreen.screen.dispose();
		layers.clear();
		labels.clear();
		objects.clear();
		buttons.clear();
		backgrs.clear();
		clouds.clear();
		backgrKeys.clear();
		objectKeys.clear();
	}
}
