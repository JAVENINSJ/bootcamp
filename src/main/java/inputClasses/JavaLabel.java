package inputClasses;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;
//import java.awt.Image; WILL BE NEEDED FOR SCALING

public class JavaLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	public static File fileRoute = new File(System.getProperty("user.dir"));
	public static String routeButtons = fileRoute + "//Buttons//";

	public String name;
	public boolean inZone;
	public boolean selected;
	static ImageIcon[] buttonIcon = new ImageIcon[3];
	public int startX, startY;

	public static void setupButtonImages() {
		buttonIcon[0] = new ImageIcon(routeButtons + "button.png");
		buttonIcon[1] = new ImageIcon(routeButtons + "buttonOn.png");
		buttonIcon[2] = new ImageIcon(routeButtons + "buttonPressed.png");
	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height,
			HashMap<String, JavaLabel> list) {// CONSTRUCTS BUTTON
		super(name, buttonIcon[0], JLabel.CENTER);
		this.name = name;
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setFont(new Font("Verdana", Font.BOLD, 42));
		this.setForeground(Color.white);
		this.setBounds(x, y, width, height);
		this.setIcon(buttonIcon[0]);
		this.inZone = false;
		this.selected = false;
		try {
			((JavaLayeredPane) location).add(this, (Integer) 20);
		} catch (Exception e) {
			((JFrame) location).add(this);
		}
		new Mouse(this);
		list.put(name, this);
	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height, HashMap<String, JavaLabel> list,
			Integer layer, String folderRoute) {// CONSTRUCTS JLABEL
		super(new ImageIcon(fileRoute + folderRoute + name + ".png"), JLabel.CENTER);
		this.name = name;
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setFont(new Font("Verdana", Font.BOLD, 16));
		this.setForeground(Color.white);
		this.setBounds(x, y, width, height);
		this.startX = x;
		this.startY = y;
		Graphics2D g = (Graphics2D) this.getGraphics();
		
		try {
			((JavaLayeredPane) location).add(this, layer);
		} catch (Exception e) {
			((JLabel) location).add(this);
		}
		list.put(name, this);
		// NEXT LINE WILL BE FOR SCALING
		//this.setIcon(new ImageIcon(new javax.swing.ImageIcon(fileRoute + folderRoute + name + ".png").getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH)));

	}

	public void setIcon(int iconNumber, boolean inZone) {
		this.setIcon(buttonIcon[iconNumber]);
		this.inZone = inZone;
	}
}