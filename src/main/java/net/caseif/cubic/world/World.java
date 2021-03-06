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

import net.caseif.cubic.entity.Entity;
import net.caseif.cubic.math.vector.Vector2i;
import net.caseif.cubic.timing.tick.TickManager;
import net.caseif.cubic.world.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class World {

    public static final int MAX_HEIGHT = 128;
    public static final int CHUNK_LENGTH = 16;

    private String name;

    private final TickManager tickManager = new TickManager(this);
    private Map<Vector2i, Chunk> chunkMap = new HashMap<>();
    private Set<Entity> entities = new HashSet<>();

    public World(String name) {
        this.name = name;
        tickManager.start();
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
        checkArgument(y >= 0 && y < MAX_HEIGHT, "Invalid y-coordinate passed to World#getBlock");
        int chunkX = x / CHUNK_LENGTH;
        int chunkZ = z / CHUNK_LENGTH;
        Optional<Chunk> c = getChunk(new Vector2i(chunkX, chunkZ));
        // normalize the x- and z-coordinates
        int normX = x < 0 ? 16 - (Math.abs(x) % CHUNK_LENGTH) : (x % CHUNK_LENGTH);
        int normZ = z < 0 ? 16 - (Math.abs(z) % CHUNK_LENGTH) : (z % CHUNK_LENGTH);
        return c.isPresent() ? c.get().getBlock(normX, y, normZ) : Optional.empty();
    }

    public Optional<Block> getBlock(float x, float y, float z) {
        return getBlock((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public TickManager getTickManager() {
        return tickManager;
    }

}
