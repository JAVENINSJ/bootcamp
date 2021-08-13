package stuntMan;

import ioClasses.Audio;
import ioClasses.JavaLabel;
import passwords.Password;

public class startClass {

	public static void main(String[] argv) {
		Password.getLoginFile();
		Audio.play(JavaLabel.fRoute + "//happyRock.wav"); //TODO REMOVE "//" WHEN YOU WANT MUSIC
		new GameScreen();
		new MainMenu();
	}
}
