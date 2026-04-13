package engine;

import model.Goblin;

import java.util.Arrays;

public class EasyLevel extends Level {

    public EasyLevel() {
        super("Easy",
            () -> Arrays.asList(new Goblin("Goblin A"), new Goblin("Goblin B"), new Goblin("Goblin C")),
            null
        );
    }
}
