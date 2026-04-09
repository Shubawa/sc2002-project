package item;

import model.Combatant;
import model.Player;
import engine.BattleContext;

import java.util.List;

public interface IItem {
    void use(Player player, List<? extends Combatant> enemies, Combatant target, BattleContext ctx);
    String getName();
    String getDescription();
}
