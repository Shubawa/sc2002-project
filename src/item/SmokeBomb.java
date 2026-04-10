package item;

import model.Combatant;
import model.Player;
import effect.SmokeBombEffect;
import engine.BattleContext;

import java.util.List;

public class SmokeBomb implements IItem {

    @Override
    public void use(Player player, List<? extends Combatant> enemies, Combatant target, BattleContext ctx) {
        player.applyEffect(new SmokeBombEffect(2));
        ctx.log(player.getName() + " -> Smoke Bomb used: Enemy attacks deal 0 damage this turn + next.");
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public String getDescription() {
        return "Enemy attacks deal 0 damage for 2 turns.";
    }
}
