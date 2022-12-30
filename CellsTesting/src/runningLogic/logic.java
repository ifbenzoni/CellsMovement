package runningLogic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import display.*;
import display.CellDisplay.*;
import java.util.*;
import java.util.Map.Entry;

public class logic {
	
	/**
	 * time between updates in milliseconds
	 */
	private static final int DELAY = 100;
	
	private static final Random RANDOM = new Random();
	
	/**
	 * dimension for square of live cells
	 */
	private static final int SIZE = 6;
	
	public static void run(Container c, GridVals gridVals) {
		Cell[][] cellArray = CellDisplay.createArray(gridVals.getWidthArr(), gridVals.getHeightArr(), 
				gridVals.getWidthCell(), gridVals.getHeightCell(), c, true);
		Cell.State[][] stateArray1 = new Cell.State[gridVals.getWidthArr()][gridVals.getHeightArr()];
		Cell.State[][] stateArray2 = new Cell.State[gridVals.getWidthArr()][gridVals.getHeightArr()];
		int[] averagePos = {0, 0};
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				cellArray[i][j].setState(Cell.State.LIVE);
				stateArray1[i][j] = Cell.State.LIVE;
				averagePos[0] += i;
				averagePos[1] += j;
			}
		}
		averagePos[0] /= SIZE * SIZE;
		averagePos[1] /= SIZE * SIZE;
		Map<String, int[]> targetPosMap = new HashMap<String, int[]>();
		update(cellArray, stateArray1, stateArray2, c, targetPosMap, averagePos, gridVals);
	}
	
	public static void update(Cell[][] cellArray, Cell.State[][] stateArray1, Cell.State[][] stateArray2, 
			Container c, Map<String, int[]> targetPosMap, int[] averagePos, GridVals gridVals) {
		c.setVisible(false);
		//add new targets to array
		for (int i = 0; i < cellArray.length; i++) {
			for (int j = 0; j < cellArray[i].length; j++) {
				if (cellArray[i][j].getState() == Cell.State.TARGET) {
					stateArray1[i][j] = Cell.State.TARGET;
					targetPosMap.put(String.valueOf(i) + String.valueOf(j), new int[]{i, j});
				}
			}
		}
		if (!targetPosMap.isEmpty()) {
			averagePos = new int[2];
		}
		//update array
		for (int i = 0; i < cellArray.length; i++) {
			for (int j = 0; j < cellArray[i].length; j++) {
				//persist if not overwritten
				if (stateArray1[i][j] == Cell.State.TARGET && stateArray2[i][j] == null) {
					stateArray2[i][j] = Cell.State.TARGET;
				}
				if (stateArray1[i][j] == Cell.State.LIVE) {
					//move towards target if exists
					if (!targetPosMap.isEmpty()) {
						int[] loc = {i, j};
						double[] unitDir = VectorFunctions.findUnitDirMap(loc, targetPosMap);
						double[] finalDir = VectorFunctions.findFinalDir(unitDir);
						double distToTarget = -1;
						for (Entry<String, int[]> pos : 
							targetPosMap.entrySet()) {
							double dist = Math.sqrt((double) Math.pow(pos.getValue()[0] - loc[0], 2) + 
									(double) Math.pow(pos.getValue()[1] - loc[1], 2));
							if (distToTarget == -1 || dist < distToTarget) {
								distToTarget = dist;
							}
						}
						finalDir = targetingMovement(finalDir, distToTarget, gridVals);
						//make sure not overwriting a live cell
						if (i + (int) finalDir[0] >= 0 && i + (int) finalDir[0] < cellArray.length &&
								j + (int) finalDir[1] >= 0 && j + (int) finalDir[1] < cellArray[0].length &&
								stateArray1[i + (int) finalDir[0]][j + (int) finalDir[1]] != Cell.State.LIVE &&
								stateArray2[i + (int) finalDir[0]][j + (int) finalDir[1]] != Cell.State.LIVE) {
							stateArray2[i + (int) finalDir[0]][j + (int) finalDir[1]] = Cell.State.LIVE;
							averagePos[0] += i + (int) finalDir[0];
							averagePos[1] += j + (int) finalDir[1];
						} else {
							stateArray2[i][j] = Cell.State.LIVE;
							averagePos[0] += i;
							averagePos[1] += j;
						}
					} else { //else move towards average position
						int[] loc = {i, j};
						double[] unitDir = VectorFunctions.findUnitDirPos(loc, averagePos);
						double[] finalDir = VectorFunctions.findFinalDir(unitDir);
						int distX = averagePos[0] - i; 
						int distY = averagePos[1] - j;
						finalDir = idleMovement(finalDir, distX, distY);
						//make sure not overwriting a live cell
						if (i + (int) finalDir[0] >= 0 && i + (int) finalDir[0] < cellArray.length &&
								j + (int) finalDir[1] >= 0 && j + (int) finalDir[1] < cellArray[0].length &&
								stateArray1[i + (int) finalDir[0]][j + (int) finalDir[1]] != Cell.State.LIVE &&
								stateArray2[i + (int) finalDir[0]][j + (int) finalDir[1]] != Cell.State.LIVE) {
							stateArray2[i + (int) finalDir[0]][j + (int) finalDir[1]] = Cell.State.LIVE;
						} else {
							stateArray2[i][j] = Cell.State.LIVE;
						}
					}
				}
			}
		}
		if (!targetPosMap.isEmpty()) {
			averagePos[0] /= SIZE * SIZE;
			averagePos[1] /= SIZE * SIZE;
		}
		//update graphics based on array values
		for (int i = 0; i < cellArray.length; i++) {
			for (int j = 0; j < cellArray[i].length; j++) {
				if (stateArray2[i][j] != null) {
					cellArray[i][j].setState(stateArray2[i][j]);
				} else {
					cellArray[i][j].reset();
				}
				//reset initial array for reuse
				stateArray1[i][j] = null;
			}
		}
		targetPosMap.clear();
		c.setVisible(true);
		//swaps arrays each recursive call
		Timer t = new Timer(DELAY, new updateRunningActionListener(cellArray, stateArray2, stateArray1, c, 
				targetPosMap, averagePos, gridVals));
		t.setRepeats(false);
		t.start();
	}
	
	//for recursion
	static class updateRunningActionListener implements ActionListener {
		private Cell[][] cellArray;
		private Cell.State[][] stateArray1;
		private Cell.State[][] stateArray2;
		Container c;
		Map<String, int[]> targetPosMap;
		int[] averagePos;
		GridVals gridVals;
		
		public updateRunningActionListener(Cell[][] cellArray, Cell.State[][] stateArray1, 
				Cell.State[][] stateArray2, Container c, Map<String, int[]> targetPosMap, int[] averagePos, GridVals gridVals) {
			this.cellArray = cellArray;
			this.stateArray1 = stateArray1;
			this.stateArray2 = stateArray2;
			this.c = c;
			this.targetPosMap = targetPosMap;
			this.averagePos = averagePos;
			this.gridVals = gridVals;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			update(cellArray, stateArray1, stateArray2, c, targetPosMap, averagePos, gridVals);
		}
	}
	
	/**
	 * function to alter movement when idle
	 */
	private static double[] idleMovement(double[] dir, int distX, int distY) {
		//rotation values using rotation matrix
		double rotateX = dir[0] * Math.cos(Math.PI / 4) + dir[1] * Math.sin(Math.PI / 4);
		double rotateY = dir[0] * -Math.sin(Math.PI / 4) + dir[1] * Math.cos(Math.PI / 4);
		int r1 = RANDOM.nextInt(3) - 1;
		int r2 = RANDOM.nextInt(3) - 1;
		//movement alterations
		double alterationX;
		double alterationY;
		//returns to square
		if (SIZE % 2 == 1) {
			if (Math.abs(distX) > SIZE / 2 || Math.abs(distY) > SIZE / 2) {
				alterationX = dir[0] + r1 * 2 + rotateX;
				alterationY = dir[1] + r2 * 2 + rotateY;
			} else {
				alterationX = dir[0] * 2 + r1;
				alterationY = dir[1] * 2 + r2;
			}
		} else {
			if (Math.abs(distX) > SIZE / 2 || Math.abs(distY) > SIZE / 2 || 
					(distX > 0 && distX > SIZE / 2 - 1) || 
					(distY > 0 &&  distY > SIZE / 2 - 1)) {
				alterationX = dir[0] + r1 * 2 + rotateX;
				alterationY = dir[1] + r2 * 2 + rotateY;
			} else {
				alterationX = dir[0] * 2 + r1;
				alterationY = dir[1] * 2 + r2;
			}
		}
		double alterationVec[] = {alterationX, alterationY};
		alterationVec = VectorFunctions.convertToUnitVector(alterationVec);
		dir = VectorFunctions.findFinalDir(alterationVec);
		return dir;
	}
	
	//movement alterations towards target
	private static double[] targetingMovement(double dir[], double dist, GridVals gridVals) {
		//max dist on screen from corner to corner
		double maxDist = Math.sqrt(Math.pow(gridVals.getWidthArr(), 2) + Math.pow(gridVals.getHeightArr(), 2));
		int r1 = RANDOM.nextInt(3) - 1;
		int r2 = RANDOM.nextInt(3) - 1;
		//movement alterations
		double alterationX;
		double alterationY;
		
		//input value for sine that changes with position
		double sineInput = dist / maxDist * Math.PI * 8;
		double sineValue = Math.sin(sineInput) * 5;
		double[] sineXY = {sineInput, sineValue};
		//input for relative sine that is nearby, will help calculate approximate current sine direction
		double relativeSineInput = dist / maxDist * Math.PI * 8 - Math.PI / 100;
		double relativeSineValue = Math.sin(relativeSineInput) * 5;
		double[] relativeXY = {relativeSineInput, relativeSineValue};
		
		//current dir of sine function based on direction from 'relative sine' to 'sine' position
		double[] sineDir = VectorFunctions.findUnitDirPosDoubles(relativeXY, sineXY);
		//average dir of my sine function
		double[] baseSineDir = {1, 0};
		
		//angle of rotation calculation
		double dotProduct = dir[0] * baseSineDir[0] + dir[1] * baseSineDir[1];
		double lengthProduct = Math.sqrt(Math.pow(dir[0], 2) + Math.pow(dir[1], 2)) * 1;
		double angle = Math.acos(dotProduct / lengthProduct);
		
		//temp values used to calculate rotated sine
		double tempSineX = sineDir[0];
		double tempSineY = sineDir[1];
		//arccos only goes from 0 to 180 so need to need to rotate in opposing dir depending on y
		if (dir[1] == 1) {
			sineDir[0] = tempSineX * Math.cos(angle) - tempSineY * Math.sin(angle);
			sineDir[1] = tempSineX * Math.sin(angle) + tempSineY * Math.cos(angle);
		} else {
			sineDir[0] = tempSineX * Math.cos(angle) + tempSineY * Math.sin(angle);
			sineDir[1] = -tempSineX * Math.sin(angle) + tempSineY * Math.cos(angle);
		}
		
		//combine pieces of alteration vector
		alterationX = (dir[0] + sineDir[0]) * 2 + r1;
		alterationY = (dir[1] + sineDir[1]) * 2 + r2;
		double alterationVec[] = {alterationX, alterationY};
		//convert alteration vector to unit vector
		alterationVec = VectorFunctions.convertToUnitVector(alterationVec);
		dir = VectorFunctions.findFinalDir(alterationVec);
		return dir;
	}

}
