package effect;

import model.Combatant;

public class DefendEffect implements IStatusEffect {

    private static final int DEFENSE_BONUS = 10;
    private int turnsRemaining;

    public DefendEffect(int turns) {
        this.turnsRemaining = turns;
    }

    @Override
    public void onApply(Combatant c) {
        c.modifyDefense(DEFENSE_BONUS);
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) turnsRemaining--;
    }

    @Override
    public void onExpire(Combatant c) {
        c.modifyDefense(-DEFENSE_BONUS);
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public String getName() {
        return "Defend";
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}

