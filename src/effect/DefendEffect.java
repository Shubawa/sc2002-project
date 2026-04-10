package effect;

import model.Combatant;

public class DefendEffect implements IStatusEffect {

    private static final int DEFENSE_BONUS = 10;
    private int turnsRemaining;

    public DefendEffect(int turns) {
        this.turnsRemaining = turns;
    }

    @Override
    public void apply(Combatant target) {
        target.modifyDefense(DEFENSE_BONUS);
    }

    @Override
    public void tick(Combatant target) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            target.modifyDefense(-DEFENSE_BONUS);
        }
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }
}
