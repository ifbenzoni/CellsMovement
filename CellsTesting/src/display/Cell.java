package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * cell of the grid, contains functions to set and reset shape and color
 */
public class Cell extends JPanel {
	
	public final static Color BACKGROUND = Color.WHITE;

	/**
	 * default id
	 */
	private static final long serialVersionUID = 1L;
	private int shape = -1;
	private Color color;
	private int width;
	private int height;
	private State state;
	
	public enum State {
		TARGET,
		LIVE
	}
	
	public Cell(int width, int height) {
		this.width = width;
		this.height = height;
		shape = -1;
		color = BACKGROUND;
		state = null;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		if (shape == -1) {
			g.drawRect(0, 0, width, height);
			g.fillRect(0, 0, width, height);
		} else if (shape == 0) {
			g.drawOval(0, 0, width, height);
			g.fillOval(0, 0, width, height);
		} else if (shape == 1) {
			g.drawRect(0, 0, width, height);
			g.fillRect(0, 0, width, height);
		}
	}
	
	public void setShape(int shape) {
		if (shape >= 0 && shape <= 1) {
			this.shape = shape;
		}
	}
	
	public int getShape() {
		return shape;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setState(State state) {
		this.state = state;
		if (state == State.TARGET) {
			setShape(0);
			setColor(Color.RED);
			this.repaint();
		} else if (state == State.LIVE) {
			setShape(1);
			setColor(Color.BLACK);
			this.repaint();
		} else {
			setShape(-1);
			setColor(BACKGROUND);
			this.repaint();
		}
	}
	
	public State getState() {
		return state;
	}
	
	public void reset() {
		setShape(-1);
		setColor(BACKGROUND);
		setState(null);
		this.repaint();
	}
	
	public void setAll(int shape, Color color, State state) {
		setShape(shape);
		setColor(color);
		setState(state);
		this.repaint();
	}
}
