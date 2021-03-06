package objects;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ioClasses.JavaLabel;
import stuntMan.GameScreen;
import stuntMan.MainMenu;

public class JavaObject extends JLabel {
	private static final long serialVersionUID = 1L;
	static Random random = new Random();
	static int fWidth = GameScreen.fWidth, fHeight = GameScreen.fHeight;
	String name;
	public int x;
	public int y;
	public double speedX = 0, speedY = 0, distanceX, distanceY;
	public Rectangle hitbox;

	public static void setSizing(int size) {
		fWidth = MainMenu.fWidth;
		fHeight = fWidth / 2;
	}

	public JavaObject(int x, int y, int width, int height, int rangeX, int rangeY, double posX, double posY,
			String pngName, int layer) {
		super(new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//" + pngName + ".png").getImage()
				.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH)), JLabel.CENTER);
		if (rangeX != 0 && rangeY != 0) {
			x = random.nextInt((rangeX - (fWidth * width / 1000)) / 10) * 10 + x;
			y = random.nextInt((rangeY - (fWidth * height / 1000)) / 10) * 10 + y;
		}
		this.x = x;
		this.y = y;
		this.distanceX = posX;
		this.distanceY = posY;
		this.setBounds(x, y, fWidth * width / 1000, fWidth * height / 1000);
		this.hitbox = this.getBounds();
		GameScreen.layers.get("gameLayer").add(this, (Integer) layer);
	}

	public JavaObject(int x, int y, int width, int height, String pngName, double posX, double posY) { // !! FOR TRAIL
		super(new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//" + pngName + ".png").getImage()
				.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH)), JLabel.CENTER);
		this.setBounds(x, y, width, height);
		this.x = x + width * 3 / 2;
		this.y = y + height * 3 / 2;
		this.distanceX = posX;
		this.distanceY = posY;
		GameScreen.layers.get("gameLayer").add(this, (Integer) 2);
	}

	public boolean checkForDespawn(double distanceX, double distanceY) {
		this.setPosition(distanceX, distanceY);
		if (this.getX() < -fWidth || this.getX() > 2 * fWidth || this.getY() < -fHeight || this.getY() > 2 * fHeight) {
			GameScreen.layers.get("gameLayer").remove(this);
			return true;
		}
		return false;
	}

	public boolean colisionCheck() {
		this.hitbox = this.getBounds();
		if (GameScreen.playerHitbox.intersects(this.hitbox)) {
			return true;
		}
		return false;
	}

	public int despawn() {
		GameScreen.layers.get("gameLayer").remove(this);
		return 1;
	}

	public void setPosition(double moveX, double moveY) {
		this.setLocation(x + (int) (moveX - distanceX), y + (int) (moveY - distanceY));
	}
}