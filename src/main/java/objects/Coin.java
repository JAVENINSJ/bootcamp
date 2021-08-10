package objects;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import inputClasses.JavaLabel;
import stuntMan.GameScreen;

public class Coin extends JavaObject {
	
	private static final long serialVersionUID = 1L;
	static Random random = new Random();
	public static int animationFrame;
	JLabel coin;

	public Coin(int x, int y, int width, int height, int rangeX, int rangeY, double posX, double posY, String pngName) {
		super(x, y, width, height, rangeX, rangeY, posX, posY, pngName);
		coin = new JLabel(new ImageIcon(new ImageIcon(JavaLabel.fRoute + "//GameAssets//Coin.png").getImage()
				.getScaledInstance(width, height * 4, Image.SCALE_SMOOTH)), JLabel.CENTER);
		coin.setBounds(0, 0, width, height * 4);
		this.add(coin);
		GameScreen.coins.add(this);
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
