package inputClasses;

import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;

public class JavaLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	public static File fileRoute = new File(System.getProperty("user.dir"));
	public static String routeButtons = fileRoute + "//Buttons//";

	public String name;
	public boolean inZone;
	public boolean selected;
	static ImageIcon[] buttonIcon = new ImageIcon[3];

	public static void setupButtonImages() {
		buttonIcon[0] = new ImageIcon(routeButtons + "button.png");
		buttonIcon[1] = new ImageIcon(routeButtons + "buttonOn.png");
		buttonIcon[2] = new ImageIcon(routeButtons + "buttonPressed.png");
	}

	public JavaLabel(String name, JLayeredPane location, int x, int y, int width, int height,
			HashMap<String, JavaLabel> list) {// CONSTRUCTS BUTTON
		super(name, buttonIcon[0], JLabel.CENTER);
		this.name = name;
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setFont(new Font("Verdana", Font.BOLD, 42));
		this.setBounds(x, y, width, height);
		this.setIcon(buttonIcon[0]);
		this.inZone = false;
		this.selected = false;
		location.add(this, (Integer) 20);
		new Mouse(this);
		list.put(name, this);
	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height, HashMap<String, JavaLabel> list,
			Integer layer, String folderRoute) {// CONSTRUCTS JLABEL
		super(new ImageIcon(fileRoute + folderRoute + name + ".png"), JLabel.CENTER);
		this.name = name;
		this.setBounds(x, y, width, height);
		try {
			((JavaLayeredPane) location).add(this, layer);
		} catch (Exception e) {
			((JLabel) location).add(this);
		}
		list.put(name, this);
	}

	public void setIcon(int iconNumber, boolean inZone) {
		this.setIcon(buttonIcon[iconNumber]);
		this.inZone = inZone;
	}
}