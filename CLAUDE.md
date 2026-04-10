# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

This is a plain Java project with no build tool. Compile and run from the `src/` directory:

```bash
# Compile all source files
javac -d out $(find src -name "*.java")

# Run the game
java -cp out ui.GameCLI
```

There is no test framework configured yet.

## Assignment Context

**Deadline: April 19, 11:59 PM** — submit as `LABGroup_AssignmentGrp.zip` (e.g., `FCS1_Grp1.zip`) to NTULearn. The zip must include: report (UML class diagram, UML sequence diagram, design write-up, reflection, GitHub link), class diagram image, sequence diagram image, and GitHub commit history.

Grading: UML Class Diagram [25], UML Sequence Diagram [20], Design Consideration [15], Implementation Code [20], Thinking/Process/Report [20].

## Architecture

This is a turn-based battle RPG (SC2002 course project) structured around the following layers:

### Package Overview

| Package | Role |
|---|---|
| `engine` | Core loop: `BattleEngine` drives rounds, `BattleContext` holds round-scoped state, `BattleResult` holds outcome data |
| `model` | `Combatant` (base) → `Player` (abstract) / `Enemy` (abstract); concrete classes: `Warrior`, `Wizard`, `Goblin`, `Wolf` |
| `action` | `IAction` interface; `BasicAttackAction` (in progress) — actions encapsulate what a combatant does on their turn |
| `effect` | `IStatusEffect` interface — status effects applied to combatants (e.g., `StunEffect`, `SmokeBombEffect`) |
| `item` | `IItem` interface; concrete: `Potion`, `PowerStone`, `SmokeBomb` — items are used by `Player` from inventory |
| `strategy` | `ITurnOrderStrategy` / `SpeedBasedTurnOrder` — determines round turn order from combatant speed stats |
| `ui` | `UIDelegate` interface (output abstraction); `GameCLI` (entry point, in progress) |

### Key Design Patterns

- **Strategy**: `ITurnOrderStrategy` is injected into `BattleEngine` — swap turn-order logic without touching the engine.
- **Delegate**: `UIDelegate` is injected into `BattleEngine` — the engine never prints directly, keeping output testable.
- **Template Method**: `Player` handles inventory/cooldown bookkeeping; subclasses implement `executeSpecialSkill()`.
- **BattleContext**: passed into `IItem.use(...)` so items can log messages without coupling to `UIDelegate` directly.

### SOLID Requirements (Graded)

- **SRP**: Each class has one responsibility.
- **OCP**: New `Action` or `StatusEffect` must be addable without modifying `BattleEngine`.
- **LSP**: `Player` and `Enemy` are interchangeable as `Combatant`.
- **ISP**: No bloated interfaces.
- **DIP**: `BattleEngine` depends on abstractions (`IAction`, `ITurnOrderStrategy`, `UIDelegate`), not concrete classes.

## Game Rules & Combat Logic

### Damage Formula

`Damage = max(0, AttackerATK − TargetDEF)` — HP is clamped to 0, never negative.

### Turn Order Per Round

Turn order is determined by speed (higher SPD goes first). Combatants with equal speed both appear in the order list (the spec doesn't define a tiebreaker). Dead combatants are skipped.

### Within a Single Turn (order matters)

1. Apply existing status effects (`tickEffects`) first.
2. Check if combatant is alive — skip if dead.
3. Check if combatant is stunned — skip action, decrement stun.
4. Execute the combatant's action.
5. After all turns: check win/loss condition. If all enemies in current wave are defeated and a backup wave exists, spawn it (they appear at start of the next round).

### Player Actions (choose one per turn)

| Action | Details |
|---|---|
| `BasicAttack` | `Damage = max(0, ATK − targetDEF)`. Player selects target. |
| `Defend` | +10 DEF for the current round and the next round (`DefendEffect`, 2 turns). |
| `Item` | Use one item from inventory (single-use; action unavailable once all items consumed). |
| `SpecialSkill` | Execute class special ability. **Cooldown: 3 turns including the current round** (usable again 3 turns later). Cooldown only decrements when the combatant actually takes a turn — stunned turns do NOT decrement cooldown. |

Enemies always execute `BasicAttack` only.

### Status Effects

| Effect | Behaviour |
|---|---|
| `StunEffect` | Affected combatant skips their turn for the current turn + the next turn (2 turns total). Cooldown does NOT tick while stunned. |
| `SmokeBombEffect` | Enemy attacks deal 0 damage to the player for 2 turns (current + next). |
| `DefendEffect` | +10 DEF for 2 turns. |

### Items (two chosen at game start, duplicates allowed)

| Item | Effect |
|---|---|
| `Potion` | `newHP = min(currentHP + 100, maxHP)` |
| `Power Stone` | Triggers the special skill once for free; **does not start or change the cooldown**. |
| `Smoke Bomb` | Enemy attacks deal 0 damage for 2 turns (`SmokeBombEffect`). |

### Levels & Difficulty

| Level | Difficulty | Initial Spawn | Backup Spawn |
|---|---|---|---|
| 1 | Easy | 3 Goblins | — |
| 2 | Medium | 1 Goblin + 1 Wolf | 2 Wolves |
| 3 | Hard | 2 Goblins | 1 Goblin + 2 Wolves |

**Backup spawn rules**: triggers when ALL enemies in the initial (or current) wave are eliminated. All backup entities enter simultaneously at the start of the next round. Only one backup wave per level.

### Win/Loss & End Screens

- **Win**: all enemies defeated → show "Congratulations..."; stats: Remaining HP / Total Rounds.
- **Loss**: player HP reaches 0 → show "Defeated. Don't give up, try again!"; stats: Enemies Remaining / Total Rounds Survived. Offer: replay with same settings, new game, or exit.

### Game Setup (Loading Screen)

Player selects: character (Warrior or Wizard), two items (duplicates allowed), difficulty level. Display all combatant attributes before selection.

## Combatant Stats

All combatants have: `name`, `maxHp`, `currentHp`, `attack`, `defense`, `speed`. Key methods expected: `isAlive()`, `isStunned()`, `takeDamage(int)`, `heal(int)`, `applyEffect(IStatusEffect)`, `tickEffects()`, `modifyAttack(int)`, `resetForLevel()`.

### Player Classes

| Class | HP | ATK | DEF | SPD | Special Skill |
|---|---|---|---|---|---|
| Warrior | 260 | 40 | 20 | 30 | **Shield Bash** — BasicAttack damage to one target + Stun 2 turns; cooldown 3 |
| Wizard | 200 | 50 | 10 | 20 | **Arcane Blast** — BasicAttack damage to ALL living enemies; each kill grants +10 ATK for the rest of the level; cooldown 3 |

### Enemy Classes

| Class | HP | ATK | DEF | SPD |
|---|---|---|---|---|
| Goblin | 55 | 35 | 15 | 25 |
| Wolf | 40 | 45 | 5 | 35 |

## What Is Still Incomplete

- `Combatant` base class body (currently empty)
- `BasicAttackAction` body
- `SpeedBasedTurnOrder.determineTurnOrder()` body
- `BattleEngine.runBattle()` player/enemy turn branches
- `UIDelegate` commented-out methods (waiting on `Combatant` model)
- `GameCLI` entry point
- `IStatusEffect` implementations (`StunEffect`, `SmokeBombEffect`, `DefendEffect`) — not yet created as files
- `BattleContext` class — not yet created as a file
