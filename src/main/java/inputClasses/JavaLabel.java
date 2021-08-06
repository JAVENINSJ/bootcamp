package inputClasses;

<<<<<<< HEAD
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
=======
import java.awt.*;
>>>>>>> 79c70151d94ece389776283a3139832310561c27
import java.io.File;
import java.util.HashMap;
import javax.swing.*;

public class JavaLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	public static File fRoute = new File(System.getProperty("user.dir"));
	public static String routeButtons = fRoute + "//Buttons//";

	public String name;
	public boolean inZone;
	public boolean selected;
	public ImageIcon[] buttonIcon = new ImageIcon[3];
	public int startX, startY;

	public static void setupButtonImages() {

	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height,
			HashMap<String, JavaLabel> list) {// CONSTRUCTS BUTTON
		super(name);
		this.buttonIcon[0] = new ImageIcon(new ImageIcon(routeButtons + "button.png").getImage()
				.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		this.buttonIcon[1] = new ImageIcon(new ImageIcon(routeButtons + "buttonOn.png").getImage()
				.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		this.buttonIcon[2] = new ImageIcon(new ImageIcon(routeButtons + "buttonPressed.png").getImage()
				.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		this.setIcon(buttonIcon[0]);
		this.inZone = false;
		this.selected = false;
		try {
			((JavaLayeredPane) location).add(this, (Integer) 20);
		} catch (Exception e) {
			((JFrame) location).add(this);
		}
		new Mouse(this);
		this.setFont(new Font("Verdana", Font.BOLD, 46));
		addCommonProperties(name, x, y, width, height, list);
	}

	public JavaLabel(String name, Object location, int x, int y, int width, int height, HashMap<String, JavaLabel> list,
			Integer layer, String fPath, boolean setText) {// CONSTRUCTS JLABEL
		super(new ImageIcon(new ImageIcon(fRoute + fPath + name + ".png").getImage().getScaledInstance(width, height,
				Image.SCALE_SMOOTH)), JLabel.CENTER);
		if (setText) {
			this.setText(name);
		}
		this.startX = x;
		this.startY = y;
		Graphics2D g = (Graphics2D) this.getGraphics();
		
		try {
			((JavaLayeredPane) location).add(this, layer);
		} catch (Exception e) {
			((JLabel) location).add(this);
		}
		this.setFont(new Font("Verdana", Font.BOLD, 16));
		addCommonProperties(name, x, y, width, height, list);
	}

	public void addCommonProperties(String name, int x, int y, int width, int height, HashMap<String, JavaLabel> list) {
		this.name = name;
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setForeground(Color.white);
		this.setBounds(x, y, width, height);
		list.put(name, this);
	}

	public void setIcon(int iconNumber, boolean inZone) {
		this.setIcon(buttonIcon[iconNumber]);
		this.inZone = inZone;
	}
}