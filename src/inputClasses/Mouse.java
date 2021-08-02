package inputClasses;

import java.awt.event.*;

import stuntMan.*;

public class Mouse implements MouseListener {

	public Mouse(JavaLabel button) {
		button.addMouseListener(this); // adds mouse listener to label
	}

	public void mousePressed(MouseEvent e) {
		((JavaLabel) e.getSource()).setIcon(2, true); // buttonIcon_Pressed.png
	}

	public void mouseReleased(MouseEvent e) {
		JavaLabel button = (JavaLabel) e.getSource();
		if ("Play".equals(button.name)) {
			new GameScreen();
			MainMenu.screen.setVisible(false);
		}
		if ("Back".equals(button.name)) {
			gameClose();
			MainMenu.screen.setVisible(true);
		}
		if (button.inZone && !button.selected) {
			button.setIcon(1, true); // buttonIcon_ON.png
		}
	}

	void gameClose() {
		GameScreen.timer.stop();
		GameScreen.screen.removeAll();
		GameScreen.screen.dispose();
	}

	public void mouseEntered(MouseEvent e) {
		if ((boolean) ((JavaLabel) e.getSource()).selected == false) {
			((JavaLabel) e.getSource()).setIcon(1, true); // buttonIcon_ON.png
		}
	}

	public void mouseExited(MouseEvent e) {
		if ((boolean) ((JavaLabel) e.getSource()).selected == false) {
			((JavaLabel) e.getSource()).setIcon(0, false); // buttonIcon.png
		}
	}

	public void mouseClicked(MouseEvent e) { // UNNECESSARY METHOD (NEEDS TO EXISTS, BECAUSE OF IMPLEMENTATION)
	}
}
// 1 konstruktors; 7 metodes; 112 rindas