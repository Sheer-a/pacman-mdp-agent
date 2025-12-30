package de.fh.stud.p3;

import java.util.*;
import de.fh.stud.p2.Knoten;

public class Suche {
	
	private Stack<Knoten> stack;
	private Queue<Knoten> queue;
	private List<Knoten> closedList;
	private Knoten start;
	
	public enum SearchType {
		DFS, BFS, GREEDY, UCS, ASTAR
	}
	
	public Suche(Knoten start) {
		this.start = start;
		closedList = new ArrayList<Knoten>();
		stack = new Stack<Knoten>();
	}	
	
	public Knoten start(SearchType type) {
		switch (type) {
		case DFS: 		return dfs();
		case BFS: 		return bfs();
		case GREEDY: 	return greedy();
		case UCS: 		return ucs();
		case ASTAR:		return aStar();
		}
		return null;
	}
	
	public Knoten dfs() {
		stack.push(start);
		//Für Ausgabe Queue intial.
		queue = new LinkedList<Knoten>(); 
		
		while(!stack.isEmpty()) {
			Knoten k = stack.pop();
			closedList.add(k);			
			if(k.isTarget()) {
				ausgeben();
				return k;
			}
			else {
				for(Knoten child : k.expand()) {
					if(!closedList.contains(child))
						stack.push(child);
				}
			}			
		}
		return null;
	}
	
	public Knoten bfs() {
		this.queue = new LinkedList<Knoten>();
		Knoten k = throughTheQueue();
		ausgeben();
		return k;
	}
	
	public Knoten ucs() {
		queue = new PriorityQueue<Knoten>(100, Comparator.comparingInt(Knoten::getTurn));
		Knoten k = throughTheQueue();
		ausgeben();
		return k;
	}
	
	public Knoten greedy() {
		queue = new PriorityQueue<Knoten>(100, Comparator.comparingInt(Knoten::getNumDots));
		Knoten k = throughTheQueue();
		ausgeben();
		return k;
	}
	
	public Knoten aStar() { // FUNKTIONIERT NICHT
		queue = new PriorityQueue<Knoten>(100, Comparator.comparingInt((x)-> {return x.getTurn()+x.getNumDots();}));
		Knoten k = throughTheQueue();
		ausgeben();
		return k;
	}
	
	private Knoten throughTheQueue() {
		
		queue.add(start);
		
		while(!queue.isEmpty()) {

			Knoten k = queue.poll();
			closedList.add(k);
			
			if(k.isTarget())
				return k;
			
			for(Knoten child : k.expand())
				if(!closedList.contains(child))
					queue.add(child);
		}
		
		return null;
	}
	
	
	private void ausgeben() {
		System.out.println("openlist (Stack): "+stack.size()+" openlist (Queue): "+queue.size()+" closedlist: "+closedList.size());
	}
}
