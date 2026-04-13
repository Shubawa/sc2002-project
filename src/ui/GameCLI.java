package ui;

import action.*;
import model.*;
import effect.IStatusEffect;
import engine.*;
import item.*;
import strategy.SpeedBasedTurnOrder;

import java.util.*;

public class GameCLI implements BattleEngine.UIDelegate {

    private static final String SEP  = "=".repeat(60);
    private static final String DASH = "-".repeat(60);

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        printTitle();
        boolean keepPlaying = true;
        while (keepPlaying) {
            keepPlaying = mainMenu();
        }
        System.out.println("\nThanks for playing! Goodbye.\n");
    }

    private boolean mainMenu() {
        System.out.println(SEP);
        System.out.println("           TURN-BASED COMBAT ARENA");
        System.out.println(SEP);
        System.out.println("  [1] New Game");
        System.out.println("  [2] Exit");
        System.out.println(DASH);
        int choice = promptInt("Choose: ", 1, 2);
        if (choice == 2) return false;
        return startNewGame();
    }

    private boolean startNewGame() {
        Player player = selectCharacter();
        List<Integer> originalItemChoices = selectItems(player);
        Level level = selectLevel();

        BattleEngine engine = new BattleEngine(new SpeedBasedTurnOrder(), this);
        print("\n" + SEP);
        print("Let the battle begin!");
        print(SEP + "\n");

        BattleResult result = engine.runBattle(player, level);
        showResult(result, player);
        return postGameMenu(player, level, originalItemChoices);
    }

    private Player selectCharacter() {
        System.out.println("\n" + SEP);
        System.out.println("  CHARACTER SELECT");
        System.out.println(SEP);
        System.out.println("  [1] Warrior");
        System.out.printf("      HP: 260 | ATK: 40 | DEF: 20 | SPD: 30%n");
        System.out.printf("      Special: Shield Bash - Damage + Stun one enemy (2 turns). CD: 3%n");
        System.out.println();
        System.out.println("  [2] Wizard");
        System.out.printf("      HP: 200 | ATK: 50 | DEF: 10 | SPD: 20%n");
        System.out.printf("      Special: Arcane Blast - Damage ALL enemies. +10 ATK per kill. CD: 3%n");
        System.out.println(DASH);

        int choice = promptInt("Choose character: ", 1, 2);
        if (choice == 1) {
            System.out.println("  > Warrior selected.\n");
            return new Warrior("Warrior");
        } else {
            System.out.println("  > Wizard selected.\n");
            return new Wizard("Wizard");
        }
    }

    private List<Integer> selectItems(Player player) {
        List<Integer> chosenCodes = new ArrayList<>();

        System.out.println(SEP);
        System.out.println("  ITEM SELECT  (choose 2 items, duplicates allowed)");
        System.out.println(SEP);
        System.out.println("  [1] Potion      - Heal 100 HP (capped at max HP).");
        System.out.println("  [2] Power Stone - Free extra Special Skill use. Cooldown unchanged.");
        System.out.println("  [3] Smoke Bomb  - Enemy attacks deal 0 damage for 2 turns.");
        System.out.println(DASH);

        for (int i = 1; i <= 2; i++) {
            int choice = promptInt("Pick item " + i + ": ", 1, 3);
            chosenCodes.add(choice);
            player.addItem(createItem(choice));
        }

        System.out.print("  Items chosen: ");
        for (IItem item : player.getInventory()) System.out.print("[" + item.getName() + "] ");
        System.out.println("\n");

        return chosenCodes;
    }

    private IItem createItem(int choice) {
        switch (choice) {
            case 1:  return new Potion();
            case 2:  return new PowerStone();
            case 3:  return new SmokeBomb();
            default: return new Potion();
        }
    }

    private Level selectLevel() {
        System.out.println(SEP);
        System.out.println("  LEVEL SELECT");
        System.out.println(SEP);
        System.out.println("  Enemies:");
        System.out.println("    Goblin - HP:55 ATK:35 DEF:15 SPD:25");
        System.out.println("    Wolf   - HP:40 ATK:45 DEF:5  SPD:35");
        System.out.println();

        Level[] levels = { new EasyLevel(), new MediumLevel(), new HardLevel() };
        System.out.println("  [1] Easy   - Initial: 3 Goblins");
        System.out.println("  [2] Medium - Initial: 1 Goblin + 1 Wolf | Backup: 2 Wolves");
        System.out.println("  [3] Hard   - Initial: 2 Goblins | Backup: 1 Goblin + 2 Wolves");
        System.out.println(DASH);

        int choice = promptInt("Choose level: ", 1, 3);
        Level level = levels[choice - 1];
        System.out.println("  > " + level.getName() + " selected.\n");
        return level;
    }

    @Override
    public void print(String line) {
        System.out.println(line);
    }

    @Override
    public void printRoundHeader(int round, Player player, List<Combatant> enemies) {
        System.out.println(SEP);
        System.out.println("  ROUND " + round);
        System.out.println(DASH);
        printCombatantStatus(player);
        for (Combatant e : enemies) printCombatantStatus(e);
        System.out.println(DASH);
    }

    @Override
    public void printEndOfRound(int round, Player player, List<Combatant> allEnemies) {
        System.out.println(DASH);
        System.out.print("  End of Round " + round + ": " + player.getName()
                + " HP: " + player.getCurrentHp() + "/" + player.getMaxHp());

        for (Combatant e : allEnemies) {
            if (e instanceof Enemy) {
                System.out.print(" | " + e.getName() + " HP: "
                        + (e.isAlive() ? e.getCurrentHp() : "X"));
                if (e.isAlive() && e.isStunned())      System.out.print(" [STUNNED]");
                if (e.isAlive() && e.isInvulnerable()) System.out.print(" [INVU]");
            }
        }

        for (IItem item : player.getInventory()) {
            System.out.print(" | " + item.getName() + ": 1");
        }

        System.out.print(" | CD: " + player.getSpecialSkillCooldown());
        System.out.println("\n");
    }

    @Override
    public IAction promptPlayerAction(Player player, BattleContext ctx) {
        System.out.println(SEP);
        System.out.println("  " + player.getName() + "'s Turn - Choose Action:");
        System.out.println(DASH);

        List<String> options = new ArrayList<>();
        options.add("Basic Attack");
        options.add("Defend");
        if (player.hasItems()) options.add("Use Item");
        boolean skillReady = player.isSpecialSkillReady();
        options.add("Special Skill: " + player.getSpecialSkillName()
                + (skillReady ? " (Ready)" : " [CD: " + player.getSpecialSkillCooldown() + "]"));

        for (int i = 0; i < options.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + options.get(i));
        }
        System.out.println(DASH);

        int choice = promptInt("Choose: ", 1, options.size());
        String chosen = options.get(choice - 1);

        if (chosen.equals("Basic Attack")) {
            Combatant target = pickEnemy(ctx.getActiveEnemies(), "Attack which enemy?");
            return wrapAction(new BasicAttackAction(), target);

        } else if (chosen.equals("Defend")) {
            return new DefendAction();

        } else if (chosen.equals("Use Item")) {
            return buildItemAction(player, ctx);

        } else {
            if (!skillReady) {
                System.out.println("  [!] Special Skill is on cooldown!");
                return promptPlayerAction(player, ctx);
            }
            return buildSpecialSkillAction(player, ctx);
        }
    }

    private IAction wrapAction(BasicAttackAction base, Combatant target) {
        return new IAction() {
            @Override
            public void execute(Combatant actor, Combatant t, BattleContext c) {
                base.execute(actor, target, c);
            }

            @Override
            public String getName() {
                return base.getName();
            }
        };
    }

    private IAction buildItemAction(Player player, BattleContext ctx) {
        List<IItem> inv = player.getInventory();
        System.out.println("  Use which item?");
        for (int i = 0; i < inv.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + inv.get(i).getName()
                    + " - " + inv.get(i).getDescription());
        }
        System.out.println("  [" + (inv.size() + 1) + "] Back");
        int choice = promptInt("Choose: ", 1, inv.size() + 1);
        if (choice == inv.size() + 1) return promptPlayerAction(player, ctx);

        IItem chosen = inv.get(choice - 1);
        Combatant target = null;

        if (chosen instanceof PowerStone) {
            target = buildSpecialSkillTarget(player, ctx);
        }

        final Combatant finalTarget = target;
        final IItem finalItem = chosen;
        return new IAction() {
            @Override
            public void execute(Combatant actor, Combatant t, BattleContext c) {
                new ItemAction(finalItem, finalTarget).execute(actor, t, c);
            }

            @Override
            public String getName() {
                return "Item: " + finalItem.getName();
            }
        };
    }

    private IAction buildSpecialSkillAction(Player player, BattleContext ctx) {
        Combatant target = buildSpecialSkillTarget(player, ctx);
        final Combatant finalTarget = target;
        return new IAction() {
            @Override
            public void execute(Combatant actor, Combatant t, BattleContext c) {
                new SpecialSkillAction(finalTarget).execute(actor, t, c);
            }

            @Override
            public String getName() {
                return "Special Skill";
            }
        };
    }

    private Combatant buildSpecialSkillTarget(Player player, BattleContext ctx) {
        if (player instanceof Warrior) {
            return pickEnemy(ctx.getActiveEnemies(), "Shield Bash which enemy?");
        }
        return null;
    }

    private void printCombatantStatus(Combatant c) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(c.getName())
          .append("  HP: ").append(c.getCurrentHp()).append("/").append(c.getMaxHp())
          .append("  ATK: ").append(c.getAttack())
          .append("  DEF: ").append(c.getDefense())
          .append("  SPD: ").append(c.getSpeed());

        List<IStatusEffect> effects = c.getActiveEffects();
        if (!effects.isEmpty()) {
            sb.append("  [");
            for (IStatusEffect e : effects) sb.append(e.getName()).append(" ");
            sb.append("]");
        }
        if (c instanceof Player) {
            Player p = (Player) c;
            sb.append("  CD: ").append(p.getSpecialSkillCooldown());
            if (p.hasItems()) {
                sb.append("  Items: ");
                for (IItem i : p.getInventory()) sb.append("[").append(i.getName()).append("]");
            }
        }
        System.out.println(sb);
    }

    private Combatant pickEnemy(List<Combatant> enemies, String prompt) {
        System.out.println("  " + prompt);
        for (int i = 0; i < enemies.size(); i++) {
            Combatant e = enemies.get(i);
            System.out.println("  [" + (i + 1) + "] " + e.getName()
                    + " HP: " + e.getCurrentHp() + "/" + e.getMaxHp()
                    + (e.isStunned() ? " [STUNNED]" : ""));
        }
        int ch = promptInt("Target: ", 1, enemies.size());
        return enemies.get(ch - 1);
    }

    private int promptInt(String msg, int min, int max) {
        while (true) {
            System.out.print("  " + msg);
            try {
                String line = scanner.nextLine().trim();
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) return val;
                System.out.println("  [!] Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Enter a number.");
            }
        }
    }

    private void printTitle() {
        System.out.println();
        System.out.println(SEP);
        System.out.println("     *** TURN-BASED COMBAT ARENA ***");
        System.out.println("       SC2002 Group Assignment - Java OODP");
        System.out.println(SEP);
        System.out.println();
    }

    private void showResult(BattleResult result, Player player) {
        System.out.println("\n" + SEP);
        if (result.isPlayerWon()) {
            System.out.println("  CONGRATULATIONS! You have defeated all your enemies!");
            System.out.println(DASH);
            System.out.println("  Remaining HP  : " + result.getRemainingHp() + " / " + result.getMaxHp());
            System.out.println("  Total Rounds  : " + result.getTotalRounds());
            if (player.hasItems()) {
                System.out.print("  Unused Items  : ");
                player.getInventory().forEach(i -> System.out.print("[" + i.getName() + "] "));
                System.out.println();
            }
        } else {
            System.out.println("  DEFEATED. Don't give up, try again!");
            System.out.println(DASH);
            System.out.println("  Enemies remaining : " + result.getEnemiesRemaining());
            System.out.println("  Rounds survived   : " + result.getTotalRounds());
        }
        System.out.println(SEP + "\n");
    }

    private boolean postGameMenu(Player lastPlayer, Level lastLevel, List<Integer> originalItemChoices) {
        System.out.println("  What would you like to do?");
        System.out.println("  [1] Replay with same settings");
        System.out.println("  [2] New Game (return to home screen)");
        System.out.println("  [3] Exit");
        System.out.println(DASH);
        int ch = promptInt("Choose: ", 1, 3);
        switch (ch) {
            case 1:
                Player fresh;
                if (lastPlayer instanceof Warrior) fresh = new Warrior("Warrior");
                else                                fresh = new Wizard("Wizard");
                for (int code : originalItemChoices) {
                    fresh.addItem(createItem(code));
                }
                Level newLevel = createLevel(lastLevel);
                BattleEngine engine = new BattleEngine(new SpeedBasedTurnOrder(), this);
                BattleResult result = engine.runBattle(fresh, newLevel);
                showResult(result, fresh);
                return postGameMenu(fresh, newLevel, originalItemChoices);
            case 2:
                return true;
            default:
                return false;
        }
    }

    private Level createLevel(Level lastLevel) {
        if (lastLevel instanceof MediumLevel) return new MediumLevel();
        if (lastLevel instanceof HardLevel)   return new HardLevel();
        return new EasyLevel();
    }
}
