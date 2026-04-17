package engine;

import model.Goblin;
import model.Wolf;

import java.util.Arrays;

public class HardLevel extends Level {

    public HardLevel() {
        super("Hard",
            () -> Arrays.asList(new Goblin("Goblin A"), new Goblin("Goblin B")),
            () -> Arrays.asList(new Goblin("Goblin C"), new Wolf("Wolf A"), new Wolf("Wolf B"))
        );
    }
}
