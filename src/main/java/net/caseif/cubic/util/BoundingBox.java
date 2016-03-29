package net.caseif.cubic.util;

import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.timing.tick.TickManager;

public class BoundingBox {

    private Vector3f minCoord;
    private Vector3f maxCoord;
    private Vector3f size;

    private BoundingBox(Vector3f coord, Vector3f size, boolean isCenterCoord) {
        this.size = size;
        if (isCenterCoord) {
            updateMinCoord(coord);
        } else {
            this.minCoord = coord;
        }
        updateMaxCoord();
    }

    public static BoundingBox createFromCenterCoord(Vector3f center, Vector3f size) {
        return new BoundingBox(center, size, true);
    }

    public static BoundingBox createFromMinCoord(Vector3f minCoord, Vector3f size) {
        return new BoundingBox(minCoord, size, false);
    }

    public Vector3f getSize() {
        return size;
    }

    public Vector3f getMinCoord() {
        return minCoord;
    }

    public Vector3f getMaxCoord() {
        return maxCoord;
    }

    private void updateMinCoord(Vector3f center) {
        minCoord = center.add(size.multiply(-0.5f));
    }

    private void updateMaxCoord() {
        maxCoord = minCoord.add(size);
    }

    public void setCenter(Vector3f center) {
        updateMinCoord(center);
        updateMaxCoord();
    }

    public Vector3f testIntersectionWithVelocity(BoundingBox bb, Vector3f velocity) {
        float vX = velocity.getX();
        float vY = velocity.getY();
        float vZ = velocity.getZ();

        velocity = velocity.multiply(1f / TickManager.TICKS_PER_SECOND);

        Vector3f origMin = minCoord;
        Vector3f origMax = maxCoord;

        minCoord = minCoord.add(velocity.getX(), 0f, 0f);
        updateMaxCoord();
        if (intersects(bb)) {
            vX = 0;
        }
        minCoord = origMin;
        maxCoord = origMax;

        minCoord = minCoord.add(0f, velocity.getY(), 0f);
        updateMaxCoord();
        if (intersects(bb)) {
            vY = 0;
        }
        minCoord = origMin;
        maxCoord = origMax;

        minCoord = minCoord.add(0f, 0f, velocity.getZ());
        updateMaxCoord();
        if (intersects(bb)) {
            vZ = 0;
        }
        minCoord = origMin;
        maxCoord = origMax;

        return new Vector3f(vX, vY, vZ);
    }

    public boolean intersects(BoundingBox bb) {
        return     intersects(minCoord.getX(), maxCoord.getX(), bb.minCoord.getX(), bb.maxCoord.getX())  // x-axis check
                && intersects(minCoord.getY(), maxCoord.getY(), bb.minCoord.getY(), bb.maxCoord.getY())  // y-axis check
                && intersects(minCoord.getZ(), maxCoord.getZ(), bb.minCoord.getZ(), bb.maxCoord.getZ()); // z-axis check
    }

    /**
     * 1-dimensional AABB check.
     *
     * @param aMin Minimum bounding coord for object A
     * @param aMax Maximum bounding coord for object A
     * @param bMin Minimum bounding coord for object B
     * @param bMax Maximum bounding coord for object B
     * @return Whether the objects intersect
     */
    private boolean intersects(float aMin, float aMax, float bMin, float bMax) {
        return (aMin >= bMin && aMin <= bMax) || (aMax >= bMin && aMax <= bMax);
    }

}
