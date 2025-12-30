package de.fh.ki.fin.mdp;

import java.util.*;
import de.fh.kiServer.util.Vector2;
import de.fh.pacman.GhostInfo;
import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanTileType;
import de.fh.ki.fin.search.UCS;


public class MDP_Field {
	
	private int x;
	private int y;
	private PacmanPercept percept;
	
	public MDP_Field(int x, int y, PacmanPercept percept) {
		this.x = x;
		this.y = y;
		this.percept = percept;
	}
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setPercept(PacmanPercept percept) {
        this.percept = percept;
    }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public Vector2 getVector() { 
		return new Vector2(x, y); 
	}
    public Vector2 getVectorR() { 
    	return new Vector2(y, x); 
    }
    public PacmanPercept getPercept() {
        return percept;
    }
	public List<GhostInfo> getGhostInfo() {
		return percept.getGhostInfos();
	}
	
	public int getPathLengthToPacMan() {	
		UCS.setGoalPath(x, y, percept.getPosX(), percept.getPosY(), percept.getView());
		return UCS.search();
	}

	public int getPathDeadEndEntranceToGhost(Vector2 entrance, Vector2 ghostPos) {
		UCS.setGoalPath(entrance.getX(), entrance.getY(), ghostPos.getY(), ghostPos.getX(), percept.getView());
		return UCS.search();
	}
	
	public int getPathLengthToNextGhost() {
		int result = 1000;
		for(GhostInfo ghostInfo  : percept.getGhostInfos())
			if(!ghostInfo.isDead()) {
				UCS.setGoalPath(x, y, ghostInfo.getPos().getX(), ghostInfo.getPos().getY(), percept.getView());
				int searchResult = UCS.search();
				if(result > searchResult)
					result = searchResult;
			}
		return result;
	}
	
	public int getGhostsNearBy(int range) {
	    int result = 0;
	    int x = this.x, y = this.y;
	    PacmanTileType[][] view = percept.getView();
	    int viewWidth = view.length, viewHeight = view[0].length;

	    for (int i=1; i<=range; i++) {
	        if (x+i < viewWidth && view[x+i][y] == PacmanTileType.GHOST)
	            result++;
	        if (x-i >= 0 && view[x-i][y] == PacmanTileType.GHOST)
	            result++;
	        if (y+i < viewHeight && view[x][y+i] == PacmanTileType.GHOST)
	            result++;
	        if (y-i >= 0 && view[x][y-i] == PacmanTileType.GHOST)
	            result++;
	    }
	    return result;
	}
	
	public boolean isGhostRespawnActive() {
		List<GhostInfo> ghostInfos = percept.getGhostInfos();
		for(GhostInfo ghostInfo  : ghostInfos)
			if(ghostInfo.isDead() && 
					x == ghostInfo.getSpawn().getX() &&
					y == ghostInfo.getSpawn().getY() &&
					3 > ghostInfo.getRespawnTimer())
				return true;
		return false;
	}

	public boolean isPartOfDeadEnd(MDP_DeadEndHandler mdpDeadEndHandler) {
		return mdpDeadEndHandler.isPartOfDeadEnd(getVector());
	}
	
	public List<GhostInfo> ghostsInRangeOf(int range) {
	    List<GhostInfo> ghostsInRange = new ArrayList<>();
	    for (GhostInfo ghostInfo : getPercept().getGhostInfos()) {
	        UCS.setGoalPath(x, y, ghostInfo.getPos().getX(), ghostInfo.getPos().getY(), percept.getView());
	        if (UCS.search() <= range) {
	            ghostsInRange.add(ghostInfo);
	        }
	    }
	    return ghostsInRange;
	}
}