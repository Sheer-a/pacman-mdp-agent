package de.fh.ki.fin.mdp;

import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanTileType;

public class MDP_Mapper {
	
	public static MDP_Mapper mapper;
	private final MDP_ValueManager mdpValueManager;

    private MDP_Mapper() {
    	this.mdpValueManager = new MDP_ValueManager();
    }
    
	public static void createMapper() {
		mapper = new MDP_Mapper();		
	}
	
	public static MDP_Mapper getMapper() {
		return mapper;
	}
	
	public MDP_Map toMap(PacmanPercept percept) {
		PacmanTileType[][] view = percept.getView();
        double[][] mdpValues = new double[view.length][view[0].length];
        try {
            for(int i=0; i<view[0].length; ++i)
            	for(int j=0; j<view.length; ++j)
            		mdpValues[j][i] = fieldMapper(new MDP_Field(i, j, percept));
        } catch(TypeNotPresentException e) {
            e.printStackTrace();
            System.exit(-1);
        }        
        return new MDP_Map(mdpValues);
	}

	private double fieldMapper(MDP_Field mdpField) {
        switch (mdpField.getPercept().getView()[mdpField.getX()][mdpField.getY()]) {
        	case PACMAN:
        		return MDP_ValueManager.PACMAN;
        	case WALL:
        		return MDP_ValueManager.BLOCKED;
	        case DOT:
	        	return mdpValueManager.getDotValue(mdpField);
	        case EMPTY:
	        	return mdpValueManager.getEmptyValue(mdpField);
	        case POWERPILL:
	        	return mdpValueManager.getPowerPillValue(mdpField);
	        case GHOST, GHOST_AND_DOT, GHOST_AND_POWERPILL:
	        	return mdpValueManager.getGhostValue(mdpField);
        	default:
        		throw new TypeNotPresentException(null, null);
		}
	}
}