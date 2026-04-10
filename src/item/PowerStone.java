package item;

import model.Combatant;
import model.Player;
import engine.BattleContext;

import java.util.ArrayList;
import java.util.List;

public class PowerStone implements IItem {

    @Override
    public void use(Player player, List<? extends Combatant> enemies, Combatant target, BattleContext ctx) {
        int cooldownBefore = player.getSpecialSkillCooldown();

        ctx.log(player.getName() + " -> Power Stone used -> " + player.getSpecialSkillName() + " triggered:");
        String result = player.executeSpecialSkill(new ArrayList<>(enemies), target);
        ctx.log(result);

        player.setSpecialSkillCooldown(cooldownBefore);
        ctx.log("  Cooldown unchanged -> " + cooldownBefore);
    }

    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public String getDescription() {
        return "Free extra use of Special Skill. Does not start or change cooldown.";
    }
}
