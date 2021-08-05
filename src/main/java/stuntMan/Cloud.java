package stuntMan;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import inputClasses.JavaLabel;

public class Cloud extends JLabel {
	private static final long serialVersionUID = 1L;
	static Random random = new Random();
	String name;
	int x, y;
	double speedX = 0, speedY = 0, distanceX, distanceY;
	static int width = 128, height = 128;
	Rectangle hitbox;

	public Cloud(int x, int y, int rangeX, int rangeY, double distanceX, double distanceY) {
		super(new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//Cloud.png").getImage()
				.getScaledInstance(width, height, Image.SCALE_SMOOTH)), JLabel.CENTER);
		x = random.nextInt((rangeX - width) / 10) * 10 + x;
		y = random.nextInt((rangeY - height) / 10) * 10 + y;
		this.x = x;
		this.y = y;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		this.setBounds(x, y, 128, 128);
		this.hitbox = this.getBounds();
		GameScreen.layers.get("gameLayer").add(this, (Integer) 1);
		GameScreen.clouds.add(this);
	}

	public void move() {
		for (Cloud cloud : GameScreen.clouds) {
			if (this.hitbox.intersects(cloud.hitbox) && !this.equals(cloud)) {
				cloud.speedX = 0.7 * this.speedX;
				cloud.speedY = 0.7 * this.speedY;
				this.speedX = 0.3 * this.speedX;
				this.speedY = 0.3 * this.speedY;
			}
		}
		this.distanceX += this.speedX -= this.speedX * 0.1;
		this.distanceY += this.speedY -= this.speedY * 0.1;
	}

	public void setPosition(double moveX, double moveY) {
		this.setLocation(x + (int) (moveX - distanceX), y + (int) (moveY - distanceY));
	}
}
