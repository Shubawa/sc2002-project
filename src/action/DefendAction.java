package action;

import model.Combatant;
import effect.DefendEffect;
import engine.BattleContext;

public class DefendAction implements IAction {

    @Override
    public String execute(Combatant actor, Combatant target, BattleContext ctx) {
        actor.applyEffect(new DefendEffect(2));
        return actor.getName() + " -> Defend: DEF +" + 10 + " for 2 turns.";
    }
}
