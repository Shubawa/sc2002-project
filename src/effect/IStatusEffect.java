package effect;

import model.Combatant;

public interface IStatusEffect {
    void onApply(Combatant combatant);
    void tick();
    void onExpire(Combatant combatant);
    String getName();
    boolean isExpired();
}