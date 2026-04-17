package engine;

import model.Goblin;
import model.Wolf;

import java.util.Arrays;

public class MediumLevel extends Level {

    public MediumLevel() {
        super("Medium",
            () -> Arrays.asList(new Goblin("Goblin A"), new Wolf("Wolf A")),
            () -> Arrays.asList(new Wolf("Wolf B"), new Wolf("Wolf C"))
        );
    }
}
