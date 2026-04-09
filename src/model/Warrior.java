package model;

import effect.StunEffect;

import java.util.List;

public class Warrior extends Player {

    private static final int BASE_HP  = 260;
    private static final int BASE_ATK = 40;
    private static final int BASE_DEF = 20;
    private static final int BASE_SPD = 30;

    public Warrior(String instanceName) {
        super(instanceName, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
    }

    @Override
    public String executeSpecialSkill(List<Combatant> enemies, Combatant target) {
        if (target == null || !target.isAlive()) {
            return getName() + " -> Shield Bash -> no valid target!";
        }

        int before = target.getCurrentHp();
        target.takeDamage(getAttack());
        int after = target.getCurrentHp();
        int actualDmg = before - after;

        target.applyEffect(new StunEffect(2));

        String tag = !target.isAlive() ? " X ELIMINATED" : " STUNNED (2 turns)";
        return getName() + " -> Shield Bash -> " + target.getName()
                + ": HP: " + before + " -> " + after
                + " (dmg: " + getAttack() + "-" + target.getDefense() + "=" + actualDmg + ")"
                + tag;
    }

    @Override
    public String getSpecialSkillName() {
        return "Shield Bash";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "Deal BasicAttack damage to one enemy and Stun them for 2 turns. Cooldown: 3 turns.";
    }
}
