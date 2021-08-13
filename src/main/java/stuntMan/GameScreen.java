package stuntMan;

import javax.swing.*;
import ioClasses.*;
import objects.Background;
import objects.Cloud;
import objects.Coin;
import objects.JavaObject;
import objects.PowerUp;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class GameScreen implements ActionListener {
	public static String fPath = "//GameAssets//";
	public static JFrame screen;
	public static Timer gameTimer, preGameTimer;
	public static int playerX, playerY, playerStartX, playerStartY, horizontalDir, verticalDir, coinCount, time,
			groundLVL, powerUpTime, xMarkerNR, yMarkerNR, markerSpacing, speedBoost;
	public static int fWidth, fHeight, coinWidth, coinHeight, playerWidth, playerHeight, moveToX;
	public static double movingSpeed, playerSpeedX, playerSpeedY, xBackgr, yBackgr, minSpeed, maxSpeed, jetpackPower;
	public static double dragY, dragX, gravitySpeed, distanceX, distanceY, startSpeed, launchAngle, powerLevel,
			powerGain;
	public static boolean paused, controllable, gameStarted, gameFinished, sandbox, gravity;
	public static boolean up, left, right, down, jetpack, trail, cloudContact, groundContact;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, Background> backgrounds = new HashMap<String, Background>();
	public static HashMap<String, JavaLabel> buttons = new HashMap<String, JavaLabel>(),
			labels = new HashMap<String, JavaLabel>(), //
			objects = new HashMap<String, JavaLabel>(); //
	public static HashMap<String, ImageIcon> playerIcons = new HashMap<String, ImageIcon>();
	public static JavaLabel player;
	static Random random = new Random();
	public static ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	public static ArrayList<Coin> coins = new ArrayList<Coin>();
	public static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	public static ArrayList<JavaObject> trails = new ArrayList<JavaObject>();
	public static Set<String> backgrKeys, objectKeys;
	public static Rectangle playerHitbox;
	public static JavaObject dMarkerX, dMarkerY;

	static DecimalFormat df = new DecimalFormat("0.00");

	public GameScreen() {
		gameTimer = new Timer(14, this);
		preGameTimer = new Timer(14, this);
	}

	public static void startGame() {
		setupParameters();
		screen = MainMenu.setupScreen(fWidth, fHeight);
		new JavaLayeredPane("gameLayer", screen, 0, 0, fWidth, fHeight, layers, 0);
		new JavaLayeredPane("Sidebar", layers.get("gameLayer"), fWidth * 754 / 1000, 0, fWidth * 250 / 1000, fHeight,
				layers, 20);
		setupLabels();
		dMarkerX = new JavaObject(-playerStartX + markerSpacing, 0, 32, 500, 0, 0, -markerSpacing * 10, 0, "markerX",
				2);
		dMarkerX.setPosition(-playerStartX + markerSpacing, 0);
		dMarkerY = new JavaObject(0, +playerStartY - markerSpacing, 1000, 32, 0, 0, 0, markerSpacing * 10, "markerY",
				2);
		dMarkerY.setPosition(0, playerStartY - markerSpacing);
		new KeyInputListener();
		preGameTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Timer Execution
		if (e.getSource() == preGameTimer) { // CANNON TIME
			if (powerLevel + powerGain > 101 || powerLevel + powerGain < 20) {
				powerGain *= -1;
			}
			powerLevel += powerGain;
			labels.get("PowerSlider").setLocation((int) (labels.get("PowerSlider").getWidth() / 75 * powerLevel), 0);
		} else { // FLYING TIME
			movePlayerIcon();
			moveObjects();
			updateLabels();
			if (powerUpTime > 0) {
				powerUpTime -= 1;
			}
			if (trail) {
				generateTrail();
			}
		}
		if (gameStarted) {
			int tempX = layers.get("Sidebar").getX();
			if (tempX != moveToX) {
				layers.get("Sidebar").setLocation(tempX + 2 * ((moveToX - tempX) / Math.abs(moveToX - tempX)), 0);
			}
		}
	}

	public static void movePlayerIcon() {
		checkPlayerMovement();
		if (Math.abs(playerSpeedX) < minSpeed) {
			playerSpeedX = 0;
		}
		double tempDragX = dragX;
		if (distanceY < 16) {
			tempDragX *= 10;
		}
		double tempDragY = dragY;
		if (groundContact) {
			tempDragY = 0;
		}
		playerSpeedX -= playerSpeedX * tempDragX;
		playerSpeedY -= playerSpeedY * tempDragY;
		System.out.println(groundContact);
		if (controllable && playerSpeedY - gravitySpeed > -maxSpeed && distanceY > 0 && !groundContact) {
			playerSpeedY -= gravitySpeed;// BACKGROUND GRAVITY
		}
		distanceX -= playerSpeedX;
		distanceY += playerSpeedY;
		gravitySpeed = 1 * 0.5;
		if (!gravity) {
			gravitySpeed = 0;
		}
		if (Math.abs(playerSpeedX) < maxSpeed) {
			playerSpeedX -= horizontalDir / (5 / speedBoost) + horizontalDir * Math.abs(playerSpeedY / (dragY * 1000));
			playerX += horizontalDir * (boundCheck(playerX + horizontalDir, fHeight / 2, horizontalDir));
		}
		if (Math.abs(playerSpeedY) < maxSpeed) {
			if (verticalDir == -1 || sandbox) {
				playerSpeedY -= verticalDir / (5 / speedBoost);
			}
			playerY += verticalDir * (boundCheck(playerY + verticalDir, fWidth / 4, verticalDir));
		}
		if (playerY + 1 < fHeight / 2 + fHeight / 4 && verticalDir != -1 && controllable && gravitySpeed != 0
				&& !groundContact) {
			playerY += 1; // GRAVITY
		}
		if (groundContact) {
			playerSpeedY = 1;
			player.setLocation(playerX, playerY - 1);
		}
		System.out.println(playerSpeedY);
		player.setLocation(playerX, playerY);
		playerHitbox = player.getBounds();
		setPlayerIcon();
	}

	static double boundCheck(int pos, int bound, int direction) {
		if ((pos + movingSpeed * direction > bound + direction * bound / 2) == (direction == 1) || cloudContact) {
			return 0;
		}
		return movingSpeed * speedBoost;
	}

	public static void checkPlayerMovement() {
		if (!controllable) {
			if (Math.abs(playerSpeedX) < maxSpeed && Math.abs(playerSpeedY) < maxSpeed) {
				controllable = true;
			}
			if (playerX < fWidth / 6) { // MOVES PLAYER TO RIGHT WALL
				playerX -= playerSpeedX * launchAngle;
			}
			if (playerY > fHeight / 3) { // MOVES PLAYER TO TOP WALL
				playerY -= playerSpeedY * (1 - launchAngle);
			}
		}
		if (Math.abs(playerSpeedX) > maxSpeed + 5) { // + 5 is not specific
			controllable = false;
		}
		if (Math.abs(playerSpeedY) > maxSpeed + 5) { // + 5 is not specific
			controllable = false;
		}
		if (groundContact) {
			if (sandbox) {
				if (verticalDir == -1) {
					playerSpeedY = 5;
				} else {
					playerSpeedY = 1;
					if (verticalDir == 1) {
						verticalDir = 0;
					}
				}
			} else {
				gameFinished = true;
			}
		}
	}

	public static void setPlayerIcon() { // X negative, Y positive
		String YDir = "";
		String XDir = "";
		if (playerSpeedX < -5) {
			XDir = "R";
		} else if (playerSpeedX > 5) {
			XDir = "L";
		}
		if (playerSpeedY < -9) {
			YDir = "D";
		} else if (playerSpeedY > 9 || XDir.isBlank()) {
			YDir = "U";
		}
		player.setIcon(playerIcons.get(XDir + YDir));
	}

	public static void moveObjects() {
		groundContact = false;
		int contact = 0;
		for (String backgrKey : backgrKeys) {
			contact += backgrounds.get(backgrKey).pizgets(-distanceX, distanceY, playerSpeedX, playerSpeedY);
		}
		if (contact > 0) {
			groundContact = true;
		}
		for (String objectKey : objectKeys) {
			JavaLabel object = objects.get(objectKey);
			object.setLocation(object.startX + (int) -distanceX, object.startY + (int) distanceY);
		}
		cloudContact = false;
		for (int i = 0; i < clouds.size(); i++) {
			if (clouds.get(i).checkForDespawn(-distanceX, distanceY)) {
				i -= clouds.get(i).despawn();
				continue;
			}
			if (clouds.get(i).colisionCheck()) {
				clouds.get(i).speedX = 0.5 * playerSpeedX;
				clouds.get(i).speedY = 0.5 * playerSpeedY;
				playerSpeedX = (0.99 - dragX) * playerSpeedX;
				playerSpeedY = (0.99 - dragY) * playerSpeedY;
				cloudContact = true;
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
		for (int i = 0; i < powerUps.size(); i++) {
			if (powerUps.get(i).checkForDespawn(-distanceX, distanceY)) {
				i -= powerUps.get(i).despawn();
				continue;
			}
			if (powerUps.get(i).colisionCheck()) {
				GameScreen.convertCloudsToCoins();
				powerUpTime = 1337;
				i -= powerUps.get(i).despawn();
			}
		}
		dMarkerX.setPosition(-distanceX - playerStartX + dMarkerX.getWidth(), 0);
		if (-dMarkerX.distanceX < distanceX) {
			xMarkerNR++;
			dMarkerX.distanceX = -markerSpacing * 10 * xMarkerNR;
			dMarkerX.setLocation(-playerStartX + markerSpacing, 0);
			coinCount += 5;
		}
		dMarkerY.setPosition(0, distanceY + playerStartY - dMarkerY.getHeight());
		if (dMarkerY.distanceY < distanceY) {
			yMarkerNR++;
			dMarkerY.distanceY = markerSpacing * 10 * yMarkerNR;
			dMarkerY.setLocation(0, playerStartY - markerSpacing);
			coinCount += 5;
		}
	}

	public static void keyInput(int key, boolean pressed) {
		if (controllable && !gameFinished) {
			if (key == KeyEvent.VK_W) {
				if (!(up = keyPress(pressed))) {
					verticalDir = 0;
				}
			}
			if (key == KeyEvent.VK_A) {
				if (!(left = keyPress(pressed))) {
					horizontalDir = 0;
				}
			}
			if (key == KeyEvent.VK_S) {
				if (!(down = keyPress(pressed))) {
					verticalDir = 0;
				}
			}
			if (key == KeyEvent.VK_D) {
				if (!(right = keyPress(pressed))) {
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
		if (down && !groundContact) {
			verticalDir = 1;
		}
		if (key == KeyEvent.VK_SPACE && !gameFinished) {
			jetpack = keyPress(pressed);
		}
		if (jetpack) {
			playerSpeedX -= jetpackPower;
			playerSpeedY += jetpackPower;
		}
		if (sandbox) {
			if (key == KeyEvent.VK_G && pressed) {
				gravity = !gravity;
			}
			if (key == KeyEvent.VK_R && pressed && sandbox) {
				GameScreen.gameClose();
				GameScreen.startGame();
			}
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
				layers.get("Sidebar").setLocation(fWidth * 766 / 1000, 0);
			}
			paused = !paused;
		}
		if (preGameTimer.isRunning()) {
			if (key == KeyEvent.VK_SPACE) {
				playerX = fWidth / 3;
				playerY = fHeight / 4;
				playerSpeedX = -(startSpeed * launchAngle / 100 * powerLevel);
				playerSpeedY = startSpeed * (1 - launchAngle) / 100 * powerLevel;
				preGameTimer.stop();
				gameTimer.start();
				gameStarted = true;
			} else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_RIGHT) && pressed && launchAngle + 0.2 < 1) {
				launchAngle += 0.2;
			} else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT) && pressed && launchAngle - 0.2 > 0) {
				launchAngle -= 0.2;
			}
			objects.get("Angle").setLocation(playerX + (int) (fWidth * 128 / 1000 * launchAngle),
					playerY - (int) (fHeight * 128 / 500 * (1 - launchAngle)));
		}
	}

	static boolean keyPress(boolean pressed) {
		if (pressed) {
			return true;
		} else {
			return false;
		}
	}

	static void updateLabels() {
		time += 1000 / gameTimer.getDelay();
		labels.get("Distance").setText("X:" + df.format(distanceX / 10) + " m; Y:" + df.format(distanceY / 10) + " m");
		labels.get("Speed").setText("X = " + df.format(Math.abs(playerSpeedX * 3600 / 1000)) + " km/h; Y = "
				+ df.format(Math.abs(playerSpeedY * 3600 / 1000)) + " km/h");
		labels.get("Coins").setText("Coins = " + coinCount);
	}

	public static void convertCloudsToCoins() {
		for (int i = 0; i < clouds.size(); i++) {
			new Coin(clouds.get(i).getX(), clouds.get(i).getY(), coinWidth, coinHeight, 0, 0, clouds.get(i).distanceX,
					clouds.get(i).distanceY);
			i -= clouds.get(i).despawn();
		}
	}

	static void generateTrail() {
		if (trail) {
			trails.add(new JavaObject(player.getX(), player.getY(), player.getWidth() / 4, player.getHeight() / 4,
					"Trail", -distanceX, distanceY));
			if (trails.size() > 50) {
				trails.get(0).despawn();
				trails.remove(0);
			}
			for (JavaObject trail : trails) {
				trail.checkForDespawn(-distanceX, distanceY);
			}
		}
	}

	public static void setSidebarPos(boolean focused) {
		moveToX = fWidth - (fWidth * 16 / 1000);
		if (focused) {
			moveToX = fWidth * 754 / 1000;
		}
	}

	public static void setTrail() {
		trail = !trail;
		MainMenu.labels.get("Trail Display").setText(trail + "");
	}

	static void setupParameters() { // GIVES VARIABLES THEIR START VALUES
		paused = false; // boolean
		controllable = false;
		gameStarted = false;
		gameFinished = false;
		gravity = true;
		fWidth = MainMenu.fWidth; // size
		fHeight = fWidth / 2;
		groundLVL = fHeight * 3 / 4;
		Background.setupBackgroundSettings(groundLVL);
		playerWidth = 64;
		playerHeight = 64;
		playerX = fWidth * 64 / 1000;
		playerStartX = playerX;
		playerY = groundLVL - fWidth * 32 / 1000;
		playerStartY = playerY;
		coinWidth = 64;
		coinHeight = 64;
		markerSpacing = fWidth * 500 / 1000;
		moveToX = fWidth - (fWidth * 16 / 1000);
		launchAngle = 0.5; // random
		powerLevel = 20;
		powerGain = 2;
		distanceX = 0; // cumulative
		distanceY = 0;
		time = 0;
		coinCount = 0;
		xMarkerNR = 1;
		yMarkerNR = 1;
		playerSpeedX = 0; // speed
		playerSpeedY = 0;
		startSpeed = 200;
		minSpeed = 0.5;
		maxSpeed = 30;
		dragY = 0.02;
		dragX = 0.01;
		movingSpeed = 0.1 / dragY;
		speedBoost = 1;
		if (sandbox) {
			speedBoost = 5;
		}
		horizontalDir = 0;
		verticalDir = 0;
		jetpackPower = 2;
		playerIcons.put("U", setupImageIcon(playerWidth, playerHeight, "Player")); // image icons
		playerIcons.put("RU", setupImageIcon(playerWidth, playerHeight, "PlayerRU"));
		playerIcons.put("R", setupImageIcon(playerWidth, playerHeight, "PlayerR"));
		playerIcons.put("RD", setupImageIcon(playerWidth, playerHeight, "PlayerRD"));
		playerIcons.put("D", setupImageIcon(playerWidth, playerHeight, "PlayerD"));
		playerIcons.put("LD", setupImageIcon(playerWidth, playerHeight, "PlayerLD"));
		playerIcons.put("L", setupImageIcon(playerWidth, playerHeight, "PlayerL"));
		playerIcons.put("LU", setupImageIcon(playerWidth, playerHeight, "PlayerLU"));
	}

	public static ImageIcon setupImageIcon(int width, int height, String name) {
		ImageIcon icon = new ImageIcon(new ImageIcon(JavaLabel.fRoute + fPath + name + ".png").getImage()
				.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH));
		return icon;
	}

	static void setupLabels() {
		new JavaLabel("BackgroundWater", layers.get("gameLayer"), 0, 0, 1000, 500, labels, 0, fPath, false);
		player = new JavaLabel("", layers.get("gameLayer"), playerX, playerY, playerWidth, playerHeight, labels, 4,
				fPath, false);
		new JavaLabel("Cannon", layers.get("gameLayer"), playerX - 1000 * 16 / 1000, playerY - 1000 * 32 / 1000, 64, 64,
				objects, 5, fPath, false);
		new JavaLabel("PowerBase", layers.get("gameLayer"), playerX - 1000 * 16 / 1000, playerY + 1000 * 32 / 1000, 196,
				32, objects, 2, fPath, false);
		new JavaLabel("PowerFrame", objects.get("PowerBase"), 0, 0, 196, 32, labels, 0, fPath, false);
		new JavaLabel("PowerSlider", objects.get("PowerBase"), 0, 0, 196, 32, labels, 0, fPath, false);
		new JavaLabel("Angle", layers.get("gameLayer"), playerX + 1000 * 64 / 1000, playerY - 1000 * 64 / 1000, 16, 16,
				objects, 2, fPath, false);
		new Background("Backgr1", 0, groundLVL - 500);
		new Background("Backgr2", 1000, groundLVL - 500);
		new Background("Backgr3", 1000, groundLVL);
		new Background("Backgr4", 0, groundLVL);
		new JavaLabel("Distance", layers.get("gameLayer"), 300, 0, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Speed", layers.get("gameLayer"), 300, 32, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Coins", layers.get("gameLayer"), 300, 64, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Sidebar", layers.get("Sidebar"), 0, 0, 250, 500, labels, 0, fPath, false);
		new JavaLabel("Back", layers.get("Sidebar"), 20, 30, 200, 60, buttons);
		new JavaLabel("Reset", layers.get("Sidebar"), 20, 100, 200, 60, buttons);
		backgrounds.get("Backgr1").setIcon(Background.backdrop);
		backgrounds.get("Backgr2").setIcon(Background.backdrop);
		backgrounds.get("Backgr3").setIcon(Background.ground);
		backgrounds.get("Backgr4").setIcon(Background.ground);
		backgrKeys = backgrounds.keySet();
		objectKeys = objects.keySet();
		labels.get("Distance").setText("\"W A S D\" FOR MOVEMENT!");
		labels.get("Speed").setText("ARROW KEYS ADJUST ANGLE!");
		labels.get("Coins").setText("\"SPACE\" LAUNCHES PLAYER!");
		new Mouse(labels.get("Sidebar"));
	}

	public static void setMode(boolean modeSandbox) {
		sandbox = modeSandbox;
	}

	public static void gameClose() {
		layers.clear();
		labels.clear();
		objects.clear();
		buttons.clear();
		backgrounds.clear();
		clouds.clear();
		trails.clear();
		backgrKeys.clear();
		objectKeys.clear();
		GameScreen.gameTimer.stop();
		GameScreen.preGameTimer.stop();
		GameScreen.screen.removeAll();
		GameScreen.screen.dispose();
	}
}
