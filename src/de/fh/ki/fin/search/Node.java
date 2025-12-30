package de.fh.ki.fin.search;

import java.util.*;

import de.fh.pacman.enums.PacmanTileType;

public class Node {
	private Node prev;
	private int posX;
	private int posY;
	private int steps;
	
	public Node(int posX, int posY, int steps) {
		this();
		this.posX = posX;
		this.posY = posY;
		this.steps = steps;
	}
	
	public Node(int posX, int posY) {
		this(posX, posY, 0);
	}
	
	public Node() {
		super();
	}
	
	public Node getPrev()	{ return prev;	}
	public int getPosX()	{ return posX;	}
	public int getPosY()	{ return posY;	}
	public int getSteps()	{ return steps;	}
	
	public boolean isTarget() {
		return posX == UCS.end.posX && posY == UCS.end.posY;
	}
	
	public List<Node> expand() {
		
		ArrayList<Node> children = new ArrayList<Node>();
		
		if(UCS.map[posX+1][posY] != PacmanTileType.WALL)
			children.add(generateChild(posX+1, posY));
		if(UCS.map[posX-1][posY] != PacmanTileType.WALL)
			children.add(generateChild(posX-1, posY));
		if(UCS.map[posX][posY+1] != PacmanTileType.WALL)
			children.add(generateChild(posX, posY+1));
		if(UCS.map[posX][posY-1] != PacmanTileType.WALL)
			children.add(generateChild(posX, posY-1));

		return children;
	}
	
	private Node generateChild(int posX, int posY) {
		return new Node(posX, posY, this.steps+1);
	}

	@Override
	public String toString() {
		return String.format("((%d, %d), %d)", posX, posY, steps);
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj != null && this.getClass() == obj.getClass()) {
			Node node = (Node) obj;
			if(this == node || (this.posX == node.posX && this.posY == node.posY)) {
				return true;
			}
		}
		return false;
	}
}
