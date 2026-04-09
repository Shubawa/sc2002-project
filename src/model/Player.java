package model;

import item.IItem;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Combatant {

    private final List<IItem> inventory = new ArrayList<>();
    private int specialSkillCooldown = 0;

    protected Player(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    public List<IItem> getInventory() {
        return inventory;
    }

    public void addItem(IItem item) {
        inventory.add(item);
    }

    public IItem consumeItem(IItem item) {
        inventory.remove(item);
        return item;
    }

    public boolean hasItems() {
        return !inventory.isEmpty();
    }

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    public void setSpecialSkillCooldown(int turns) {
        specialSkillCooldown = turns;
    }

    public void decrementCooldown() {
        if (specialSkillCooldown > 0) specialSkillCooldown--;
    }

    public boolean isSpecialSkillReady() {
        return specialSkillCooldown == 0;
    }

    public abstract String executeSpecialSkill(List<Combatant> enemies, Combatant target);
    public abstract String getSpecialSkillName();
    public abstract String getSpecialSkillDescription();

    @Override
    public void resetForLevel() {
        super.resetForLevel();
        specialSkillCooldown = 0;
    }
}
