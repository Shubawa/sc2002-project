package engine;

import model.Goblin;
import model.Wolf;

import java.util.Arrays;

public class MediumLevel extends Level {

    public MediumLevel() {
        super("Medium",
            () -> Arrays.asList(new Goblin("Goblin"), new Wolf("Wolf")),
            () -> Arrays.asList(new Wolf("Wolf"), new Wolf("Wolf"))
        );
    }
}
