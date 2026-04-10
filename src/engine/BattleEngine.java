package engine;

import strategy.ITurnOrderStrategy;
import model.Combatant;
import ui.UIDelegate;

import java.util.ArrayList;
import java.util.List;

public class BattleEngine {

  private final ITurnOrderStrategy turnOrderStrategy;
  private final UIDelegate ui;

  public BattleEngine(ITurnOrderStrategy turnOrderStrategy, UIDelegate ui) {
    this.turnOrderStrategy = turnOrderStrategy;
    this.ui = ui;
  }

  public BattleResult runBattle(Player player, Level level) {
        List<Combatant> initialEnemies = level.createInitialEnemies();
        List<Combatant> allCombatants = new ArrayList<>();
        allCombatants.add(player);
        allCombatants.addAll(initialEnemies);

        BattleContext ctx = new BattleContext(allCombatants);
        int round = 0;

        while (true) {
            round++;
            ctx.setRoundNumber(round);

            List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(ctx.getAliveCombatants());
            ui.printRoundHeader(round, player, ctx.getActiveEnemies());

            for (Combatant actor : turnOrder) {
                if (!actor.isAlive()) continue;

                if (actor.isStunned()) {
                    actor.tickEffects();
                    ui.print(actor.getName() + " -> STUNNED: Turn skipped.");
                    continue;
                }

                actor.tickEffects();

                if (actor instanceof Player) {
                    Player p = (Player) actor;
                    p.decrementCooldown();

                    IAction action = ui.promptPlayerAction(p, ctx);
                    action.execute(actor, null, ctx);
                    for (String entry : ctx.flushLog()) ui.print(entry);

                } else {
                    if (!player.isAlive()) continue;
                    IAction attack = new BasicAttackAction();
                    attack.execute(actor, player, ctx);
                    for (String entry : ctx.flushLog()) ui.print(entry);
                }

                if (!player.isAlive()) break;
            }

            ui.printEndOfRound(round, player, ctx.getAllCombatants());

            if (ctx.getActiveEnemies().isEmpty()) {
                List<Combatant> backup = level.tryBackupSpawn();
                if (backup != null && !backup.isEmpty()) {
                    ui.print("\nAll initial enemies eliminated -> Backup Spawn triggered!");
                    for (Combatant b : backup) {
                        ui.print("  " + b.getName() + " enters (HP: " + b.getCurrentHp() + ")");
                    }
                    ctx.addCombatants(backup);
                } else {
                    return buildResult(true, round, player, ctx.getAllCombatants());
                }
            }

            if (!player.isAlive()) {
                return buildResult(false, round, player, ctx.getAllCombatants());
            }
        }
    }

    private BattleResult buildResult(boolean won, int rounds, Player player, List<Combatant> all) {
        long enemiesRemaining = all.stream()
                .filter(c -> c instanceof Enemy && c.isAlive())
                .count();
        return new BattleResult(won, rounds, player.getCurrentHp(), player.getMaxHp(), (int) enemiesRemaining);
    }
}