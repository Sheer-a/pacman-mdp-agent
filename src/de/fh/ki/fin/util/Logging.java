package de.fh.ki.fin.util;

import java.util.ArrayList;

import de.fh.kiServer.util.Util;
import de.fh.kiServer.util.Vector2;
import de.fh.pacman.enums.PacmanTileType;
import de.fh.ki.fin.mdp.MDP_Map;

public class Logging {
	
	public static final boolean ON = false;

    public static void printView(PacmanTileType[][] view) {
        if (Logging.ON)
        	Util.printView(view);
    }

    public static void printMdpMap(int iteration, MDP_Map map) {
        if (Logging.ON)
            MDP_Map.printMap(iteration, map);
    }

	public static void printMdpDeadEndMap(int[][] deadEndMap, ArrayList<ArrayList<Vector2>> deadEnds) {
		if(Logging.ON) {
			
			for(int i=0; i<deadEndMap.length; ++i) {
				for(int j=0; j<deadEndMap[i].length; ++j)
					System.out.print(deadEndMap[i][j]+"\t");
				System.out.println();
			}
			
			int i = 0;
			System.out.println("DeadEnds:");
		    if(!deadEnds.isEmpty()) {
		        for(ArrayList<Vector2> deadEnd : deadEnds) {
		            System.out.print("DeadEnd " + (++i) + ":");
		            for(Vector2 field : deadEnd)
		                System.out.print(" (" + field.getX() + ", " + field.getY() + "),");
		            System.out.println();
		        }
		    }
		}
	}
}
