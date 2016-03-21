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

import static net.caseif.cubic.gl.GraphicsMain.CAMERA;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import net.caseif.cubic.gl.texture.Texture;
import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.math.vector.Vector4f;
import net.caseif.cubic.world.Chunk;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.BlockType;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class BlockRenderer {

    private static final int globalHandle = glGenBuffers();

    private static final float BLOCK_LENGTH = 0.5f;

    private static final int texCoordAttrIndex = glGetAttribLocation(ShaderHelper.cameraShader, "in_texCoord");

    public static void render(World world) {
        glUseProgram(ShaderHelper.cameraShader);
        int uniformLoc = glGetUniformLocation(ShaderHelper.cameraShader, "orthoTransform");
        glUniformMatrix4fv(uniformLoc, false, CAMERA.getOrthoMatrix());
        /*glBegin(GL_QUADS);
        world.getChunks().forEach(BlockRenderer::renderChunk);
        glEnd();*/
        world.getChunks().forEach(c -> renderVbo(c.getVboHandle(), createVbo(c)));
        glUseProgram(0);
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
                    BlockType type = chunk.getBlocks()[x][y][z].getType();
                    FloatBuffer fb = FloatBuffer.allocate((4 * (3 + 4)) * 6);
                    // back face
                    applyVertex(fb, new Vector3f(x, y, z), type, 0);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y, z), type, 1);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y + BLOCK_LENGTH, z), type, 2);
                    applyVertex(fb, new Vector3f(x, y + BLOCK_LENGTH, z), type, 3);
                    // front face
                    applyVertex(fb, new Vector3f(x, y, z + BLOCK_LENGTH), type, 0);
                    applyVertex(fb, new Vector3f(x, y + BLOCK_LENGTH, z + BLOCK_LENGTH), type, 1);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y + BLOCK_LENGTH, z + BLOCK_LENGTH), type, 2);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y, z + BLOCK_LENGTH), type, 3);
                    // left face
                    applyVertex(fb, new Vector3f(x, y, z), type, 0);
                    applyVertex(fb, new Vector3f(x, y + BLOCK_LENGTH, z), type, 1);
                    applyVertex(fb, new Vector3f(x, y + BLOCK_LENGTH, z + BLOCK_LENGTH), type, 2);
                    applyVertex(fb, new Vector3f(x, y, z + BLOCK_LENGTH), type, 3);
                    // right face
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y, z), type, 0);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y + BLOCK_LENGTH, z), type, 1);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y + BLOCK_LENGTH, z + BLOCK_LENGTH), type, 2);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y, z + BLOCK_LENGTH), type, 3);
                    // bottom face
                    applyVertex(fb, new Vector3f(x, y, z), type, 0);
                    applyVertex(fb, new Vector3f(x, y, z + BLOCK_LENGTH), type, 1);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y, z + BLOCK_LENGTH), type, 2);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y, z), type, 3);
                    // top face
                    applyVertex(fb, new Vector3f(x, y + BLOCK_LENGTH, z), type, 0);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y + BLOCK_LENGTH, z), type, 1);
                    applyVertex(fb, new Vector3f(x + BLOCK_LENGTH, y + BLOCK_LENGTH, z + BLOCK_LENGTH), type, 2);
                    applyVertex(fb, new Vector3f(x, y + BLOCK_LENGTH, z + BLOCK_LENGTH), type, 3);

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
    
    private static void applyVertex(FloatBuffer fb, Vector3f location, BlockType type, int ordinal) {
        fb.put(location.getX()).put(location.getY()).put(location.getZ());
        Vector2f texCoords = Texture.getTexture(type).getAtlasCoords();
        float xAdd = ordinal >= 2 ? (float) Texture.SIZE / Texture.atlasSize : 0;
        float yAdd = ordinal == 1 || ordinal == 2 ? (float) Texture.SIZE / Texture.atlasSize : 0;
        fb.put(texCoords.getX() + xAdd).put(texCoords.getY() + yAdd);

        //fb.put
        //fb.put(color.getX()).put(color.getY()).put(color.getZ()).put(color.getW());
    }

    private static void renderVbo(int handle, FloatBuffer vbo) {
        glBindBuffer(GL_ARRAY_BUFFER, globalHandle);
        glBufferData(GL_ARRAY_BUFFER, vbo, GL_STATIC_DRAW);

        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, Texture.atlasHandle);
        glEnableVertexAttribArray(texCoordAttrIndex);
        glVertexPointer(3, GL_FLOAT, 20, 0);
        glVertexAttribPointer(texCoordAttrIndex, 2, GL_FLOAT, false, 20, 12);
        //glColorPointer(4, GL_FLOAT, 28, 12);
        glDrawArrays(GL_QUADS, 0, vbo.capacity() / 5);
        glDisableVertexAttribArray(texCoordAttrIndex);
        glBindTexture(GL_TEXTURE_2D, 0);
        glPopMatrix();
    }

}
