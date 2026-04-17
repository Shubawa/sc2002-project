package action;

import model.Combatant;
import engine.BattleContext;

public interface IAction {
    void execute(Combatant actor, Combatant target, BattleContext ctx);
    String getName();
}