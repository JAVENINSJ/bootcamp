package stuntMan;

public class Cloud extends Coin {
	private static final long serialVersionUID = 1L;
	
	public static int width = 128, height = 128;

	public Cloud(int x, int y, int rangeX, int rangeY, double posX, double posY) {
		super(x, y, width, height, rangeX, rangeY, posX, posY, "Cloud");
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

	@Override
	public boolean checkForDespawn(double distanceX, double distanceY) {
		this.move();
		return super.checkForDespawn(distanceX, distanceY);
	}
	
	
}
