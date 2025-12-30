# 👾 Pacman MDP Agent

Intelligenter Pacman-Agent basierend auf **Markov Decision Process (MDP)** für autonomes Spielen.

Implementiert KI-Agenten für Pacman, Pacman VS und Wumpus World mit Value Iteration und MDP-Strategien.

## 📁 Struktur

- `src/de/fh/ki/fin/mdp/` - MDP Implementation und Value Iteration
- `src/de/fh/ki/fin/search/` - Suchalgorithmen (UCS)
- `src/de/fh/ki/fin/util/` - Hilfsklassen
- `data/pacman/level/` - Level-Dateien
- `data/pacmanvs/level/` - PvP Level
- `data/wumpus/level/` - Wumpus World Level

## ⚙️ Voraussetzungen

- Java 11+
- JavaFX SDK
- server.jar (wird von der Hochschule bereitgestellt, nicht im Repo enthalten)

## 🚀 Setup

1. **Repository klonen**
   ```bash
   git clone https://github.com/Sheer-a/pacman-mdp-agent.git
   ```

2. **JavaFX VM-Optionen** (in IDE konfigurieren)
   ```
   --module-path /PATH/TO/javafx-sdk/lib --add-modules=javafx.controls,javafx.fxml
   ```

3. **server.jar einbinden**
   - Datei von der Hochschule erhalten
   - Im Projektroot platzieren

4. **Projekt starten**
   ```bash
   javac -d bin src/de/fh/ki/fin/**/*.java
   java -cp bin:server.jar de.fh.ki.fin.mdp.MDP_Runner
   ```

## 🧠 Funktionsweise

Der Agent nutzt **Value Iteration** um optimale Strategien zu berechnen:

```java
newValue = (1-γ) * currentValue + γ * neighbourAverage + stepCost
```

- Abwertungsfaktor (γ): 0.1
- Schrittkosten: -0.0001
- Umgebung ist vollständig deterministisch

## 🎮 Level

**Pacman:** dots_einfach, dots_mittel, dots_schwer, FHDO, raster_HR, raster_RR

**Pacman VS:** PVS_Easy, PVS_Medium, PVS_Hard, PVS_Extreme, PVS_FHDO

**Wumpus World:** WW_base_easy/moderate, WW_addon_easy/moderate/advanced, WW_large

## 🛠️ Entwicklung

Neuen Agent erstellen:
```java
public class MyAgent extends PacmanAgent {
    public PacmanAction getFinalNextAction(PacmanPercept percept) {
        // MDP berechnen und beste Aktion zurückgeben
    }
}
```

Debugging mit `Logging.java` aktivieren.

## 👥 Autoren

Gruppe 13 - KI-Kings
- Sheer Ahmed
- Abdulaziz Aldalati
- Mohamed Chamharouch Aukili

## 📝 Lizenz

MIT License
