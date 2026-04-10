package engine;

public class BattleContext {

    private final StringBuilder log = new StringBuilder();

    public void log(String message) {
        log.append(message).append("\n");
    }

    public String getLog() {
        return log.toString();
    }

    public void clearLog() {
        log.setLength(0);
    }
}
