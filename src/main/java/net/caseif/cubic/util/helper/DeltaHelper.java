package net.caseif.cubic.util.helper;

public class DeltaHelper {

    private static long delta;
    private static long lastTime = System.nanoTime();

    /**
     * Returns the delta time in seconds.
     *
     * @return The delta time in seconds.
     */
    public static float getDelta() {
        return delta / 1e6f;
    }

    /**
     * Updates the delta time.
     */
    public static void updateDelta() {
        long currentTime = System.nanoTime();
        delta = currentTime - lastTime;
        lastTime = currentTime;
    }

}
