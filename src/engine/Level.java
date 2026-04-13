package engine;

import model.Combatant;

import java.util.List;
import java.util.function.Supplier;

public abstract class Level {

    private final String name;
    private final Supplier<List<Combatant>> initialSpawnFactory;
    private final Supplier<List<Combatant>> backupSpawnFactory;
    private boolean backupSpawned = false;

    protected Level(String name,
                    Supplier<List<Combatant>> initialSpawnFactory,
                    Supplier<List<Combatant>> backupSpawnFactory) {
        this.name = name;
        this.initialSpawnFactory = initialSpawnFactory;
        this.backupSpawnFactory = backupSpawnFactory;
    }

    public String getName() {
        return name;
    }

    public List<Combatant> createInitialEnemies() {
        return initialSpawnFactory.get();
    }

    public List<Combatant> tryBackupSpawn() {
        if (backupSpawnFactory != null && !backupSpawned) {
            backupSpawned = true;
            return backupSpawnFactory.get();
        }
        return null;
    }

    public boolean hasBackup() {
        return backupSpawnFactory != null;
    }

    public boolean isBackupSpawned() {
        return backupSpawned;
    }
}
