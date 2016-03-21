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

import net.caseif.cubic.math.vector.Vector2f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class World {

    static final int MAX_HEIGHT = 128;
    static final int CHUNK_LENGTH = 16;

    private String name;
    private Map<Vector2f, Chunk> chunkMap = new HashMap<>();

    public World(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<Vector2f, Chunk> getChunkMap() {
        return chunkMap;
    }

    public Collection<Chunk> getChunks() {
        return getChunkMap().values();
    }

    public Chunk getChunk(Vector2f location) {
        return getChunkMap().get(location);
    }

    public void addChunk(Chunk chunk) {
        checkArgument(!chunkMap.containsKey(chunk.getPosition()),
                "Chunk already exists at position " + chunk.getPosition() + " in world " + getName());
        chunkMap.put(chunk.getPosition(), chunk);
    }

    public void removeChunk(Vector2f position) {
        chunkMap.remove(position);
    }

}
