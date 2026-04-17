# Turn-Based Combat Arena

A CLI turn-based combat game built in Java for the SC2002 Object-Oriented Design & Programming group assignment.

Choose a character (Warrior or Wizard), pick items, select a difficulty level, and battle through waves of enemies in a text-based arena.

## Prerequisites

- **Java JDK 8** or later (`javac` and `java` must be available on your PATH)

Verify your installation:

```bash
java -version
javac -version
```

## Project Structure

```
src/
├── Main.java                  # Entry point
├── ui/GameCLI.java            # CLI interface
├── engine/                    # Battle engine, levels, context
├── model/                     # Combatants (Player, Enemy, Warrior, Wizard, etc.)
├── action/                    # Actions (Attack, Defend, Item, Special Skill)
├── effect/                    # Status effects (Stun, Defend, Smoke Bomb)
├── item/                      # Usable items (Potion, Power Stone, Smoke Bomb)
└── strategy/                  # Turn-order strategies
```

## How to Run

### Windows

Double-click `run.bat`, or run from Command Prompt / PowerShell:

```bat
run.bat
```

Or compile and run manually:

```bat
mkdir out
javac -sourcepath src -d out src\Main.java src\action\*.java src\effect\*.java src\engine\*.java src\item\*.java src\model\*.java src\strategy\*.java src\ui\*.java
java -cp out Main
```

### macOS / Linux

From a terminal in the project root:

```bash
chmod +x run.sh
./run.sh
```

Or compile and run manually:

```bash
mkdir -p out
javac -sourcepath src -d out $(find src -name "*.java")
java -cp out Main
```

## Cleaning Build Output

To remove compiled class files:

```bash
# macOS / Linux
rm -rf out/

# Windows (Command Prompt)
rmdir /s /q out
```
