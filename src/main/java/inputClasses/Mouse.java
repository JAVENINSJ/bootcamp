package inputClasses;

import java.awt.event.*;

import objects.JavaObject;
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
		if ("Log In".equals(button.name) && button.inZone) {
			MainMenu.checkLogin(false);
		}
		if ("Create User".equals(button.name) && button.inZone) {
			MainMenu.checkLogin(true);
		}
		if ("Enter As Guest".equals(button.name) && button.inZone) {
			MainMenu.setupMenuSettings();
		}
		if ("Play".equals(button.name) && button.inZone) {
			JavaObject.setSizing(MainMenu.fWidth);
			GameScreen.startGame();
			MainMenu.screen.setVisible(false);
			MainMenu.timer.stop();
		}
		if ("Upgrades".equals(button.name) && button.inZone) {
			MainMenu.buttons.get("Main Menu").setVisible(true);
			MainMenu.switchScreens("Upgrades");
			MainMenu.labels.get("Coins").setText("" + MainMenu.coins);
		}
		if ("Settings".equals(button.name) && button.inZone) {
			MainMenu.buttons.get("Main Menu").setVisible(true);
			MainMenu.switchScreens("Settings");
		}
		if ("Logout".equals(button.name) && button.inZone) {
			MainMenu.switchScreens("Login");
		}
		if ("Exit".equals(button.name) && button.inZone) {
			System.exit(0);
		}
		// UPGRADES ::
		if ("Main Menu".equals(button.name) && button.inZone) {
			MainMenu.buttons.get("Main Menu").setVisible(false);
			MainMenu.switchScreens("Menu");
		}
		// SETTINGS ::
		if ("Audio".equals(button.name) && button.inZone) {
			Audio.setAudio();
		}
		if ("Trail".equals(button.name) && button.inZone) {
			GameScreen.setTrail();
		}
		if ("Resolution".equals(button.name) && button.inZone) {
			MainMenu.resolutionNR += MainMenu.selectSetting(MainMenu.resolutionNR, MainMenu.resolution.length);
			MainMenu.buttons.get("Resolution").setText(MainMenu.resolution[MainMenu.resolutionNR]+"");
		}
		if ("Accept Settings".equals(button.name) && button.inZone) {
			MainMenu.resetScreen();
		}

		// GAME SCREEN ::
		if ("Back".equals(button.name) && button.inZone) {
			MainMenu.coins += GameScreen.coinCount;
			GameScreen.gameClose();
			MainMenu.timer.start();
			MainMenu.screen.setVisible(true);
		}
		if ("Reset".equals(button.name) && button.inZone) {
			GameScreen.gameClose();
			GameScreen.startGame();
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