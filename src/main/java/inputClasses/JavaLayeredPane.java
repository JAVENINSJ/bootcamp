package inputClasses;

import java.util.HashMap;

import javax.swing.*;

public class JavaLayeredPane extends JLayeredPane {

	private static final long serialVersionUID = 1L;

	public String name;
	int xStart, yStart;
	public boolean selected;

	@SuppressWarnings("unchecked")
	public JavaLayeredPane(String name, Object location, int xStart, int yStart, int width, int height, Object list,
			Integer layer) {
		super();
		this.name = name;
		this.xStart = xStart;
		this.yStart = yStart;
		this.setBounds(xStart, yStart, width, height);
		this.selected = false;
		try {
			((JFrame) location).add(this);
		} catch (Exception e) {
			((JavaLayeredPane) location).add(this, layer);
		}
		((HashMap<String, JavaLayeredPane>) list).put(name, this);
	}
}