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

import net.caseif.cubic.math.vector.Vector2i;
import net.caseif.cubic.world.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class World {

    public static final int MAX_HEIGHT = 128;
    public static final int CHUNK_LENGTH = 16;

    private String name;
    private Map<Vector2i, Chunk> chunkMap = new HashMap<>();

    public World(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Collection<Chunk> getChunks() {
        return chunkMap.values();
    }

    public Optional<Chunk> getChunk(Vector2i location) {
        return Optional.ofNullable(chunkMap.get(location));
    }

    public void addChunk(Chunk chunk) {
        checkArgument(!chunkMap.containsKey(chunk.getPosition()),
                "Chunk already exists at position " + chunk.getPosition() + " in world " + getName());
        chunkMap.put(chunk.getPosition(), chunk);
    }

    public void removeChunk(Vector2i position) {
        chunkMap.remove(position);
    }

    public Optional<Block> getBlock(int x, int y, int z) {
        int chunkX = x / CHUNK_LENGTH;
        int chunkZ = z / CHUNK_LENGTH;
        Optional<Chunk> c = getChunk(new Vector2i(chunkX, chunkZ));
        return c.isPresent() ? c.get().getBlock(x % CHUNK_LENGTH, y % MAX_HEIGHT, z % CHUNK_LENGTH) : Optional.empty();
    }

}
