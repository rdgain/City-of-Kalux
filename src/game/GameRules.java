package game;

final class GameRules {

    private GameRules() {
    }

    static boolean shouldSpawnAchievementCharacter(boolean moneyAchievement,
                                                    boolean captureAchievement,
                                                    boolean alreadySpawned) {
        return moneyAchievement && captureAchievement && !alreadySpawned;
    }

    static int stealAmount(int stealRate, int successThreshold, int roll) {
        if (roll < successThreshold) {
            return 0;
        }
        return stealRate / Math.max(1, successThreshold);
    }

    static int scanStart(int position, int radius) {
        return Math.max(0, position - radius);
    }

    static int scanEnd(int position, int radius, int extent) {
        return Math.min(extent - 1, position + radius);
    }
}
