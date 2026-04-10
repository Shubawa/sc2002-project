package item;

import model.Combatant;
import model.Player;
import engine.BattleContext;

import java.util.List;

public class Potion implements IItem {

    private static final int HEAL_AMOUNT = 100;

    @Override
    public void use(Player player, List<? extends Combatant> enemies, Combatant target, BattleContext ctx) {
        int before = player.getCurrentHp();
        player.heal(HEAL_AMOUNT);
        int after = player.getCurrentHp();
        ctx.log(player.getName() + " -> Potion used: HP: " + before + " -> " + after
                + " (+" + (after - before) + ")");
    }

    @Override
    public String getName() {
        return "Potion";
    }

    @Override
    public String getDescription() {
        return "Heal 100 HP (capped at max HP).";
    }
}
