package de.fh.ki.fin.mdp;

import de.fh.kiServer.agents.Agent;
import de.fh.pacman.PacmanAgent;
import de.fh.pacman.PacmanGameResult;
import de.fh.pacman.PacmanPercept;
import de.fh.pacman.PacmanStartInfo;
import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanActionEffect;
import de.fh.ki.fin.util.Logging;

/**
 * @author Sheer Ahmed
 * @author Abdulaziz Aldalati
 * @author Mohamed Chamharouch Aukili
 * 
 * Gruppe 13 KI-Kings
 *
 */
public class MyAgent_Final extends PacmanAgent {

	private final MDP_Runner mdpRunner;
	
	public MyAgent_Final(String name) {
		super(name);
		mdpRunner = new MDP_Runner();
		MDP_State.createState();
		MDP_Mapper.createMapper();
		MDP_DeadEndHandler.createHandler();
		
	}
	
	public static void main(String[] args) {
		MyAgent_Final agent = new MyAgent_Final("KI-KINGS_AGENT");
		Agent.start(agent, "127.0.0.1", 5000);
	}

	/**
	 * @param percept - Aktuelle Wahrnehmung des Agenten, bspw. Position der Geister und Zustand aller Felder der Welt.
	 * @param actionEffect - Aktuelle Rückmeldung des Server auf die letzte übermittelte Aktion.
	 */
	@Override
	public PacmanAction action(PacmanPercept percept, PacmanActionEffect actionEffect) {
		MDP_DeadEndHandler.setView(percept.getView());
		Logging.printMdpDeadEndMap(MDP_DeadEndHandler.getHandler().getDeadEndMap(), MDP_DeadEndHandler.getDeadEnds());
		MDP_State.newTurn(percept, actionEffect);
		Logging.printView(percept.getView());
		final PacmanAction action = mdpRunner.getFinalNextAction(percept);
		return action;
	}

	@Override
	protected void onGameStart(PacmanStartInfo startInfo) {	}

	@Override
	protected void onGameover(PacmanGameResult gameResult) { }
}
