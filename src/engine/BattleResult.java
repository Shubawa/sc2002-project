package engine;

public class BattleResult {

    private final boolean playerWon;
    private final int totalRounds;
    private final int remainingHp;
    private final int maxHp;
    private final int enemiesRemaining;

    public BattleResult(boolean playerWon, int totalRounds,
                        int remainingHp, int maxHp, int enemiesRemaining) {
        this.playerWon = playerWon;
        this.totalRounds = totalRounds;
        this.remainingHp = remainingHp;
        this.maxHp = maxHp;
        this.enemiesRemaining = enemiesRemaining;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getRemainingHp() {
        return remainingHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getEnemiesRemaining() {
        return enemiesRemaining;
    }
}
