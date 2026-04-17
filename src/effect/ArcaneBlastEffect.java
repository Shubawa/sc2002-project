package effect;

import model.Combatant;

public class ArcaneBlastEffect implements IStatusEffect {

    private int bonus;

    public ArcaneBlastEffect(int initialBonus) {
        this.bonus = initialBonus;
    }

    public void addBonus(int amount, Combatant c) {
        bonus += amount;
        c.modifyAttack(amount);
    }

    @Override
    public void onApply(Combatant c) {
        c.modifyAttack(bonus);
    }

    @Override
    public void tick() {
        // Lasts until end of level — no countdown needed
    }

    @Override
    public void onExpire(Combatant c) {
        c.modifyAttack(-bonus);
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String getName() {
        return "Arcane Blast (+" + bonus + " ATK)";
    }

    public int getBonus() {
        return bonus;
    }
}
