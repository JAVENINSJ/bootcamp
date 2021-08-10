package stuntMan;

import passwords.Password;

public class startClass {

	public static void main(String[] args) {
		Password.getLoginFile();
		new GameScreen();
		new MainMenu();
	}
}
