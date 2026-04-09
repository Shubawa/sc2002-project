package engine;

import strategy.ITurnOrderStrategy;
import model.Combatant;
import ui.UIDelegate;

import java.util.ArrayList;
import java.util.List;

public class BattleEngine {

  private final ITurnOrderStrategy turnOrderStrategy;
  private final UIDelegate ui;

  public BattleEngine(ITurnOrderStrategy turnOrderStrategy, UIDelegate ui) {
    this.turnOrderStrategy = turnOrderStrategy;
    this.ui = ui;
  }

  // TODO: Update after model is implemented
  public BattleResult runBattle(Player player, Level level) {
    List<Combatant> initialEnemies = level.createInitialEnemies();
    List<Combatant> allCombatants = new ArrayList<>();

    int round = 0;

    while (true) {
      round++;

      List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(allCombatants);
      ui.printRoundHeader(round, player, allCombatants);

      for (Combatant actor : turnOrder) {
        if (!actor.isAlive()) continue;

        if (actor.isStunned()) {
          actor.tickEffects();
          ui.print(actor.getName() + " -> STUNNED: Turn skipped.");
          continue;
        }

        actor.tickEffects();

        if (actor instanceof Player) {
          // Player turn
        } else {
          // Enemy turn
        }
      }
    }
  }
}