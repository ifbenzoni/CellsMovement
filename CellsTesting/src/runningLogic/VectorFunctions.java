package runningLogic;

import java.util.Map;
import java.util.Map.Entry;

public class VectorFunctions {
	
	/**
	finds unit direction to closest target, using map
	 */
	public static double[] findUnitDirMap(int[] loc, Map<String, int[]> targetPosMap) {
		int[] dest = new int[2];
		double minDist = -1;
		for (Entry<String, int[]> pos : 
			targetPosMap.entrySet()) {
			double dist = Math.sqrt((double) Math.pow(pos.getValue()[0] - loc[0], 2) + 
					(double) Math.pow(pos.getValue()[1] - loc[1], 2));
			if (minDist == -1 || dist < minDist) {
				minDist = dist;
				dest[0] = pos.getValue()[0];
				dest[1] = pos.getValue()[1];
			}
		}
		double[] dir = {((dest[0] - loc[0]) / 
				Math.sqrt(Math.pow(dest[0] - loc[0], 2) + Math.pow(dest[1] - loc[1], 2))), 
				((dest[1] - loc[1]) / 
						Math.sqrt(Math.pow(dest[0] - loc[0], 2) + Math.pow(dest[1] - loc[1], 2)))};
		return dir;
	}
	
	/**
	finds unit direction to closest target, using two positions
	 */
	public static double[] findUnitDirPos(int[] loc, int[] targetPos) {
		double[] dir = {((targetPos[0] - loc[0]) / 
				Math.sqrt(Math.pow(targetPos[0] - loc[0], 2) + Math.pow(targetPos[1] - loc[1], 2))), 
				((targetPos[1] - loc[1]) / 
						Math.sqrt(Math.pow(targetPos[0] - loc[0], 2) + Math.pow(targetPos[1] - loc[1], 2)))};
		if (Math.sqrt(Math.pow(targetPos[0] - loc[0], 2) + Math.pow(targetPos[1] - loc[1], 2)) == 0) {
			dir[0] = 0;
			dir[1] = 0;
		}
		return dir;
	}
	
	/**
	finds unit direction to closest target, using two positions (doubles)
	 */
	public static double[] findUnitDirPosDoubles (double [] loc, double[] targetPos) {
		double[] dir = {((targetPos[0] - loc[0]) / 
				Math.sqrt(Math.pow(targetPos[0] - loc[0], 2) + Math.pow(targetPos[1] - loc[1], 2))), 
				((targetPos[1] - loc[1]) / 
						Math.sqrt(Math.pow(targetPos[0] - loc[0], 2) + Math.pow(targetPos[1] - loc[1], 2)))};
		if (Math.sqrt(Math.pow(targetPos[0] - loc[0], 2) + Math.pow(targetPos[1] - loc[1], 2)) == 0) {
			dir[0] = 0;
			dir[1] = 0;
		}
		return dir;
	}
	
	/**
	converts unit dir to closest of 8 possible directions
	 */
	public static double[] findFinalDir(double[] unitDir) {
		double[] finalDir = {0, 0};
		double minAngle = -1;
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (!(x == 0 && y == 0)) {
					if (finalDir[0] == 0 && finalDir[1] == 0 || 
							Math.acos((unitDir[0] * x + unitDir[1] * y) / 
									Math.sqrt(Math.abs(x) + Math.abs(y))) < minAngle) {
						finalDir[0] = x;
						finalDir[1] = y;
						minAngle = Math.acos((unitDir[0] * x + unitDir[1] * y) / 
								Math.sqrt(Math.abs(x) + Math.abs(y)));
					}
				}
			}
		}
		if (unitDir[0] == 0 && unitDir[1] == 0) {
			finalDir[0] = 0;
			finalDir[1] = 0;
		}
		return finalDir;
	}
	
	public static double[] convertToUnitVector(double[] inputVec) {
		double[] unitVec = new double[2];
		unitVec[0] = inputVec[0] / Math.sqrt(Math.pow(inputVec[0], 2) + Math.pow(inputVec[1], 2));
		unitVec[1] = inputVec[1] / Math.sqrt(Math.pow(inputVec[0], 2) + Math.pow(inputVec[1], 2));
		return unitVec;
	}

}
