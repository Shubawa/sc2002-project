package effect;

import model.Combatant;

public class StunEffect implements IStatusEffect {

    private int turnsRemaining;

    public StunEffect(int turns) {
        this.turnsRemaining = turns;
    }

    @Override
    public void onApply(Combatant c) {
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) turnsRemaining--;
    }

    @Override
    public void onExpire(Combatant c) {
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public String getName() {
        return "Stun";
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
