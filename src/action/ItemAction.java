package action;

import model.Combatant;
import model.Player;
import engine.BattleContext;
import item.IItem;

public class ItemAction implements IAction {

    private final Item item;
    private final Combatant target;

    public ItemAction(Item item, Combatant target) {
        this.item = item;
        this.target = target;
    }

    @Override
    public void execute(Combatant actor, Combatant ignored, BattleContext ctx) {
        if (!(actor instanceof Player)) {
            ctx.log(actor.getName() + " cannot use items!");
            return;
        }

        Player player = (Player) actor;
        player.consumeItem(item);
        item.use(player, ctx.getActiveEnemies(), target, ctx);
    }

    @Override
    public String getName() {
        return "Item: " + item.getName();
    }
}
