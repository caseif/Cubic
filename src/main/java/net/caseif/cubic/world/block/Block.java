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

package net.caseif.cubic.world.block;

import static net.caseif.cubic.world.World.CHUNK_LENGTH;
import static net.caseif.cubic.world.World.MAX_HEIGHT;

import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.math.vector.Vector3i;
import net.caseif.cubic.util.BoundingBox;
import net.caseif.cubic.world.Chunk;

import com.google.common.base.Preconditions;

import java.util.Optional;

public class Block {

    private static final Vector3f UNIT_SIZE = new Vector3f(1, 1, 1);

    private final Chunk owningChunk;
    private final Vector3i position;
    private final BlockType type;

    private final BoundingBox boundingBox;

    public Block(Chunk owningChunk, Vector3i position, BlockType type) {
        Preconditions.checkArgument(position.getY() >= 0 && position.getY() < MAX_HEIGHT,
                "Invalid y-coordinate (" + position.getY() + ") for block");
        this.position = position;
        this.owningChunk = owningChunk;
        this.type = type;

        this.boundingBox = BoundingBox.createFromMinCoord(position.asVector3f(), UNIT_SIZE);
    }

    public Chunk getOwningChunk() {
        return owningChunk;
    }

    public Vector3i getPosition() {
        return position;
    }

    public BlockType getType() {
        return type;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Optional<Block> getRelative(BlockFace face) {
        switch (face) {
            case TOP: {
                return position.getY() < MAX_HEIGHT - 1
                        ? getOwningChunk().getBlock(position.getX(), position.getY() + 1, position.getZ())
                        : Optional.empty();
            }
            case BOTTOM: {
                return position.getY() > 0
                        ? getOwningChunk().getBlock(position.getX(), position.getY() - 1, position.getZ())
                        : Optional.empty();
            }
            case LEFT: {
                if (position.getX() > 0) {
                    return getOwningChunk().getBlock(position.getX() - 1, position.getY(), position.getZ());
                } else {
                    return getOwningChunk().getWorld()
                            .getBlock(getPosition().getX() - 1, getPosition().getY(), getPosition().getZ());
                }
            }
            case RIGHT: {
                if (position.getX() < CHUNK_LENGTH - 1) {
                    return getOwningChunk().getBlock(position.getX() + 1, position.getY(), position.getZ());
                } else {
                    return getOwningChunk().getWorld()
                            .getBlock(getPosition().getX() + 1, getPosition().getY(), getPosition().getZ());
                }
            }
            case BACK: {
                if (position.getZ() > 0) {
                    return getOwningChunk().getBlock(position.getX(), position.getY(), position.getZ() - 1);
                } else {
                    return getOwningChunk().getWorld()
                            .getBlock(getPosition().getX(), getPosition().getY(), getPosition().getZ() - 1);
                }
            }
            case FRONT: {
                if (position.getZ() < CHUNK_LENGTH - 1) {
                    return getOwningChunk().getBlock(position.getX(), position.getY(), position.getZ() + 1);
                } else {
                    return getOwningChunk().getWorld()
                            .getBlock(getPosition().getX(), getPosition().getY(), getPosition().getZ() + 1);
                }
            }
            default:
                throw new AssertionError();
        }
    }

}
