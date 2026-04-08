package strategy;

import model.Combatant;

import java.util.List;

public interface ITurnOrderStrategy {
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}