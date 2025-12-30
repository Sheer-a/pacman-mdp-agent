package de.fh.ki.fin.mdp;

import java.util.ArrayList;
import de.fh.kiServer.util.Vector2;
import de.fh.pacman.GhostInfo;
import de.fh.pacman.enums.PacmanTileType;

public class MDP_ValueManager {
    public static final double PACMAN = -9999999.99d;
    public static final double BLOCKED = -Double.MAX_VALUE;
    private MDP_DeadEndHandler mdpDeadEndHandler;
    
    public MDP_ValueManager() {
        mdpDeadEndHandler = MDP_DeadEndHandler.getHandler();
    }

	public double getDotValue(MDP_Field mdpField) {
		mdpDeadEndHandler = MDP_DeadEndHandler.getHandler();
		int remainingPowerPillMoves = getRemainingPowerPillMoves(mdpField);
		if(mdpField.isGhostRespawnActive()) {
			MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), -5.0d);
			return -5.0d;
		}
		else if(mdpDeadEndHandler.isPartOfDeadEnd(mdpField.getVectorR())) {
			final ArrayList<Vector2> deadEnd = mdpDeadEndHandler.getDeadEnd(mdpField.getVectorR());
			if(remainingDotsAreInOneDeadEnd(mdpField)) {
				MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), 10);
				return 10.0d;
			} 
			else if(remainingPowerPillMoves>deadEnd.size()) {
				MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), 5);
				return 5.0d;
			}
			else if (isSaveDeadEnd(mdpField, deadEnd))
				return 1.233d;
			else if(!isPacmanInDeadEnd(mdpField, deadEnd)) {
					MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), -4.8);
					Vector2 entrence = mdpDeadEndHandler.getEntranceOfDeadEnd(mdpField);
					MDP_State.insertValue(new Vector2(entrence.getX(), entrence.getY()), -10);
					return 0.0d;
			}
		}
		return 1.0d + 1.233d*(1/remainingDots(mdpField)) - mdpField.getGhostsNearBy(1);
	}

	public double getEmptyValue(MDP_Field mdpField) {
        mdpDeadEndHandler = MDP_DeadEndHandler.getHandler();
        double value = 0.0d;
		if(mdpField.isGhostRespawnActive()) {
			value = -5.0d;
			MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), -5.0d);
		}
		else if(mdpDeadEndHandler.isPartOfDeadEnd(mdpField.getVectorR())) {
			ArrayList<Vector2> deadEnd = mdpDeadEndHandler.getDeadEnd(mdpField.getVectorR());
			Vector2 deadEndEnd = mdpDeadEndHandler.getDeadEndEnd(deadEnd);
			if(deadEndEnd.getY()==mdpField.getX() && deadEndEnd.getX()==mdpField.getY())
				value = -mdpDeadEndHandler.getDeadEnd(mdpField.getVectorR()).size();
		}
    	return value;
    }
	
	public double getPowerPillValue(MDP_Field mdpField) {
		double value = 0.0d, tmp = 0.0d;
		if(mdpField.getPathLengthToPacMan() <= 2) {
	        for(GhostInfo g : mdpField.ghostsInRangeOf(5)) {
	        	if(g.getPillTimer()<2 && !g.isDead()) {
	        		if(g.getType().equals("ghost_hunter"))
	        			value = 4.5d;
	        		else if(g.getPillTimer()>2 && g.getType().equals("ghost_eager")) {
	        			tmp = 4.0d;
	        			if(tmp>value) value = tmp;
	        		}    			
	        		else if(g.getPillTimer()>2 && g.getType().equals("ghost_random")) {
	        			tmp = 3.5d;
	        			if(tmp>value) value = tmp;
	        		}
	        	}
	        }
		}
		return value;
	}
	
	public double getGhostValue(MDP_Field mdpField) {
		for(GhostInfo g : mdpField.getGhostInfo())
			if(g.getPos().getX() == mdpField.getX() && g.getPos().getY() == mdpField.getY()) {
				if(g.getType().equals("ghost_random")) {
			    	if(g.getPillTimer()<1)
						MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), -5.5d);
			    	else
						MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), 2.0d);
					
			    	return g.isDead() ? 0 : (g.getPillTimer()>0 ? 2.0d : -3.5d);
				}
				else if(g.getType().equals("ghost_eager")) {
			    	if(g.getPillTimer()<1)
						MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), -5.5d);
			    	else
						MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), 3.0d);
			    	
			    	return g.isDead() ? 0 : (g.getPillTimer()>0 ? 3.0d : -4d);
				} 
				else if(g.getType().equals("ghost_hunter")) {
			    	if(g.getPillTimer()<1) {
						MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), -5.5d);
			    		setFieldsNearByGhosts(mdpField);
			    	}
			    	else
						MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()), 3.5d);
			    	
			    	return g.isDead() ? 0 : (g.getPillTimer()>0 ? 3.5d : -4.5d);
				}				
		}		
		return 0.0d;
	}
	
	private int getRemainingPowerPillMoves(MDP_Field mdpField) {
		for(GhostInfo g : mdpField.getGhostInfo())
			if (g.getType().equals("ghost_hunter") && !g.isDead())
				return g.getPillTimer();
		 return 0;
	}
	
	private void setFieldsNearByGhosts(MDP_Field mdpField) {
		if(mdpField.getPercept().getView()[mdpField.getX()+1][mdpField.getY()] != PacmanTileType.WALL)
			MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()+1), -5.0d);
		if(mdpField.getPercept().getView()[mdpField.getX()-1][mdpField.getY()] != PacmanTileType.WALL)
			MDP_State.insertValue(new Vector2(mdpField.getY(), mdpField.getX()-1), -5.0d);
		if(mdpField.getPercept().getView()[mdpField.getX()][mdpField.getY()+1] != PacmanTileType.WALL)
			MDP_State.insertValue(new Vector2(mdpField.getY()+1, mdpField.getX()), -5.0d);
		if(mdpField.getPercept().getView()[mdpField.getX()][mdpField.getY()-1] != PacmanTileType.WALL)
			MDP_State.insertValue(new Vector2(mdpField.getY()-1, mdpField.getX()), -5.0d);
	}
	
	private boolean isPacmanInDeadEnd(MDP_Field mdpField, ArrayList<Vector2> deadEnd) {
		for(Vector2 v : deadEnd)
			if(v.getY()==mdpField.getPercept().getPosX() && v.getX()==mdpField.getPercept().getPosY())
				return true;
		return false;
	}

	private boolean remainingDotsAreInOneDeadEnd(MDP_Field mdpField) {
		final PacmanTileType[][] view = mdpField.getPercept().getView();
		int counterDots = 0;
		for(int i=0; i<view.length; i++)
			for( int j=0; j<view[i].length; j++)
				if(view[i][j] == PacmanTileType.DOT)
					counterDots++;
		int counterDotsInDeadEnd = 0;
		for(ArrayList<Vector2> deadEnd : MDP_DeadEndHandler.getDeadEnds()) {
			for(Vector2 v : deadEnd)
				if(view[v.getY()][v.getX()] == PacmanTileType.DOT)
					counterDotsInDeadEnd++;
			if(counterDots==counterDotsInDeadEnd)
				return true;
			counterDotsInDeadEnd = 0;
		}
		return false;
    }
	
	private int remainingDots(MDP_Field mdpField) {
		final PacmanTileType[][] view = mdpField.getPercept().getView();
		int result = 0;
		for(int i=0; i<view.length; i++)
			for(int j=0; j<view[i].length; j++)
				if(view[i][j] == PacmanTileType.DOT)
					result++;
				
		return result;
	}
    
	private boolean isSaveDeadEnd(MDP_Field mdpField, ArrayList<Vector2> deadEnd) {
		final Vector2 entrance = mdpDeadEndHandler.getEntranceOfDeadEnd(mdpField);
		MDP_Field mdpFieldEntrance = new MDP_Field(entrance.getY(), entrance.getX(), mdpField.getPercept());
		for(GhostInfo g : mdpField.getGhostInfo()) {
			if(g.getType().equals("ghost_hunter")) {
				int pathLengthToGhost = mdpField.getPathDeadEndEntranceToGhost(entrance, g.getPos()) + g.getPillTimer();
				int pathLengthToPacman = mdpFieldEntrance.getPathLengthToPacMan();
				int deepth = deadEnd.size();
				return pathLengthToGhost > pathLengthToPacman+(2*deepth)+2;
			} else {
				for(Vector2 field : deadEnd)
					if(g.getPos().getY()==field.getX() && g.getPos().getX()==field.getY())
						return false;
			}
		}
		return true;	    
	}
}