package effect;

import model.Combatant;

public interface IStatusEffect {
    void apply(Combatant target);
    void tick(Combatant target);
    void onExpire(Combatant combatant);
    String getName();
    boolean isExpired();
}