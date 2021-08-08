package objects;

public class PowerUp extends Coin {
	private static final long serialVersionUID = 1L;

	public static int width = 64, height = 64;
	String type;
	
	public PowerUp(int x, int y, int rangeX, int rangeY, double posX, double posY) {
		super(x, y, width, height, rangeX, rangeY, posX, posY, "PowerUp");
	}
	
	
}
