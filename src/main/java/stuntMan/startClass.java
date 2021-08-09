package stuntMan;

import inputClasses.JavaLabel;
import passwords.Password;

public class startClass {

	public static void main(String[] args) {
		System.out.println("PASHOL");
		JavaLabel.setupButtonImages();
		Password.getLoginFile();
		new GameScreen();
		new MainMenu();
	}
}
