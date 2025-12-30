package de.fh.stud.p2;

import java.util.*;

import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanTileType;

public class Knoten {
	
	private Knoten previous;
	private int pacmanPosX;
	private int pacmanPosY;
	private int turn;
	private PacmanTileType[][] map;
	
	public Knoten(PacmanTileType[][] map, Knoten previous, int pacmanPosX, int pacmanPosY, int turn) {
		this.previous = previous;
		this.pacmanPosX = pacmanPosX;
		this.pacmanPosY = pacmanPosY;
		this.turn = turn;
		this.map = map;
	}
	
	
	public Knoten getPrevious() 		{ return previous; }
	public int getPacmanPosX() 			{ return pacmanPosX; }
	public int getPacmanPosY() 			{ return pacmanPosY; }
	public int getTurn() 				{ return turn; }
	public PacmanTileType[][] getMap()	{ return map; }
	
	public List<Knoten> expand() {

		ArrayList<Knoten> children = new ArrayList<Knoten>();
		
		if(map[pacmanPosX+1][pacmanPosY] != PacmanTileType.WALL)
			children.add(generateChild(PacmanAction.GO_EAST, pacmanPosX+1, pacmanPosY, turn+1));

		if(map[pacmanPosX-1][pacmanPosY] != PacmanTileType.WALL)
			children.add(generateChild(PacmanAction.GO_WEST, pacmanPosX-1, pacmanPosY, turn+1));
		
		if(map[pacmanPosX][pacmanPosY+1] != PacmanTileType.WALL)
			children.add(generateChild(PacmanAction.GO_SOUTH, pacmanPosX, pacmanPosY+1, turn+1));

		if(map[pacmanPosX][pacmanPosY-1] != PacmanTileType.WALL)
			children.add(generateChild(PacmanAction.GO_NORTH, pacmanPosX, pacmanPosY-1, turn+1));

		return children;
	}
	
	private Knoten generateChild(PacmanAction action, int pacmanNewPosX, int pacmanNewPosY, int turn) {	
		
		PacmanTileType[][] map = new PacmanTileType[this.map.length][this.map[0].length];
		
		int i, j;
		for(i=0; i<map.length; i++)
			for(j=0; j<map[0].length; j++)
				map[i][j] = this.map[i][j];
		
		map[pacmanPosX][pacmanPosY] = PacmanTileType.EMPTY;

		return new Knoten(map, this, pacmanNewPosX, pacmanNewPosY, turn);
	}

	public boolean isTarget() {
		boolean result = true;
		int i, j;
		for(i=0; i<map.length; i++)
			if(result)
				for(j=0; j<map[i].length; j++)
					if(map[i][j]==PacmanTileType.DOT) {
						result = false;
						break;
					}
		return result;
	}
	
	public Stack<PacmanAction> getActionStack() {
		Stack<PacmanAction> result = new Stack<PacmanAction>();
		Knoten k = this;
		Knoten pre = this.previous;
		while(pre != null) {
			
			int x_axis = k.pacmanPosX - pre.pacmanPosX;
			int y_axis = k.pacmanPosY - pre.pacmanPosY;
			
			PacmanAction action = PacmanAction.WAIT;
			if(x_axis != 0)
				action = x_axis>0 ? PacmanAction.GO_EAST : PacmanAction.GO_WEST;
			
			if(y_axis != 0)
				action = y_axis>0 ? PacmanAction.GO_SOUTH : PacmanAction.GO_NORTH;				
			
			result.push(action);
				
			k = pre;
			pre = pre.previous;
		}
		return result;
	}
	
	public int getNumDots() {
		int count = 0;
		int i, j;
		for(i=0; i<map.length; i++)
			for(j=0; j<map[i].length; j++)
				if(map[i][j] == PacmanTileType.DOT)
					count++;
				
		return count;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		Knoten k = (Knoten) o;
				
		if(pacmanPosX != k.pacmanPosX || pacmanPosY != k.pacmanPosY)
			return false;
		
		int i, j;
		for(i=0; i<map.length; i++) {
			for(j=0; j<map[0].length; j++)
				if(this.map[i][j] != k.map[i][j])
					return false;
			
		}			
		return true;
	}	
	
	@Override
	public String toString() {
		String result = "";
		int i, j;
		for(i=0; i<map.length; i++) {	
			for(j=0; j<map[0].length; j++)
				result += map[i][j] + " ";
			
			result += "\n";
		}		
		return result;		
	}
}