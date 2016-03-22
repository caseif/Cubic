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

package net.caseif.cubic.world;

import static com.google.common.base.Preconditions.checkArgument;
import static net.caseif.cubic.world.World.CHUNK_LENGTH;
import static net.caseif.cubic.world.World.MAX_HEIGHT;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import net.caseif.cubic.math.vector.Vector2i;
import net.caseif.cubic.world.block.Block;

import java.util.Optional;

public class Chunk {

    private int vboHandle = -1;
    private boolean dirty = true;

    private World world;
    private Vector2i position;

    private Block[][][] blocks = new Block[CHUNK_LENGTH][World.MAX_HEIGHT][CHUNK_LENGTH];

    public Chunk(World world, Vector2i position) {
        this.world = world;
        this.position = position;
    }

    public World getWorld() {
        return world;
    }

    public Vector2i getPosition() {
        return position;
    }

    public Optional<Block> getBlock(int x, int y, int z) {
        checkArgument(x >= 0 && x < CHUNK_LENGTH
                        && z >= 0 && z < CHUNK_LENGTH
                        && y >= 0 && y < MAX_HEIGHT,
                "Invalid coordinates (" + x + ", " + y + ", " + z + ") passed to Chunk#getBlock");
        return Optional.ofNullable(blocks[x][y][z]);
    }

    public void addBlock(Block block) {
        int x = block.getPosition().getX();
        int y = block.getPosition().getY();
        int z = block.getPosition().getZ();
        checkArgument(x / CHUNK_LENGTH == position.getX() && z / CHUNK_LENGTH == position.getY(),
                "Invalid block at (" + x + ", " + y + ", " + z
                        + ") added to chunk at (" + position.getX() + ", " + position.getY() + ")");
        blocks[x % CHUNK_LENGTH][y][z % CHUNK_LENGTH] = block;
    }

    public Vector2i getMinWorldPosition() {
        return position.multiply(CHUNK_LENGTH);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public int getVboHandle() {
        if (vboHandle == -1) {
            vboHandle = glGenBuffers();
        }
        return vboHandle;
    }

}
