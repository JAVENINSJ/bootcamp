<<<<<<< HEAD
package objects;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ioClasses.JavaLabel;
import stuntMan.GameScreen;

public class Coin extends JavaObject {

	private static final long serialVersionUID = 1L;
	static Random random = new Random();
	public static int animationFrame;
	JLabel coin;

	public Coin(int x, int y, int width, int height, int rangeX, int rangeY, double posX, double posY) {
		super(x, y, width, height, rangeX, rangeY, posX, posY, "CoinBase", 5);
		addAnimation(width, height);
		GameScreen.coins.add(this);
	}
	
	public void addAnimation(int width, int height) {
		coin = new JLabel(
				new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//Coin.png").getImage()
						.getScaledInstance(fWidth * width / 1000, fWidth * height * 4 / 1000, Image.SCALE_SMOOTH)),
				JLabel.CENTER);
		coin.setBounds(0, 0, fWidth * width / 1000, fWidth * height * 4 / 1000);
		this.add(coin);
	}

	@Override
	public int despawn() {
		GameScreen.coins.remove(this);
		return super.despawn();
	}

	public void animate() {
		this.coin.setLocation(0, (-animationFrame / 10) * this.getHeight());
	}

	public static void setAnimationFrame() {
		if (animationFrame == 39) {
			animationFrame = 0;
		} else {
			animationFrame++;
		}
	}
}
=======
package objects;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ioClasses.JavaLabel;
import stuntMan.GameScreen;

public class Coin extends JavaObject {

	private static final long serialVersionUID = 1L;
	static Random random = new Random();
	public static int animationFrame;
	JLabel coin;

	public Coin(int x, int y, int width, int height, int rangeX, int rangeY, double posX, double posY) {
		super(x, y, width, height, rangeX, rangeY, posX, posY, "CoinBase", 5);
		addAnimation(width, height);
		GameScreen.coins.add(this);
	}
	
	public void addAnimation(int width, int height) {
		coin = new JLabel(
				new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//Coin.png").getImage()
						.getScaledInstance(fWidth * width / 1000, fWidth * height * 4 / 1000, Image.SCALE_SMOOTH)),
				JLabel.CENTER);
		coin.setBounds(0, 0, fWidth * width / 1000, fWidth * height * 4 / 1000);
		this.add(coin);
	}

	@Override
	public int despawn() {
		GameScreen.coins.remove(this);
		return super.despawn();
	}

	public void animate() {
		this.coin.setLocation(0, (-animationFrame / 10) * this.getHeight());
	}

	public static void setAnimationFrame() {
		if (animationFrame == 39) {
			animationFrame = 0;
		} else {
			animationFrame++;
		}
	}
}
>>>>>>> c4ce5c198a16b0dcfcfc43976bf5a624ac81f435
