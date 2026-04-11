package action;

import model.Combatant;
import engine.BattleContext;

public class BasicAttackAction implements IAction {

    @Override
    public void execute(Combatant actor, Combatant target, BattleContext ctx) {
        if (target == null || !target.isAlive()) {
            ctx.log(actor.getName() + " has no valid target!");
            return;
        }

        int before = target.getCurrentHp();
        int dmg = target.takeDamage(actor.getAttack());
        int after = target.getCurrentHp();

        StringBuilder sb = new StringBuilder();
        sb.append(actor.getName())
          .append(" -> Basic Attack -> ")
          .append(target.getName())
          .append(": HP: ").append(before).append(" -> ").append(after)
          .append(" (dmg: ").append(actor.getAttack())
          .append("-").append(target.getDefense())
          .append("=").append(dmg).append(")");

        if (!target.isAlive()) {
            sb.append(" X ELIMINATED");
        }

        ctx.log(sb.toString());
    }

    @Override
    public String getName() {
        return "Basic Attack";
    }
}


