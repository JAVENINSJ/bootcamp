package stuntMan;

import inputClasses.Audio;
import inputClasses.JavaLabel;
import passwords.Password;

public class startClass {

	public static void main(String[] argv) {
		Password.getLoginFile();
		Audio.play(JavaLabel.fRoute + "//happyRock.wav");
		new GameScreen();
		new MainMenu();
	}
}
