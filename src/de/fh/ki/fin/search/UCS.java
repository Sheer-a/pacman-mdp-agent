package de.fh.ki.fin.search;

import java.util.*;

import de.fh.pacman.enums.PacmanTileType;

public class UCS {
	
	public static Node start;
	public static Node end;
	public static Queue<Node> theOneQueue;
	public static List<Node> closedList;
	public static PacmanTileType[][] map;
	
	public static void setGoalPath(int posX, int posY, int goalX, int goalY, PacmanTileType[][] map) {
		UCS.map = map;
		UCS.start = new Node(posX, posY, 0);
		UCS.end = new Node(goalX, goalY, 0);
		UCS.theOneQueue = new LinkedList<Node>();
		UCS.closedList = new LinkedList<Node>();		
	}

	public static int search() {
		theOneQueue = new PriorityQueue<Node>(1000, Comparator.comparingInt(Node::getSteps));
		return throughTheOneQueue();
	}

	private static int throughTheOneQueue() {
		theOneQueue.add(start);
		Node node;
		while(!theOneQueue.isEmpty()) {
			node = theOneQueue.poll();
			if(node.isTarget())
				return node.getSteps();
			if(!closedList.contains(node)) {
				theOneQueue.addAll(node.expand());
				closedList.add(node);
			}
		}		
		return -9999;
	}

}
