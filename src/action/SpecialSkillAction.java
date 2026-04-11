package action;

import model.Combatant;
import model.Player;
import engine.BattleContext;

public class SpecialSkillAction implements IAction {

    private static final int COOLDOWN_AFTER_USE = 3;
    private final Combatant target;

    public SpecialSkillAction(Combatant target) {
        this.target = target;
    }

    @Override
    public void execute(Combatant actor, Combatant ignored, BattleContext ctx) {
        if (!(actor instanceof Player)) {
            ctx.log(actor.getName() + " cannot use special skills!");
            return;
        }

        Player player = (Player) actor;

        if (!player.isSpecialSkillReady()) {
            ctx.log(player.getName() + " -> " + player.getSpecialSkillName()
                    + " is on cooldown! (" + player.getSpecialSkillCooldown() + " turn(s) remaining)");
            return;
        }

        String result = player.executeSpecialSkill(ctx.getActiveEnemies(), target);
        ctx.log(result);
        player.setSpecialSkillCooldown(COOLDOWN_AFTER_USE);
    }

    @Override
    public String getName() {
        return "Special Skill";
    }
}
