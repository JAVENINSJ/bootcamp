package objects;

import stuntMan.GameScreen;

public class PowerUp extends JavaObject {
	private static final long serialVersionUID = 1L;

	public static int width = 64, height = 64;
	String type;
	
	public PowerUp(int x, int y, int rangeX, int rangeY, double posX, double posY) {
		super(x, y, width, height, rangeX, rangeY, posX, posY, "PowerUp");
		GameScreen.powerUps.add(this);
	}
	
	@Override
	public int despawn() {
		GameScreen.powerUps.remove(this);
		return super.despawn();
	}
}
