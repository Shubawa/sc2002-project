package model;

import effect.IStatusEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {
  protected final String name;
  protected final int maxHp;
  protected int currentHp;
  protected int attack;
  protected final int baseDefense;
  protected int defense;
  protected final int speed;

  private final List<IStatusEffect> activeEffects = new ArrayList<>();
  private boolean invulnerable = false;

  protected Combatant(String name, int maxHp, int attack, int defense, int speed) {
      this.name = name;
      this.maxHp = maxHp;
      this.currentHp = maxHp;
      this.attack = attack;
      this.baseDefense = defense;
      this.defense = defense;
      this.speed = speed;
  }

  public String getName() {
      return name;
  }

  public int getMaxHp() {
      return maxHp;
  }

  public int getCurrentHp() {
      return currentHp;
  }

  public int getAttack() {
      return attack;
  }

  public int getDefense() {
      return defense;
  }

  public int getSpeed() {
      return speed;
  }

  public boolean isAlive() {
      return currentHp > 0;
  }

  public int takeDamage(int attackerAtk) {
      if (invulnerable) return 0;
      int dmg = Math.max(0, attackerAtk - defense);
      currentHp = Math.max(0, currentHp - dmg);
      return dmg;
  }

  public void heal(int amount) {
      currentHp = Math.min(maxHp, currentHp + amount);
  }

  public void applyEffect(IStatusEffect effect) {
      effect.onApply(this);
      activeEffects.add(effect);
  }

  /**
   * Called once per turn to advance all active status effects.
   * Each effect's remaining duration is decremented; expired effects
   * have their onExpire callback invoked (e.g. restoring buffed stats)
   * and are then removed from the active list.
   */
  public void tickEffects() {
      List<IStatusEffect> toRemove = new ArrayList<>();
      for (IStatusEffect e : activeEffects) {
          e.tick();
          if (e.isExpired()) {
              e.onExpire(this);
              toRemove.add(e);
          }
      }
      activeEffects.removeAll(toRemove);
  }

  public boolean isStunned() {
      return activeEffects.stream().anyMatch(e -> e.getName().equals("Stun"));
  }

  public boolean isInvulnerable() {
      return invulnerable;
  }

  public void setInvulnerable(boolean v) {
      invulnerable = v;
  }

  public List<IStatusEffect> getActiveEffects() {
      return activeEffects;
  }

  public void modifyDefense(int delta) {
      defense = Math.max(0, defense + delta);
  }

  public void modifyAttack(int delta) {
      attack = Math.max(0, attack + delta);
  }

  public void resetForLevel() {
      activeEffects.clear();
      defense = baseDefense;
      invulnerable = false;
  }

  @Override
  public String toString() {
      return name + " HP:" + currentHp + "/" + maxHp
              + " ATK:" + attack + " DEF:" + defense + " SPD:" + speed;
  }
}