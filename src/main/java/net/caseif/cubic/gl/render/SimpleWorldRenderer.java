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

import static javafx.scene.input.KeyCode.F;
import static javafx.scene.input.KeyCode.V;
import static net.caseif.cubic.gl.GraphicsMain.CAMERA;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.math.vector.Vector4f;
import net.caseif.cubic.world.Chunk;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.Block;

import com.google.common.collect.Lists;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleWorldRenderer {

    private static final int globalHandle = glGenBuffers();

    private static final float UNITS_PER_BLOCK = 0.5f;

    public static void render(World world) {
        glUseProgram(ShaderHelper.cameraShader);
        /*System.out.println("Uniform location: " + glGetUniformLocation(ShaderHelper.cameraShader, "orthoTransform"));
        IntBuffer total = IntBuffer.allocate(1);
        glGetProgramiv(ShaderHelper.cameraShader, GL_ACTIVE_UNIFORMS, total);
        System.out.println("Total uniform count: " + total.array()[0]);
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "orthoTransform"), false, CAMERA.getLocationBuffer());*/
        /*glBegin(GL_QUADS);
        world.getChunks().forEach(SimpleWorldRenderer::renderChunk);
        glEnd();*/
        world.getChunks().forEach(c -> renderVbo(c.getVboHandle(), createVbo(c)));
        glUseProgram(0);
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

    private static FloatBuffer createVbo(Chunk chunk) {
        final Vector4f red = new Vector4f(1f, 0f, 0f, 1f);
        final Vector4f green = new Vector4f(0f, 1f, 0f, 1f);
        final Vector4f blue = new Vector4f(0f, 0f, 1f, 1f);
        
        List<Float> buffer = new ArrayList<>();
        int chunkLen = chunk.getBlocks().length;
        int chunkHeight = chunk.getBlocks()[0].length;
        for (int x = 0; x < chunkLen; x++) {
            for (int z = 0; z < chunkLen; z++) {
                for (int y = 0; y < chunkHeight; y++) {
                    if (chunk.getBlocks()[x][y][z] == null) {
                        continue;
                    }
                    FloatBuffer fb = FloatBuffer.allocate((4 * (3 + 4)) * 6);
                    // back face
                    applyVertex(fb, new Vector3f(x, y, z), red);
                    applyVertex(fb, new Vector3f(x, y + UNITS_PER_BLOCK, z), red);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y + UNITS_PER_BLOCK, z), red);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y, z), red);
                    // front face
                    applyVertex(fb, new Vector3f(x, y, z + UNITS_PER_BLOCK), red);
                    applyVertex(fb, new Vector3f(x, y + UNITS_PER_BLOCK, z + UNITS_PER_BLOCK), red);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y + UNITS_PER_BLOCK, z + UNITS_PER_BLOCK), red);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y, z + UNITS_PER_BLOCK), red);
                    // left face
                    applyVertex(fb, new Vector3f(x, y, z), green);
                    applyVertex(fb, new Vector3f(x, y, z + UNITS_PER_BLOCK), green);
                    applyVertex(fb, new Vector3f(x, y + UNITS_PER_BLOCK, z + UNITS_PER_BLOCK), green);
                    applyVertex(fb, new Vector3f(x, y + UNITS_PER_BLOCK, z), green);
                    // right face
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y, z), green);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y + UNITS_PER_BLOCK, z), green);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y + UNITS_PER_BLOCK, z + UNITS_PER_BLOCK), green);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y, z + UNITS_PER_BLOCK), green);
                    // top face
                    applyVertex(fb, new Vector3f(x, y, z), blue);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y, z), blue);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y, z + UNITS_PER_BLOCK), blue);
                    applyVertex(fb, new Vector3f(x, y, z + UNITS_PER_BLOCK), blue);
                    // bottom face
                    applyVertex(fb, new Vector3f(x, y + UNITS_PER_BLOCK, z), blue);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y + UNITS_PER_BLOCK, z), blue);
                    applyVertex(fb, new Vector3f(x + UNITS_PER_BLOCK, y + UNITS_PER_BLOCK, z + UNITS_PER_BLOCK), blue);
                    applyVertex(fb, new Vector3f(x, y + UNITS_PER_BLOCK, z + UNITS_PER_BLOCK), blue);

                    for (float f : fb.array()) {
                        buffer.add(f);
                    }
                }
            }
        }
        FloatBuffer fb = BufferUtils.createFloatBuffer(buffer.size());
        buffer.forEach(fb::put);
        fb.flip();
        return fb;
    }
    
    private static void applyVertex(FloatBuffer fb, Vector3f location, Vector4f color) {
        fb.put(location.getX()).put(location.getY()).put(location.getZ());
        fb.put(color.getX()).put(color.getY()).put(color.getZ()).put(color.getW());
    }

    private static void renderVbo(int handle, FloatBuffer vbo) {
        glBindBuffer(GL_ARRAY_BUFFER, globalHandle);
        glBufferData(GL_ARRAY_BUFFER, vbo, GL_STATIC_DRAW);

        glPushMatrix();
        glVertexPointer(3, GL_FLOAT, 28, 0);
        glColorPointer(4, GL_FLOAT, 28, 12);
        glDrawArrays(GL_QUADS, 0, vbo.capacity() / 7);
        glPopMatrix();
    }

}
