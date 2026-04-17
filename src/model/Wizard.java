package model;

import effect.ArcaneBlastEffect;
import effect.IStatusEffect;

import java.util.ArrayList;
import java.util.List;

public class Wizard extends Player {

    private static final int BASE_HP  = 200;
    private static final int BASE_ATK = 50;
    private static final int BASE_DEF = 10;
    private static final int BASE_SPD = 20;

    public Wizard(String instanceName) {
        super(instanceName, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
    }

    @Override
    public String executeSpecialSkill(List<Combatant> enemies, Combatant target) {
        List<Combatant> alive = new ArrayList<>();
        for (Combatant e : enemies) {
            if (e.isAlive()) alive.add(e);
        }

        if (alive.isEmpty()) return getName() + " -> Arcane Blast -> No targets!";

        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" -> Arcane Blast -> All Enemies (ATK: ").append(getAttack()).append("):");

        for (Combatant e : alive) {
            int before = e.getCurrentHp();
            e.takeDamage(getAttack());
            int after = e.getCurrentHp();
            int dmg = before - after;

            sb.append("\n  ").append(e.getName())
              .append(" HP: ").append(before).append(" -> ").append(after)
              .append(" (dmg: ").append(getAttack()).append("-").append(e.getDefense())
              .append("=").append(dmg).append(")");

            if (!e.isAlive()) {
                sb.append(" X ELIMINATED");

                ArcaneBlastEffect existing = findArcaneBlastEffect();
                if (existing != null) {
                    existing.addBonus(10, this);
                } else {
                    applyEffect(new ArcaneBlastEffect(10));
                }

                sb.append(" | ATK: ").append(getAttack() - 10).append(" -> ").append(getAttack()).append(" (+10)");
            }
        }
        return sb.toString();
    }

    private ArcaneBlastEffect findArcaneBlastEffect() {
        for (IStatusEffect e : getActiveEffects()) {
            if (e instanceof ArcaneBlastEffect) return (ArcaneBlastEffect) e;
        }
        return null;
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "Deal BasicAttack damage to ALL enemies. Each kill adds +10 ATK for this level. Cooldown: 3 turns.";
    }
}
