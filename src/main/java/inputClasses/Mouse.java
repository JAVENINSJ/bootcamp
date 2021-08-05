package inputClasses;

import java.awt.event.*;

import stuntMan.*;

public class Mouse implements MouseListener {

	public Mouse(JavaLabel button) {
		button.addMouseListener(this); // adds mouse listener to label
	}

	public void mousePressed(MouseEvent e) {
		((JavaLabel) e.getSource()).setIcon(2, true); // buttonPressed.png
	}

	public void mouseReleased(MouseEvent e) {
		JavaLabel button = (JavaLabel) e.getSource();
		// MAIN MENU ::
		if ("Play".equals(button.name) && button.inZone) {
			new GameScreen();
			MainMenu.screen.setVisible(false);
		}
		if ("Upgrades".equals(button.name) && button.inZone) {
			MainMenu.buttons.get("Main Menu").setVisible(true);
			MainMenu.switchScreens("Upgrades");
		}
		if ("Settings".equals(button.name) && button.inZone) {
			MainMenu.buttons.get("Main Menu").setVisible(true);
			MainMenu.switchScreens("Settings");
		}
		if ("Main Menu".equals(button.name) && button.inZone) {
			MainMenu.buttons.get("Main Menu").setVisible(false);
			MainMenu.switchScreens("Main");
		}
		if ("Exit".equals(button.name) && button.inZone) {
			System.exit(0);
		}
		// GAME SCREEN ::
		if ("Back".equals(button.name) && button.inZone) {
			GameScreen.gameClose();
			MainMenu.screen.setVisible(true);
		}
		if (button.inZone && !button.selected) { // FOR ALL BUTTONS
			button.setIcon(1, true); // buttonOn.png
		}
	}

	public void mouseEntered(MouseEvent e) {
		if ((boolean) ((JavaLabel) e.getSource()).selected == false) {
			((JavaLabel) e.getSource()).setIcon(1, true); // buttonON.png
		}
	}

	public void mouseExited(MouseEvent e) {
		if ((boolean) ((JavaLabel) e.getSource()).selected == false) {
			((JavaLabel) e.getSource()).setIcon(0, false); // button.png
		}
	}

	public void mouseClicked(MouseEvent e) { // UNNECESSARY METHOD (NEEDS TO EXISTS, BECAUSE OF IMPLEMENTATION)
	}
}