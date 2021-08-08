package objects;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import inputClasses.JavaLabel;
import stuntMan.GameScreen;

public class JavaObject extends JLabel {
	private static final long serialVersionUID = 1L;
	static Random random = new Random();
	static int frameWidth = GameScreen.frameWidth, frameHeight = GameScreen.frameHeight;
	String name;
	int x, y;
	public double speedX = 0, speedY = 0, distanceX, distanceY;
	public Rectangle hitbox;

	public JavaObject(int x, int y, int width, int height, int rangeX, int rangeY, double posX, double posY, String pngName) {
		super(new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//" + pngName + ".png").getImage()
				.getScaledInstance(width, height, Image.SCALE_SMOOTH)), JLabel.CENTER);
		x = random.nextInt((rangeX - width) / 10) * 10 + x;
		y = random.nextInt((rangeY - height) / 10) * 10 + y;
		this.x = x;
		this.y = y;
		this.distanceX = posX;
		this.distanceY = posY;
		this.setBounds(x, y, width, height);
		this.hitbox = this.getBounds();
		GameScreen.layers.get("gameLayer").add(this, (Integer) 1);
	}

	public boolean checkForDespawn(double distanceX, double distanceY) {
		this.setPosition(distanceX, distanceY);
		if (this.getX() < -frameWidth || this.getX() > 2 * frameWidth || this.getY() < -frameHeight
				|| this.getY() > 2 * frameHeight) {
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
