package action;

import model.Combatant;
import effect.DefendEffect;
import engine.BattleContext;

public class DefendAction implements IAction {

    private static final int DURATION = 2;

    @Override
    public void execute(Combatant actor, Combatant target, BattleContext ctx) {
        int beforeDef = actor.getDefense();
        actor.applyEffect(new DefendEffect(DURATION));
        ctx.log(actor.getName() + " -> Defend -> DEF +10 for " + DURATION + " rounds."
                + " (DEF: " + beforeDef + " -> " + actor.getDefense() + ")");
    }

    @Override
    public String getName() {
        return "Defend";
    }
}
