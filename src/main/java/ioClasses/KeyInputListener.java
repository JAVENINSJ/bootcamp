package ioClasses;

import java.awt.event.*;

import stuntMan.GameScreen;

public class KeyInputListener implements KeyListener {

	public KeyInputListener() {
		GameScreen.screen.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		GameScreen.keyInput(e.getKeyCode(), true); 
	}

	public void keyReleased(KeyEvent e) {
		GameScreen.keyInput(e.getKeyCode(), false); 
	}

	public void keyTyped(KeyEvent e) {
	}
}
//1 konstruktors; 3 metodes; 33 rindas