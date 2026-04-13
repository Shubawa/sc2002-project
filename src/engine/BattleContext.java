package engine;

import model.Combatant;
import model.Enemy;

import java.util.ArrayList;
import java.util.List;

public class BattleContext {

    private final List<Combatant> allCombatants;
    private final List<String> battleLog = new ArrayList<>();
    private int roundNumber = 0;

    public BattleContext(List<Combatant> allCombatants) {
        this.allCombatants = allCombatants;
    }

    public void log(String message) {
        battleLog.add(message);
    }

    public List<String> flushLog() {
        List<String> copy = new ArrayList<>(battleLog);
        battleLog.clear();
        return copy;
    }

    public List<Combatant> getActiveEnemies() {
        List<Combatant> enemies = new ArrayList<>();
        for (Combatant c : allCombatants) {
            if (c instanceof Enemy && c.isAlive()) enemies.add(c);
        }
        return enemies;
    }

    public List<Combatant> getAliveCombatants() {
        List<Combatant> alive = new ArrayList<>();
        for (Combatant c : allCombatants) {
            if (c.isAlive()) alive.add(c);
        }
        return alive;
    }

    public List<Combatant> getAllCombatants() {
        return allCombatants;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int n) {
        roundNumber = n;
    }

    public void addCombatants(List<Combatant> newOnes) {
        allCombatants.addAll(newOnes);
    }
}
