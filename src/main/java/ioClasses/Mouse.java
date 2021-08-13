package ioClasses;

import java.awt.event.*;

import objects.Background;
import objects.JavaObject;
import stuntMan.*;

public class Mouse implements MouseListener {

	public Mouse(JavaLabel button) {
		button.addMouseListener(this); // adds mouse listener to label
	}

	public void mousePressed(MouseEvent e) {
		if (!((JavaLabel) e.getSource()).name.equals("Sidebar")) {
			((JavaLabel) e.getSource()).setIcon(2, true); // buttonPressed.png
		}
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
			GameScreen.setMode(false);
			GameScreen.startGame();
			MainMenu.screen.setVisible(false);
			MainMenu.timer.stop();
		}
		if ("Sandbox".equals(button.name) && button.inZone) {
			JavaObject.setSizing(MainMenu.fWidth);
			GameScreen.setMode(true);
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
		if ("Wingsuit".equals(button.name) && button.inZone) {
			
		}
		if ("Radio".equals(button.name) && button.inZone) {
			
		}
		if ("Power".equals(button.name) && button.inZone) {
			
		}
		if ("Fuel".equals(button.name) && button.inZone) {
			
		}
		if ("Powder".equals(button.name) && button.inZone) {
			
		}
		if ("Angle".equals(button.name) && button.inZone) {
			
		}
		// SETTINGS ::
		if ("Audio".equals(button.name) && button.inZone) {
			Audio.setAudio();
		}
		if ("Theme".equals(button.name) && button.inZone) {
			Background.setTheme();
		}
		if ("Trail".equals(button.name) && button.inZone) {
			GameScreen.setTrail();
		}
		if ("Resolution".equals(button.name) && button.inZone) {
			MainMenu.setResolution();
		}
		if ("Accept Settings".equals(button.name) && button.inZone) {
			MainMenu.resetScreen();
		}

		// GAME SCREEN ::
		if ("Back".equals(button.name) && button.inZone) {
			if (!GameScreen.sandbox) {
				MainMenu.coins += GameScreen.coinCount;
			}
			GameScreen.gameClose();
			MainMenu.timer.start();
			MainMenu.screen.setVisible(true);
		}
		if ("Reset".equals(button.name) && button.inZone) {
			GameScreen.gameClose();
			GameScreen.startGame();
		}
		if (button.inZone && !button.selected && !((JavaLabel) e.getSource()).name.equals("Sidebar")) {
			button.setIcon(1, true); // buttonOn.png// FOR ALL BUTTONS
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (!((JavaLabel) e.getSource()).name.equals("Sidebar")) {
			if (((JavaLabel) e.getSource()).selected == false) {
				((JavaLabel) e.getSource()).setIcon(1, true); // buttonON.png
			}
			if (((JavaLabel) e.getSource()).name.equals("Back") || ((JavaLabel) e.getSource()).name.equals("Reset")) {
				GameScreen.setSidebarPos(true);
			}
		} else {
			GameScreen.setSidebarPos(true);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (!((JavaLabel) e.getSource()).name.equals("Sidebar")) {
			if (((JavaLabel) e.getSource()).selected == false) {
				((JavaLabel) e.getSource()).setIcon(0, false); // button.png
			}
			if (((JavaLabel) e.getSource()).name.equals("Back") || ((JavaLabel) e.getSource()).name.equals("Reset")) {
				GameScreen.setSidebarPos(false);
			}
		} else {
			GameScreen.setSidebarPos(false);
		}
	}

	public void mouseClicked(MouseEvent e) { // UNNECESSARY METHOD (NEEDS TO EXISTS, BECAUSE OF IMPLEMENTATION)
	}
}