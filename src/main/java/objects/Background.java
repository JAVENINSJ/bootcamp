<<<<<<< HEAD
package objects;

import javax.swing.ImageIcon;

import stuntMan.GameScreen;
import stuntMan.MainMenu;

public class Background extends JavaObject {

	private static final long serialVersionUID = 1L;

	boolean changeBG;
	static int groundLVL;
	public static String theme = "Stars";
	public static ImageIcon ground, backdrop, water;

	public static void setupBackgroundSettings(int lvl) {
		groundLVL = lvl;
		ground = GameScreen.setupImageIcon(1000, 500, "BackgroundGrass");
		water = GameScreen.setupImageIcon(1000, 500, "BackgroundWater");
		backdrop = GameScreen.setupImageIcon(1000, 500, "Background" + theme);
	}

	public Background(String name, int x, int y) {
		super(x, y, fWidth, fHeight, 0, 0, 0, 0, "", 1);
		GameScreen.backgrounds.put(name, this);
	}

	public int pizgets(double distanceX, double distanceY, double playerSpeedX, double playerSpeedY) {
		changeBG = false;
		int tempX = setBackgroundPos((int) Math.round(-playerSpeedX), fWidth, this.getX(), true);
		int tempY = setBackgroundPos((int) Math.round(-playerSpeedY), fHeight, this.getY(), false);
		this.setPosition(distanceX, distanceY);
		if (changeBG) {
			if (y >= groundLVL && distanceY < 500) {
				if (GameScreen.sandbox) {
					this.setIcon(ground);
				} else {
					this.setIcon(water);
				}
			} else {
				this.setIcon(backdrop);
				int spawnType = random.nextInt(20) + 1;
				if (distanceY > 2500 && spawnType > 8) {
					if (GameScreen.powerUpTime > 0) {
						new Coin(tempX, tempY, GameScreen.coinWidth, GameScreen.coinHeight, fWidth, fHeight, distanceX,
								distanceY);
					} else {
						new Cloud(tempX, tempY, fWidth, fHeight, distanceX, distanceY);
					}
				}
				if (spawnType < 14) {
					new Coin(tempX, tempY, GameScreen.coinWidth, GameScreen.coinHeight, fWidth, fHeight, distanceX,
							distanceY);
				}
				if (spawnType == 10) {
					new PowerUp(tempX, tempY, fWidth, fHeight, distanceX, distanceY);
				}
			}
		}
		if (this.colisionCheck() && this.getIcon().equals(ground) && GameScreen.verticalDir != -1) {
			return 1;
		}
		return 0;
	}

	public int setBackgroundPos(int speed, int bound, int pos, boolean x) {
		int direction = -1;
		boolean check = false;
		if (pos + speed > pos) {
			direction = 1;
			check = true;
		}
		pos -= speed;
		if ((pos < (-bound * direction)) == check) {
			pos += 2 * bound * direction;
			this.changeBG = true;
			if (x) {
				this.x += direction * fWidth * 2;
			} else {
				this.y += direction * fHeight * 2;
			}
		}
		return pos;
	}

	public static void setTheme() {
		if ("Stars".equals(theme)) {
			theme = "DayTime";
		} else {
			theme = "Stars";
		}
		MainMenu.labels.get("Theme Display").setText(theme + "");
	}
}
=======
package objects;

import javax.swing.ImageIcon;

import stuntMan.GameScreen;
import stuntMan.MainMenu;

public class Background extends JavaObject {

	private static final long serialVersionUID = 1L;

	boolean changeBG;
	static int groundLVL;
	public static String theme = "Stars";
	public static ImageIcon ground, backdrop, water;

	public static void setupBackgroundSettings(int lvl) {
		groundLVL = lvl;
		ground = GameScreen.setupImageIcon(1000, 500, "BackgroundGrass");
		water = GameScreen.setupImageIcon(1000, 500, "BackgroundWater");
		backdrop = GameScreen.setupImageIcon(1000, 500, "Background" + theme);
	}

	public Background(String name, int x, int y) {
		super(x, y, fWidth, fHeight, 0, 0, 0, 0, "", 1);
		GameScreen.backgrounds.put(name, this);
	}

	public int pizgets(double distanceX, double distanceY, double playerSpeedX, double playerSpeedY) {
		changeBG = false;
		int tempX = setBackgroundPos((int) Math.round(-playerSpeedX), fWidth, this.getX(), true);
		int tempY = setBackgroundPos((int) Math.round(-playerSpeedY), fHeight, this.getY(), false);
		this.setPosition(distanceX, distanceY);
		if (changeBG) {
			if (y >= groundLVL && distanceY < 500) {
				if (GameScreen.sandbox) {
					this.setIcon(ground);
				} else {
					this.setIcon(water);
				}
			} else {
				this.setIcon(backdrop);
				int spawnType = random.nextInt(20) + 1;
				if (distanceY > 2500 && spawnType > 8) {
					if (GameScreen.powerUpTime > 0) {
						new Coin(tempX, tempY, GameScreen.coinWidth, GameScreen.coinHeight, fWidth, fHeight, distanceX,
								distanceY);
					} else {
						new Cloud(tempX, tempY, fWidth, fHeight, distanceX, distanceY);
					}
				}
				if (spawnType < 14) {
					new Coin(tempX, tempY, GameScreen.coinWidth, GameScreen.coinHeight, fWidth, fHeight, distanceX,
							distanceY);
				}
				if (spawnType == 10) {
					new PowerUp(tempX, tempY, fWidth, fHeight, distanceX, distanceY);
				}
			}
		}
		if (this.colisionCheck() && this.getIcon().equals(ground) && GameScreen.verticalDir != -1) {
			return 1;
		}
		return 0;
	}

	public int setBackgroundPos(int speed, int bound, int pos, boolean x) {
		int direction = -1;
		boolean check = false;
		if (pos + speed > pos) {
			direction = 1;
			check = true;
		}
		pos -= speed;
		if ((pos < (-bound * direction)) == check) {
			pos += 2 * bound * direction;
			this.changeBG = true;
			if (x) {
				this.x += direction * fWidth * 2;
			} else {
				this.y += direction * fHeight * 2;
			}
		}
		return pos;
	}

	public static void setTheme() {
		if ("Stars".equals(theme)) {
			theme = "DayTime";
		} else {
			theme = "Stars";
		}
		MainMenu.labels.get("Theme Display").setText(theme + "");
	}
}
>>>>>>> c4ce5c198a16b0dcfcfc43976bf5a624ac81f435
