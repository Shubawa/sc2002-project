package ui;

import model.Player;
import model.Combatant;
import java.util.List;
import action.IAction;
import engine.BattleContext;

public interface UIDelegate {
  IAction promptPlayerAction(Player player, BattleContext ctx);
  void print(String message);
  void printRoundHeader(int round, Player player, List<Combatant> enemies);
  void printEndOfRound(int round, Player player, List<Combatant> allEnemies);
}
