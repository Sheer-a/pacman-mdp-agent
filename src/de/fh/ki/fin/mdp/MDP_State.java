package de.fh.ki.fin.mdp;

import java.util.*;
import javafx.util.Pair;

import de.fh.kiServer.util.Vector2;
import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanActionEffect;
import de.fh.ki.fin.util.RingBuffer;

public class MDP_State {
	
	private static MDP_State state;
	private final RingBuffer<Vector2> prevPacmanPositions;
	private final List<Pair<Vector2, Double>> listPosValue;
	private int cycleResolutionMoves = 0;
	
	private MDP_State() {
		listPosValue = new ArrayList<Pair<Vector2, Double>>();
		prevPacmanPositions = new RingBuffer<Vector2>(5);
	}
	
	public static void createState() {
		state = new MDP_State();
	}
	
	public static MDP_State getState() {
		return state;
	}
	
	public static int getCycleResolutionMoves() {
		return state.cycleResolutionMoves;
	}
	
	public static void insertValue(Vector2 pos, double value) {
		state.listPosValue.add(new Pair<Vector2, Double>(pos, value));
	}
	
	// Die Werte werden in der finalen Map übertagen!
	public void lastFillInMdpMap(MDP_Map mdpMap) {
		listPosValue.forEach(value -> {
			mdpMap.setValue(value.getValue(), value.getKey().x, value.getKey().y);
		});
		listPosValue.clear();
	}
	
	// 
	public static void newTurn(PacmanPercept percept, PacmanActionEffect actionEffect) {
		updateCounter();
		Vector2 pacmanPos = percept.getPosition();
		if(state.prevPacmanPositions.countOccurrences(pacmanPos)>3)
			state.cycleResolutionMoves += 10;
		state.prevPacmanPositions.addToBuffer(pacmanPos);
	}
	
	// Nach jede Aktion wird die Anzahl der sicheren Transaktionen um eins verringert.
	private static void updateCounter() {
		if(state.cycleResolutionMoves > 0)
			--state.cycleResolutionMoves;
	}
	
}
