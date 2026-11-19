package game;

public final class GameRulesTests {

    private static int checks;

    private GameRulesTests() {
    }

    public static void main(String[] args) {
        achievementRewardPuzzle();
        stealPayoutPuzzle();
        pursuitBoundsPuzzle();
        System.out.println("Passed " + checks + " headless gameplay checks.");
    }

    private static void achievementRewardPuzzle() {
        assertFalse("reward stays locked without achievements",
                GameRules.shouldSpawnAchievementCharacter(false, false, false));
        assertFalse("both achievements are required",
                GameRules.shouldSpawnAchievementCharacter(true, false, false));
        assertFalse("the capture achievement is required",
                GameRules.shouldSpawnAchievementCharacter(false, true, false));
        assertTrue("reward unlocks when both achievements are earned",
                GameRules.shouldSpawnAchievementCharacter(true, true, false));
        assertFalse("reward is not spawned twice",
                GameRules.shouldSpawnAchievementCharacter(true, true, true));
    }

    private static void stealPayoutPuzzle() {
        assertEquals("zero threshold always pays the steal rate", 30,
                GameRules.stealAmount(30, 0, 0));
        assertEquals("zero threshold remains safe at the highest roll", 30,
                GameRules.stealAmount(30, 0, 8));
        assertEquals("a roll below the threshold fails", 0,
                GameRules.stealAmount(30, 3, 2));
        assertEquals("a roll at the threshold succeeds", 10,
                GameRules.stealAmount(30, 3, 3));
        assertEquals("maximum threshold pays the reduced amount", 5,
                GameRules.stealAmount(40, 8, 8));
    }

    private static void pursuitBoundsPuzzle() {
        assertEquals("top-left scan starts at zero", 0, GameRules.scanStart(0, 30));
        assertEquals("top-left row scan ends within the map", 30,
                GameRules.scanEnd(0, 30, 61));
        assertEquals("top-left column scan ends within the map", 30,
                GameRules.scanEnd(0, 30, 81));
        assertEquals("bottom-right row scan starts at the radius", 30,
                GameRules.scanStart(60, 30));
        assertEquals("bottom-right row scan is clamped", 60,
                GameRules.scanEnd(60, 30, 61));
        assertEquals("bottom-right column scan starts at the radius", 50,
                GameRules.scanStart(80, 30));
        assertEquals("bottom-right column scan is clamped", 80,
                GameRules.scanEnd(80, 30, 81));
        assertEquals("interior scans keep their requested radius", 10,
                GameRules.scanStart(40, 30));
        assertEquals("interior scans keep their requested end", 70,
                GameRules.scanEnd(40, 30, 81));
    }

    private static void assertTrue(String puzzle, boolean actual) {
        checks++;
        if (!actual) {
            throw new AssertionError(puzzle);
        }
    }

    private static void assertFalse(String puzzle, boolean actual) {
        assertTrue(puzzle, !actual);
    }

    private static void assertEquals(String puzzle, int expected, int actual) {
        checks++;
        if (expected != actual) {
            throw new AssertionError(puzzle + ": expected " + expected + ", got " + actual);
        }
    }
}
