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
import net.caseif.cubic.util.helper.DeltaHelper;
import net.caseif.cubic.world.World;

import java.util.UUID;

public abstract class Entity {

    // type attributes
    private final EntityType type;
    private final float speed;

    private final UUID uuid = UUID.randomUUID(); //TODO: persist

    private World world;
    private Vector3f position;
    private Vector3f velocity = new Vector3f(0f, 0f, 0f);

    protected Entity(EntityType type, float speed, World world, Vector3f position) {
        this.type = type;
        this.speed = speed;

        this.world = world;
        this.position = position;
        world.addEntity(this);
    }

    public UUID getUniqueId() {
        return uuid;
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

    public void updatePosition() {
        doCollisionChecks();
        position = position.add(velocity.multiply(DeltaHelper.getDelta()));
    }

    private void doCollisionChecks() {
        // check x-velocity
        if (world.getBlock(position.getX() + velocity.getX(), position.getY(), position.getZ()).isPresent()) {
            velocity = new Vector3f(0f, velocity.getY(), velocity.getZ());
        }
        // check y-velocity
        if (world.getBlock(position.getX(), position.getY() + velocity.getY(), position.getZ()).isPresent()) {
            velocity = new Vector3f(velocity.getX(), 0f, velocity.getZ());
        }
        // check z-velocity
        if (world.getBlock(position.getX(), position.getY(), position.getZ() + velocity.getZ()).isPresent()) {
            velocity = new Vector3f(velocity.getX(), velocity.getY(), 0f);
        }
    }

}
