package action;

import model.Combatant;
import engine.BattleContext;

public interface IAction {
    String execute(Combatant actor, Combatant target, BattleContext ctx);
    String getName();
}