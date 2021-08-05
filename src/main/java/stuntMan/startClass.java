package stuntMan;

import inputClasses.JavaLabel;
import passwords.Password;

public class startClass {

	public static void main(String[] args) {
		JavaLabel.setupButtonImages();
		new Password();
		new MainMenu();
	}
}
