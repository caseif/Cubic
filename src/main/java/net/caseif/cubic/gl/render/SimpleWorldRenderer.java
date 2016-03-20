/*
 * GLExperiments
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

package net.caseif.cubic.gl.render;

import static org.lwjgl.opengl.GL11.*;

import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.world.Chunk;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.Block;

public class SimpleWorldRenderer {

    private static final float UNITS_PER_BLOCK = 0.5f;

    public static void render(World world) {
        glBegin(GL_QUADS);
        world.getChunks().forEach(SimpleWorldRenderer::renderChunk);
        glEnd();
    }

    private static void renderChunk(Chunk chunk) {
        int chunkLen = chunk.getBlocks().length;
        int chunkHeight = chunk.getBlocks()[0].length;
        for (int x = 0; x < chunkLen; x++) {
            for (int z = 0; z < chunkLen; z++) {
                for (int y = 0; y < chunkHeight; y++) {
                    renderBlock(chunk.getBlocks()[x][y][z]);
                }
            }
        }
    }

    private static void renderBlock(Block block) {
        if (block == null) {
            return;
        }

        Vector3f pos = block.getPosition();

        // back face
        glColor3f(0f, 1f, 0f);
        glVertex3f(pos.getX(), pos.getY(), pos.getZ());
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY(), pos.getZ());
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY() + UNITS_PER_BLOCK, pos.getZ());
        glVertex3f(pos.getX(), pos.getY() + UNITS_PER_BLOCK, pos.getZ());
        // front face
        glColor3f(0f, 1f, 0f);
        glVertex3f(pos.getX(), pos.getY(), pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY(), pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY() + UNITS_PER_BLOCK, pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX(), pos.getY() + UNITS_PER_BLOCK, pos.getZ() + UNITS_PER_BLOCK);
        // left face
        glColor3f(1f, 0f, 0f);
        glVertex3f(pos.getX(), pos.getY(), pos.getZ());
        glVertex3f(pos.getX(), pos.getY(), pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX(), pos.getY() + UNITS_PER_BLOCK, pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX(), pos.getY() + UNITS_PER_BLOCK, pos.getZ());
        // right face
        glColor3f(1f, 0f, 0f);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY(), pos.getZ());
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY(), pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY() + UNITS_PER_BLOCK, pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY() + UNITS_PER_BLOCK, pos.getZ());
        // top face
        glColor3f(0f, 0f, 1f);
        glVertex3f(pos.getX(), pos.getY(), pos.getZ());
        glVertex3f(pos.getX(), pos.getY(), pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY(), pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY(), pos.getZ());
        // bottom face
        glColor3f(0f, 0f, 1f);
        glVertex3f(pos.getX(), pos.getY() + UNITS_PER_BLOCK, pos.getZ());
        glVertex3f(pos.getX(), pos.getY() + UNITS_PER_BLOCK, pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY() + UNITS_PER_BLOCK, pos.getZ() + UNITS_PER_BLOCK);
        glVertex3f(pos.getX() + UNITS_PER_BLOCK, pos.getY() + UNITS_PER_BLOCK, pos.getZ());
    }

}
