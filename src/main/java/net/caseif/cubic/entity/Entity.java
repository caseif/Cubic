/*
 * Cubic
 * Copyright (c) 2016, Maxim Roncace <me@caseif.net>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.caseif.cubic.entity;

import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.timing.tick.TickManager;
import net.caseif.cubic.util.BoundingBox;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.Block;

import java.util.Optional;
import java.util.UUID;

public abstract class Entity {

    // type attributes
    private final EntityType type;
    private final float speed;

    private final UUID uuid = UUID.randomUUID(); //TODO: persist

    private World world;
    private Vector3f position;
    private Vector3f velocity = new Vector3f(0f, 0f, 0f);

    private BoundingBox boundingBox;

    protected Entity(EntityType type, float speed, World world, Vector3f position, Vector3f size) {
        this.type = type;
        this.speed = speed;

        this.world = world;
        this.position = position;

        this.boundingBox = BoundingBox.createFromCenterCoord(position, size);

        world.addEntity(this);
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public EntityType getType() {
        return type;
    }

    public float getSpeed() {
        return speed;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public void updatePosition() {
        doCollisionChecks();
        position = position.add(velocity.multiply(1f / TickManager.TICKS_PER_SECOND));
        getBoundingBox().setCenter(position);
    }

    private boolean doCollisionChecks() {
        int minX = (int) Math.floor(getBoundingBox().getMinCoord().getX() + velocity.getX());
        int maxX = (int) Math.floor(getBoundingBox().getMaxCoord().getX() + velocity.getX());

        int minY = (int) Math.floor(getBoundingBox().getMinCoord().getY() + velocity.getY());
        int maxY = (int) Math.floor(getBoundingBox().getMaxCoord().getY() + velocity.getY());

        int minZ = (int) Math.floor(getBoundingBox().getMinCoord().getZ() + velocity.getZ());
        int maxZ = (int) Math.floor(getBoundingBox().getMaxCoord().getZ() + velocity.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Optional<Block> block = getWorld().getBlock(x, y, z);
                    if (block.isPresent()) {
                        Vector3f newVel = getBoundingBox()
                                .testIntersectionWithVelocity(block.get().getBoundingBox(), velocity);
                        velocity = new Vector3f(velocity.getX() == 0 ? 0 : newVel.getX(),
                                velocity.getY() == 0 ? 0 : newVel.getY(), velocity.getZ() == 0 ? 0 : newVel.getZ());
                    }
                }
            }
        }
        return true;
    }

}
