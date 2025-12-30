package de.fh.ki.fin.mdp;

import de.fh.pacman.enums.PacmanAction;
//import de.fh.ki.fin.util.Logging;

public class MDP_Map {

	private double[][] values;
	
	public MDP_Map(double[][] values) {
		this.values = values;
	}
	
	public void setValues(double[][] values) {
		this.values = values;
	}
	
	public double[][] getValues() {
		return values;
	}
	
	public void setValue(double v, int x, int y) {
		if(x>=0 && x<values.length && y>=0 && y<values[0].length)
			values[x][y] = v;
	}
	
	public double getValue(int x, int y) {
		if(x>=0 && x<values.length && y>=0 && y<values[x].length)
			return values[x][y];
		else
			return MDP_ValueManager.BLOCKED;
	}
	
	public double getNeighbourAvgValue(int i, int j) {
		int neighbours = 0;
		double accumlatedNeighbourValue = 0.0d;
		double[] neighbourValues = {
				getValue(i+1, j),
				getValue(i-1, j),
				getValue(i, j+1),
				getValue(i, j-1)
		};
		for(double v : neighbourValues)
			if(!(v == MDP_ValueManager.BLOCKED || v == MDP_ValueManager.PACMAN)) {
				neighbours++;
				accumlatedNeighbourValue += v;
			}	
		return (neighbours == 0) ? 0 : (accumlatedNeighbourValue/neighbours);
	}
	
	public PacmanAction getActionToBestNeighbour(int x, int y) {
		PacmanAction result = PacmanAction.WAIT;
		double max = getValue(y,x);
		// if(Logging.ON) System.out.println("TO GO_EAST: "+getValue(y, x+1));
        if(getMax(max, y, x+1) > max) {
            max = getValue(y, x+1);
            result = PacmanAction.GO_EAST;
        }
		// if(Logging.ON) System.out.println("TO GO_WEST: "+getValue(y, x-1));
        if(getMax(max, y, x-1) > max) {
            max = getValue(y, x-1);
            result = PacmanAction.GO_WEST;
        }
		// if(Logging.ON) System.out.println("TO GO_SOUTH: "+getValue(y+1, x));
        if(getMax(max, y+1, x) > max) {
            max = getValue(y+1, x);
            result = PacmanAction.GO_SOUTH;
        }
		// if(Logging.ON) System.out.println("TO GO_NORTH: "+getValue(y-1, x));
        if(getMax(max, y-1, x) > max) {
            max = getValue(y-1, x);
            result = PacmanAction.GO_NORTH;
        }
        return result;
	}

	private double getMax(double max, int x, int y) {
		return Double.max(getValue(x,y), max);
	}
	
	public static void printMap(int iterationNum, MDP_Map map) {
		System.out.println("\nIteration " + iterationNum + ":\n");
        for(int i=0; i < map.values.length; ++i) {
            for(int j=0; j < map.values[i].length; ++j) {
                if(map.values[i][j] == MDP_ValueManager.BLOCKED)
                    System.out.print("<BLOCKED>\t");
                else if (map.values[i][j] == MDP_ValueManager.PACMAN)
                    System.out.print("<PACMAN>\t");
                else 
                	System.out.printf("<%+02.3f>\t", map.values[i][j]);
            }
            System.out.println();
        }
        System.out.println();
	}
}
