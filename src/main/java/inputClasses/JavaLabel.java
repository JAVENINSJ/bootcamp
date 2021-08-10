package inputClasses;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;

import stuntMan.GameScreen;
import stuntMan.MainMenu;

public class JavaLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	public static File fRoute = new File(System.getProperty("user.dir"));
	public static String routeButtons = fRoute + "//Buttons//";
	static int fWidth, fHeight;

	public String name;
	public boolean inZone;
	public boolean selected;
	public ImageIcon[] buttonIcon = new ImageIcon[3];
	public int startX, startY;

	public static void setSizing(int size) {
		fWidth = MainMenu.fWidth;
		fHeight = fWidth / 2;
	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height,
			HashMap<String, JavaLabel> list) {// CONSTRUCTS BUTTON
		super(name);
		this.buttonIcon[0] = new ImageIcon(new ImageIcon(routeButtons + "button.png").getImage()
				.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH));
		this.buttonIcon[1] = new ImageIcon(new ImageIcon(routeButtons + "buttonOn.png").getImage()
				.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH));
		this.buttonIcon[2] = new ImageIcon(new ImageIcon(routeButtons + "buttonPressed.png").getImage()
				.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH));
		this.setIcon(buttonIcon[0]);
		this.inZone = false;
		this.selected = false;
		try {
			((JavaLayeredPane) location).add(this, (Integer) 20);
		} catch (Exception e) {
			((JFrame) location).add(this);
		}
		new Mouse(this);
		this.setFont(new Font("Verdana", Font.BOLD, fWidth * 46 / 1000));
		addCommonProperties(name, x, y, width, height, list);
	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height, HashMap<String, JavaLabel> list,
			Integer layer, String fPath, boolean setText) {// CONSTRUCTS JLABEL
		super("", JLabel.CENTER);
		if (width == fWidth && height == fHeight) {
			this.setIcon(new ImageIcon(new ImageIcon(fRoute + fPath + name + ".png").getImage().getScaledInstance(width,
					height, Image.SCALE_SMOOTH)));
		} else {
			this.setIcon(new ImageIcon(new ImageIcon(fRoute + fPath + name + ".png").getImage()
					.getScaledInstance(fWidth * width / 1000, fWidth * height / 1000, Image.SCALE_SMOOTH)));
		}
		if (setText) {
			this.setText(name);
		}
		this.startX = x;
		this.startY = y;

		try {
			((JavaLayeredPane) location).add(this, layer);
		} catch (Exception e) {
			((JLabel) location).add(this);
		}
		this.setFont(new Font("Verdana", Font.BOLD, fWidth * 20 / 1000));
		addCommonProperties(name, x, y, width, height, list);
	}

	public void addCommonProperties(String name, int x, int y, int width, int height, HashMap<String, JavaLabel> list) {
		this.name = name;
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setForeground(Color.white);
		this.setBounds(fWidth * x / 1000, fHeight * y / 500, fWidth * width / 1000, fWidth * height / 1000);
		if (width == fWidth && height == fHeight) {
			this.setBounds(x, y, width, height);
		} else if (list.equals(GameScreen.objects) || name.equals("Player")) {
			this.setBounds(x, y,fWidth * width / 1000, fWidth * height / 1000);
		}
		System.out.println(this.name + " = " + this.getBounds());
		list.put(name, this);
	}

	public void setIcon(int iconNumber, boolean inZone) {
		this.setIcon(buttonIcon[iconNumber]);
		this.inZone = inZone;
	}
}