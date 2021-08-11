package stuntMan;

import javax.swing.*;
import inputClasses.*;
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
	public static String fPath = "//GameAssets//", theme = "Stars";
	public static JFrame screen;
	public static ImageIcon ground, backdrop;
	public static Timer gameTimer, preGameTimer;
	public static int playerX, playerY, horizontalDir, verticalDir, coinCount, time, groundLVL, powerUpTime;
	public static int fWidth, fHeight, coinWidth, coinHeight, playerWidth, playerHeight;
	public static double speedMove, playerSpeedX, playerSpeedY, xBackgr, yBackgr, minSpeed, maxSpeed;
	public static double drag, gravity, distanceX, distanceY, startSpeed, launchAngle, powerLevel, powerGain;
	public static boolean changeBG, paused, controllable, gameStarted;
	public static boolean up, left, right, down, jetpack, trail;
	public static HashMap<String, JLayeredPane> layers = new HashMap<String, JLayeredPane>();
	public static HashMap<String, JavaLabel> labels = new HashMap<String, JavaLabel>(),
			objects = new HashMap<String, JavaLabel>(), //
			buttons = new HashMap<String, JavaLabel>(), //
			backgrounds = new HashMap<String, JavaLabel>(); //
	public static HashMap<String, ImageIcon> playerIcons = new HashMap<String, ImageIcon>();
	static JavaLabel player;
	static Random random = new Random();
	public static ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	public static ArrayList<Coin> coins = new ArrayList<Coin>();
	public static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	public static ArrayList<JavaObject> trails = new ArrayList<JavaObject>();
	public static Set<String> backgrKeys, objectKeys;
	public static Rectangle playerHitbox;

	static DecimalFormat df = new DecimalFormat("0.00");

	public GameScreen() {
		gameTimer = new Timer(14, this);
		preGameTimer = new Timer(14, this);
	}

	public static void startGame() {
		setupParameters();
		screen = MainMenu.setupScreen(fWidth, fHeight);
		new JavaLayeredPane("gameLayer", screen, 0, 0, fWidth, fHeight, layers, 0);
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
		} else { // FLYING TIME
			moveBackground();
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
	}

	public static void movePlayerIcon() {
		checkPlayerMovement();
		if (Math.abs(playerSpeedX) < maxSpeed) {
			playerSpeedX -= horizontalDir;
			playerX += horizontalDir * (boundCheck(playerX + horizontalDir, fHeight / 2, horizontalDir));
		}
		if (Math.abs(playerSpeedY) < maxSpeed) {
			if (verticalDir == -1) {
				playerSpeedY -= verticalDir;
			}
			playerY += verticalDir * (boundCheck(playerY + verticalDir, fWidth / 4, verticalDir));
		}
		if (playerY + 1 < fHeight / 2 + fHeight / 4 && verticalDir != -1 && controllable && gravity != 0) {
			playerY += 1; // GRAVITY
		}
		player.setLocation(playerX, playerY);
		playerHitbox = player.getBounds();
		setPlayerIcon();
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

	public static void moveBackground() {
		gravity = 1 * 0.5;
		boolean setToGround = false;
		for (String backgroundKey : backgrKeys) {
			JavaLabel background = backgrounds.get(backgroundKey);
			if (time > 10) {
				if (background.getIcon().equals(ground) && verticalDir != -1 && background.getY() <= groundLVL
						|| distanceY < 0) {
					verticalDir = 0;
					gravity = 0;
					playerSpeedY = 0;
					playerY = groundLVL - fWidth * 32 / 1000;
					distanceY = 0;
					setToGround = true;
					break;
				}
			}
		}
		for (String backgroundKey : backgrKeys) {
			changeBG = false;
			JavaLabel background = backgrounds.get(backgroundKey);
			int tempX = setBackgroundPos((int) Math.round(-playerSpeedX), fWidth, background.getX());
			int tempY = setBackgroundPos((int) Math.round(-playerSpeedY), fHeight, background.getY());
			if (setToGround) {
				tempY = groundLVL;
				if (background.getIcon().equals(backdrop)) {
					tempY = groundLVL - fHeight;
				}
			}
			if (changeBG) {
				if (tempY >= groundLVL && distanceY < 500) {
					background.setIcon(ground);
				} else {
					background.setIcon(backdrop);
					int spawnType = random.nextInt(20) + 1;
					if (distanceY > 2500 && spawnType > 8) {
						if (powerUpTime > 0) {
							new Coin(tempX, tempY, coinWidth, coinHeight, fWidth, fHeight, -distanceX, distanceY);
						} else {
							new Cloud(tempX, tempY, fWidth, fHeight, -distanceX, distanceY);
						}
					}
					if (spawnType < 14) {
						new Coin(tempX, tempY, coinWidth, coinHeight, fWidth, fHeight, -distanceX, distanceY);
					}
					if (spawnType == 10) {
						new PowerUp(tempX, tempY, fWidth, fHeight, -distanceX, distanceY);
					}
				}
			}
			background.setLocation(tempX, tempY);
		}
		if (Math.abs(playerSpeedX) < minSpeed) {
			playerSpeedX = 0;
		}
		drag = 0.008;
		if (distanceY < 1) {
			drag *= 10;
		}
		distanceX -= playerSpeedX;
		distanceY += playerSpeedY;
		playerSpeedX -= playerSpeedX * drag;
		playerSpeedY -= playerSpeedY * drag;
		if (controllable && playerSpeedY - gravity > -maxSpeed) {// BACKGROUND GRAVITY
			playerSpeedY -= gravity;
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
			pos += 2 * bound * direction;
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
				playerSpeedX = (0.99 - drag * 10) * playerSpeedX;
				playerSpeedY = (0.99 - drag * 10) * playerSpeedY;
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
	}

	public static void keyInput(int key, boolean pressed) {
		if (controllable) {
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
		if (down) {
			verticalDir = 1;
		}
		if (key == KeyEvent.VK_SPACE) {
			jetpack = keyPress(pressed);
		}
		if (jetpack) {
			playerSpeedX -= 2;
			playerSpeedY += 2;
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
		labels.get("Distance")
				.setText("X:" + df.format(distanceX / 100) + " m; Y:" + df.format(distanceY / 100) + " m");
		labels.get("Speed").setText("X = " + df.format(Math.abs(playerSpeedX * 3600 / 1000)) + " km/h; Y = "
				+ df.format(Math.abs(playerSpeedY * 3600 / 1000)) + " km/h");
		labels.get("Coins").setText("Coins = " + coinCount);
	}

	public static void convertCloudsToCoins() {
		System.out.println("B " + clouds.size() + " " + coins.size());
		for (int i = 0; i < clouds.size(); i++) {
			new Coin(clouds.get(i).getX(), clouds.get(i).getY(), coinWidth, coinHeight, 0, 0, clouds.get(i).distanceX,
					clouds.get(i).distanceY);
			i -= clouds.get(i).despawn();
		}
		System.out.println("A " + clouds.size() + " " + coins.size());
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

	public static void setTheme() {
		if ("Stars".equals(theme)) {
			theme = "DayTime";
		} else {
			theme = "Stars";
		}
		MainMenu.labels.get("Theme Display").setText(theme + "");
	}

	public static void setTrail() {
		trail = !trail;
		MainMenu.labels.get("Trail Display").setText(trail + "");
	}

	static void setupParameters() { // GIVES VARIABLES THEIR START VALUES
		paused = false;
		controllable = false;
		gameStarted = false;
		fWidth = MainMenu.fWidth;
		fHeight = fWidth / 2;
		groundLVL = fHeight * 3 / 4;
		playerWidth = 64;
		playerHeight = 64;
		speedMove = 5;
		playerX = fWidth * 64 / 1000;
		playerY = groundLVL - fWidth * 32 / 1000;
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
		coinWidth = 64;
		coinHeight = 64;
		ground = setupImageIcon(1000, 500, "BackgroundGrass");
		backdrop = setupImageIcon(1000, 500, "Background" + theme);
		playerIcons.put("U", setupImageIcon(playerWidth, playerHeight, "Player"));
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
		player = new JavaLabel("", layers.get("gameLayer"), playerX, playerY, playerWidth, playerHeight, labels, 3,
				fPath, false);
		new JavaLabel("Cannon", layers.get("gameLayer"), playerX - 1000 * 16 / 1000, playerY - 1000 * 32 / 1000, 64, 64,
				objects, 4, fPath, false);
		new JavaLabel("PowerBase", layers.get("gameLayer"), playerX - 1000 * 16 / 1000, playerY + 1000 * 32 / 1000, 196,
				32, objects, 1, fPath, false);
		new JavaLabel("PowerFrame", objects.get("PowerBase"), 0, 0, 196, 32, labels, 1, fPath, false);
		new JavaLabel("PowerSlider", objects.get("PowerBase"), 0, 0, 196, 32, labels, 1, fPath, false);
		new JavaLabel("Angle", layers.get("gameLayer"), playerX + 1000 * 64 / 1000, playerY - 1000 * 64 / 1000, 16, 16,
				objects, 1, fPath, false);

		new JavaLabel("Backgr1", layers.get("gameLayer"), 0, groundLVL - 500, 1000, 500, backgrounds, 0, fPath, false);
		new JavaLabel("Backgr2", layers.get("gameLayer"), 1000, groundLVL - 500, 1000, 500, backgrounds, 0, fPath,
				false);
		new JavaLabel("Backgr3", layers.get("gameLayer"), 1000, groundLVL, 1000, 500, backgrounds, 0, fPath, false);
		new JavaLabel("Backgr4", layers.get("gameLayer"), 0, groundLVL, 1000, 500, backgrounds, 0, fPath, false);
		new JavaLabel("Distance", layers.get("gameLayer"), 300, 0, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Speed", layers.get("gameLayer"), 300, 32, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Coins", layers.get("gameLayer"), 300, 64, 400, 32, labels, 10, fPath, true);
		new JavaLabel("Back", layers.get("gameLayer"), 100, 0, 200, 50, buttons);
		new JavaLabel("Reset", layers.get("gameLayer"), 700, 0, 200, 50, buttons);
		backgrounds.get("Backgr1").setIcon(backdrop);
		backgrounds.get("Backgr2").setIcon(backdrop);
		backgrounds.get("Backgr3").setIcon(ground);
		backgrounds.get("Backgr4").setIcon(ground);
		buttons.get("Back").setVisible(false);
		buttons.get("Reset").setVisible(false);
		backgrKeys = backgrounds.keySet();
		objectKeys = objects.keySet();
		labels.get("Distance").setText("\"W A S D\" FOR MOVEMENT!");
		labels.get("Speed").setText("ARROW KEYS ADJUST ANGLE!");
		labels.get("Coins").setText("\"SPACE\" LAUNCHES PLAYER!");
	}

	public static void gameClose() {
		GameScreen.gameTimer.stop();
		GameScreen.screen.removeAll();
		GameScreen.screen.dispose();
		layers.clear();
		labels.clear();
		objects.clear();
		buttons.clear();
		backgrounds.clear();
		clouds.clear();
		backgrKeys.clear();
		objectKeys.clear();
	}
}
