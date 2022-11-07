package display;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import runningLogic.logic;

public class CellDisplay extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	
	public CellDisplay() {
		Container c = getContentPane();
		c.setLayout(null);
		
		GridVals gridVals = new GridVals(10, 10, 80, 65);
		logic.run(c, gridVals);
		
		int screenWidth = gridVals.getWidthCell() * gridVals.getWidthArr() + 14;
		int screenHeight = gridVals.getHeightCell() * gridVals.getHeightArr() + 37;
		setSize(screenWidth, screenHeight); 
		addWindowListener(this);
		setVisible(true);
	}
	
	public static Cell[][] createArray(int widthArr, int heightArr, int widthCell, int heightCell, Container container, 
			boolean clickToggle) {
		Cell[][] ca = new Cell[widthArr][heightArr];
		Mouse mouse = new Mouse();
		for (int i = 0; i < ca.length; i++) {
			for (int j = 0; j < ca[i].length; j++) {
				ca[i][j] = new Cell(widthCell, heightCell);
				ca[i][j].setBackground(Cell.BACKGROUND);
				ca[i][j].setBounds(i * widthCell, j * heightCell, widthCell, heightCell);
				if (clickToggle) {
					ca[i][j].addMouseListener(mouse);
				}
				container.add(ca[i][j]);
			}
		}
		return ca;
	}
	
	private static class Mouse extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			Cell source = (Cell) e.getSource();
			if (source.getColor() == Cell.BACKGROUND) {
				source.setState(Cell.State.TARGET);
			} else if (source.getState() != Cell.State.LIVE) {
				source.setState(null);
			}
		}
	}
	
	public static class GridVals {
		private int widthCell = 0;
		private int heightCell = 0;
		private int widthArr = 0;
		private int heightArr = 0;
		
		public GridVals(int widthCell, int heightCell, int widthArr, int heightArr) {
			this.widthCell = widthCell;
			this.heightCell = heightCell;
			this.widthArr = widthArr;
			this.heightArr = heightArr;
		}
		
		public int getWidthCell() {
			return widthCell;
		}
		
		public int getHeightCell() {
			return heightCell;
		}
		
		public int getWidthArr() {
			return widthArr;
		}
		
		public int getHeightArr() {
			return heightArr;
		}
	}
	
	public static void main(String args[]) {
		@SuppressWarnings("unused") 
		CellDisplay s = new CellDisplay();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
