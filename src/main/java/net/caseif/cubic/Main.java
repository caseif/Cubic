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

package net.caseif.cubic;

import net.caseif.cubic.gl.GraphicsMain;
import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.world.Chunk;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.Block;
import net.caseif.cubic.world.block.BlockType;

public class Main {

    public static World world;

    public static void main(String[] args) {
        createDummyWorld();
        initGraphicsThread();
    }

    private static void initGraphicsThread() {
        Thread t = new Thread(new GraphicsMain());
        t.start();
    }

    private static void createDummyWorld() {
        world = new World("world");
        Chunk chunk = new Chunk(world, new Vector2f(0, 0));
        world.addChunk(chunk);
        chunk.getBlocks()[0][0][0] = new Block(chunk, new Vector3f(0, 0, 0), BlockType.STONE);
    }

}
