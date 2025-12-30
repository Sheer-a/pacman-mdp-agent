package de.fh.stud.p2;


import de.fh.pacman.enums.PacmanTileType;

public class BaumTest {

	public static void main(String[] args) {
		//Anfangszustand nach Aufgabe
		PacmanTileType[][] view = {
				{PacmanTileType.WALL,PacmanTileType.WALL,PacmanTileType.WALL,PacmanTileType.WALL},
				{PacmanTileType.WALL,PacmanTileType.EMPTY,PacmanTileType.DOT,PacmanTileType.WALL},
				{PacmanTileType.WALL,PacmanTileType.DOT,PacmanTileType.WALL,PacmanTileType.WALL},
				{PacmanTileType.WALL,PacmanTileType.WALL,PacmanTileType.WALL,PacmanTileType.WALL}
		};
		//Startposition des Pacman
		int posX = 1, posY = 1;
		/*
		 * TODO Praktikum 2 [3]: Baut hier basierend auf dem gegebenen 
		 * Anfangszustand (siehe view, posX und posY) den Suchbaum auf.
		 */
		
		Knoten startKnoten = new Knoten(view, null, posX, posY, 0);

		for(Knoten k : startKnoten.expand()) {
			System.out.println("erste Ebene:\n"+k);
			for(Knoten k2 : k.expand()) {
				System.out.println(k2);
				for(Knoten k3 : k2.expand()) {
					System.out.println(k3);
				}
			}
		}
	}
}
