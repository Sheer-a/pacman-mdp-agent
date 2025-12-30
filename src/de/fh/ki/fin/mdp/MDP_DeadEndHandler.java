package de.fh.ki.fin.mdp;

import java.util.*;
import de.fh.kiServer.util.Vector2;
import de.fh.pacman.enums.PacmanTileType;

public class MDP_DeadEndHandler {
	
	private static MDP_DeadEndHandler handler;
	private int[][] deadEndMap;
	private ArrayList<ArrayList<Vector2>> deadEnds;
	private enum Direction {
		NORTH, EAST, SOUTH, WEST
	}
		
	private MDP_DeadEndHandler() { }
	
	public static void createHandler() {
		handler = new MDP_DeadEndHandler();		
	}
	
	public static MDP_DeadEndHandler getHandler() {
		return handler;
	}
	
	public static void setView(PacmanTileType[][] view) {
		if(handler == null)
			createHandler();
		handler.deadEndMap = new int[view[0].length][view.length];	
		handler.deadEnds = new ArrayList<ArrayList<Vector2>>();;
		handler.analyzeView(view);		
	}
	
	/*
	 * Gibt eine zweidimensionale ganzzahlige Matrix zurück, mit der Berücksichtigung das jedes Feld
	 * in der Pacman-Welt nur die Werte := {-1, 0, 1, 2, 3} akzeptieren kann.
	 * Bedeutung der einzelnen Werte:
	 * -1	Kein Eingang
	 * 0	Keine Wand
	 * 1	Eine Wand
	 * ...
	 */
	public int[][] getDeadEndMap() {
		return deadEndMap;
	}
	
	// Gibt alle Sackgassen der Pacman-Welt zurück.
	public static ArrayList<ArrayList<Vector2>> getDeadEnds() {
		return handler.deadEnds;
	}
	
	// Lifert den Weg bis zu einer Sackgasse zurück, wenn es einen gibt.
	public ArrayList<Vector2> getDeadEnd(Vector2 v) {
		if(deadEnds != null)
			for (ArrayList<Vector2> deadEnd : deadEnds)
	            for (Vector2 field : deadEnd)
	                if(v.getX() == field.getX() && v.getY() == field.getY())
	                	return deadEnd;	            			
		return null;
	}
	
	// Gibt den Eingang der Sackgasse zurück.
	public Vector2 getEntranceOfDeadEnd(MDP_Field mdpField) {
		ArrayList<Vector2> list = getDeadEnd(mdpField.getVectorR());
		return list.get(list.size()-1);	
	}
	
	// Gibt das Ende der Sackgasse zurück.
	public Vector2 getDeadEndEnd(ArrayList<Vector2> deadEnd) {
		for(Vector2 v : deadEnd)
			if(deadEndMap[v.getX()][v.getY()] == 3)
				return v;
		return null;
	}
	
	// Ist v ein Tupel, welches eine Sackgasse gehört?
	public boolean isPartOfDeadEnd(Vector2 v) {
		if (!deadEnds.isEmpty())
	    	for(ArrayList<Vector2> deadEnd : deadEnds)
	            for (Vector2 field : deadEnd)
	                if (v.getX()==field.getX() && v.getY()==field.getY())
	                    return true;
        return false;
    }
	
	private void analyzeView(PacmanTileType[][] view) {
		for(int i=0; i<view[0].length; i++) {
			for(int j=0; j<view.length; j++) {
				if(view[i][j] == PacmanTileType.WALL)
					deadEndMap[j][i] = -1;
				else
					deadEndMap[j][i] = surroundedWalls(i, j, view);
			}
		}
		initDeadEnds(view);		
	}
	
	private void initDeadEnds(PacmanTileType[][] view) {
		for(int i=0; i<view.length; i++) 
			for(int j=0; j<view[i].length; j++)
				if (deadEndMap[i][j]==3) {
					ArrayList<Vector2> deadEnd = new ArrayList<Vector2>();
					Vector2 deadEndEnd = new Vector2(i,j);
					iterateToExit(i, j, deadEndEnd, deadEnd);
					deadEnds.add(deadEnd);
				}
	}
	
    private void iterateToExit(int i, int j, Vector2 deadEndEnd, ArrayList<Vector2> deadEnd) {
    	deadEnd.add(deadEndEnd);
    	if(deadEndMap[i-1][j] == 2)
    		iterateRec(i-1, j, Direction.NORTH, deadEnd);
    	if(deadEndMap[i][j+1] == 2)
    		iterateRec(i, j+1, Direction.EAST, deadEnd);
    	if(deadEndMap[i+1][j] == 2)
    		iterateRec(i+1, j, Direction.SOUTH, deadEnd);
    	if(deadEndMap[i][j-1] == 2)
    		iterateRec(i, j-1, Direction.WEST, deadEnd);
	}
    
    private void iterateRec(int i, int j, Direction dirc, ArrayList<Vector2> deadEnd) {
    	deadEnd.add(new Vector2(i,j));
    	if(dirc != Direction.SOUTH && deadEndMap[i-1][j] == 2)
    		iterateRec(i-1, j, Direction.NORTH, deadEnd);
    	if(dirc != Direction.WEST && deadEndMap[i][j+1] == 2)
    		iterateRec(i, j+1, Direction.EAST, deadEnd);
    	if(dirc != Direction.NORTH && deadEndMap[i+1][j] == 2)
    		iterateRec(i+1, j, Direction.SOUTH, deadEnd);
    	if(dirc != Direction.EAST && deadEndMap[i][j-1] == 2)
    		iterateRec(i, j-1, Direction.WEST, deadEnd);
    }

	private int surroundedWalls(int i, int j, PacmanTileType[][] view) {     
		int result = 0;
        if(view[i+1][j] == PacmanTileType.WALL)
        	result++;
        if(view[i-1][j] == PacmanTileType.WALL)
        	result++;
        if(view[i][j+1] == PacmanTileType.WALL)
        	result++;
        if(view[i][j-1] == PacmanTileType.WALL)
        	result++;
        return result;
	}

}
