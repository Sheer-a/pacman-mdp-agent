package de.fh.ki.fin.mdp;

import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanAction;
import de.fh.ki.fin.util.Logging;

public class MDP_Runner {
	private static final double G = 0.1d;				// Abwertungskonstante
	private static final double STEP_COST = -0.0001d;	// Schrittkosten
	private static final int ITERATION_FACTOR = 1;

	private final MDP_Mapper mdpMapper;
	
	public MDP_Runner() {
		MDP_Mapper.createMapper();
		mdpMapper = MDP_Mapper.getMapper();
	}
	
	public PacmanAction getFinalNextAction(PacmanPercept percept) {
		MDP_DeadEndHandler.setView(percept.getView());
		MDP_Map mdpMap = mdpMapper.toMap(percept);
		
		double[][] values = mdpMap.getValues();
		double[][] newMdpMap = new double[values.length][values[0].length];
		
		for(int i=0; i<newMdpMap.length; i++)
			for(int j=0; j<newMdpMap[i].length; j++)
				newMdpMap[i][j] = mdpMap.getValue(i, j);
		
		int iterations = ITERATION_FACTOR*(values.length + values[0].length);
		
		while(iterations > 0) {
			Logging.printMdpMap(iterations, new MDP_Map(newMdpMap));
			for(int i=0; i<values[0].length; i++)
				for(int j=0; j<values.length; j++)
					if(!(mdpMap.getValue(i, j) == MDP_ValueManager.BLOCKED || mdpMap.getValue(i, j) == MDP_ValueManager.PACMAN)) {
						/*
						 *  Das Pacman Spiel ist in diesem Szenario ein determinstisches Spiel.
						 *  Warum? Weil der Pacman ohne Zufallskomponente spielt. 
						 *  Es gibt eine einzige Transaktion, die beträgt 100%.
						 */
						double range = 1;
						double neighbourAvgValue = mdpMap.getNeighbourAvgValue(i,j);
						
						/*
						 *  Auf- oder Abwerten über G und den Durchschnitt der Nachbarn + 
						 *  Kosten pro Schritt: 0.001d * iterations
						 */
	                    newMdpMap[i][j] = range*((1-G)*mdpMap.getValue(i,j) + (G)*neighbourAvgValue) + STEP_COST;
					}
			mdpMap.setValues(newMdpMap);
			iterations--;
		}
		MDP_State.getState().lastFillInMdpMap(mdpMap);
		Logging.printMdpMap(0, mdpMap);
		return mdpMap.getActionToBestNeighbour(percept.getPosX(), percept.getPosY());
	}
}
