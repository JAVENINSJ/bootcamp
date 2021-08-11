package stuntMan;

import passwords.Password;

public class startClass {

	public static void main(String[] argv) {
		System.out.println("Begin");
		Password.getLoginFile();
		new GameScreen();
		new MainMenu();
	}
}
