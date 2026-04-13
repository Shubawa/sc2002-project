package effect;

import model.Combatant;

public class SmokeBombEffect implements IStatusEffect {

    private int turnsRemaining;

    public SmokeBombEffect(int turns) {
        this.turnsRemaining = turns;
    }

    @Override
    public void onApply(Combatant c) {
        c.setInvulnerable(true);
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) turnsRemaining--;
    }

    @Override
    public void onExpire(Combatant c) {
        c.setInvulnerable(false);
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
