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

import static org.lwjgl.opengl.GL15.glGenBuffers;

import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.world.block.Block;

public class Chunk {

    private int vboHandle = -1;
    private boolean dirty = true;

    private World world;
    private Vector2f position;

    private Block[][][] blocks = new Block[World.CHUNK_LENGTH][World.MAX_HEIGHT][World.CHUNK_LENGTH];

    public Chunk(World world, Vector2f position) {
        this.world = world;
        this.position = position;
    }

    public World getWorld() {
        return world;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public Vector2f getMinWorldPosition() {
        return position.multiply(World.CHUNK_LENGTH);
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
