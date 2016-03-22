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

package net.caseif.cubic.gl.render;

import static net.caseif.cubic.gl.GraphicsMain.CAMERA;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import net.caseif.cubic.gl.texture.Texture;
import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.world.Chunk;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.Block;
import net.caseif.cubic.world.block.BlockFace;
import net.caseif.cubic.world.block.BlockType;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class BlockRenderer {

    private static final float BLOCK_LENGTH = 0.5f;

    private static final int positionAttrIndex = glGetAttribLocation(ShaderHelper.cameraShader, "in_position");
    private static final int texCoordAttrIndex = glGetAttribLocation(ShaderHelper.cameraShader, "in_texCoord");

    private static final Map<Chunk, Integer> CHUNK_VBO_SIZES = new WeakHashMap<>();
    private static final Map<Chunk, Integer> CHUNK_VAO_HANDLES = new WeakHashMap<>();

    public static void render(World world) {
        glUseProgram(ShaderHelper.cameraShader);
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "translationMatrix"), false,
                CAMERA.getTranslationMatrix());
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "rotXMatrix"), false,
                CAMERA.getXRotation());
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "rotYMatrix"), false,
                CAMERA.getYRotation());
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "rotZMatrix"), false,
                CAMERA.getZRotation());
        world.getChunks().forEach(chunk -> {
            if (chunk.isDirty()) {
                FloatBuffer vbo = createVbo(chunk);

                if (CHUNK_VAO_HANDLES.containsKey(chunk)) {
                    glDeleteVertexArrays(CHUNK_VAO_HANDLES.get(chunk));
                }

                CHUNK_VBO_SIZES.put(chunk, vbo.capacity());
                CHUNK_VAO_HANDLES.put(chunk, prepareVbo(chunk.getVboHandle(), vbo));
            }
            render(chunk);
        });
        glUseProgram(0);
    }

    private static FloatBuffer createVbo(Chunk chunk) {
        List<Float> buffer = new ArrayList<>();
        int chunkLen = chunk.getBlocks().length;
        int chunkHeight = chunk.getBlocks()[0].length;
        for (int x = 0; x < chunkLen; x++) {
            for (int z = 0; z < chunkLen; z++) {
                for (int y = 0; y < chunkHeight; y++) {
                    if (chunk.getBlocks()[x][y][z] == null) {
                        continue;
                    }
                    Block block = chunk.getBlocks()[x][y][z];
                    BlockType type = block.getType();
                    List<FloatBuffer> faces = new ArrayList<>();
                    float rX = x * BLOCK_LENGTH;
                    float rY = y * BLOCK_LENGTH;
                    float rZ = z * BLOCK_LENGTH;
                    // back face
                    if (!block.getRelative(BlockFace.BACK).isPresent()) {
                        faces.add(createQuad(type,
                                new Vector3f(rX + BLOCK_LENGTH, rY, rZ),
                                new Vector3f(rX, rY, rZ),
                                new Vector3f(rX, rY + BLOCK_LENGTH, rZ),
                                new Vector3f(rX + BLOCK_LENGTH, rY + BLOCK_LENGTH, rZ)));
                    }
                    // front face
                    if (!block.getRelative(BlockFace.FRONT).isPresent()) {
                        faces.add(createQuad(type,
                                new Vector3f(rX, rY, rZ + BLOCK_LENGTH),
                                new Vector3f(rX + BLOCK_LENGTH, rY, rZ + BLOCK_LENGTH),
                                new Vector3f(rX + BLOCK_LENGTH, rY + BLOCK_LENGTH, rZ + BLOCK_LENGTH),
                                new Vector3f(rX, rY + BLOCK_LENGTH, rZ + BLOCK_LENGTH)));
                    }
                    // left face
                    if (!block.getRelative(BlockFace.LEFT).isPresent()) {
                        faces.add(createQuad(type,
                                new Vector3f(rX, rY, rZ),
                                new Vector3f(rX, rY, rZ + BLOCK_LENGTH),
                                new Vector3f(rX, rY + BLOCK_LENGTH, rZ + BLOCK_LENGTH),
                                new Vector3f(rX, rY + BLOCK_LENGTH, rZ)));
                    }
                    // right face
                    if (!block.getRelative(BlockFace.RIGHT).isPresent()) {
                        faces.add(createQuad(type,
                                new Vector3f(rX + BLOCK_LENGTH, rY, rZ + BLOCK_LENGTH),
                                new Vector3f(rX + BLOCK_LENGTH, rY, rZ),
                                new Vector3f(rX + BLOCK_LENGTH, rY + BLOCK_LENGTH, rZ),
                                new Vector3f(rX + BLOCK_LENGTH, rY + BLOCK_LENGTH, rZ + BLOCK_LENGTH)));
                    }
                    // bottom face
                    if (!block.getRelative(BlockFace.BOTTOM).isPresent()) {
                        faces.add(createQuad(type,
                                new Vector3f(rX, rY, rZ),
                                new Vector3f(rX + BLOCK_LENGTH, rY, rZ),
                                new Vector3f(rX + BLOCK_LENGTH, rY, rZ + BLOCK_LENGTH),
                                new Vector3f(rX, rY, rZ + BLOCK_LENGTH)));
                    }
                    // top face
                    if (!block.getRelative(BlockFace.TOP).isPresent()) {
                        faces.add(createQuad(type,
                                new Vector3f(rX, rY + BLOCK_LENGTH, rZ),
                                new Vector3f(rX, rY + BLOCK_LENGTH, rZ + BLOCK_LENGTH),
                                new Vector3f(rX + BLOCK_LENGTH, rY + BLOCK_LENGTH, rZ + BLOCK_LENGTH),
                                new Vector3f(rX + BLOCK_LENGTH, rY + BLOCK_LENGTH, rZ)));
                    } else {
                        System.out.println("Not rendering top face");
                    }

                    faces.forEach(fb -> {
                        for (float f : fb.array()) {
                            buffer.add(f);
                        }
                    });
                }
            }
        }
        FloatBuffer fb = BufferUtils.createFloatBuffer(buffer.size());
        buffer.forEach(fb::put);
        fb.flip();

        chunk.setDirty(false);

        return fb;
    }

    private static FloatBuffer createQuad(BlockType type, Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3) {
        FloatBuffer fb = FloatBuffer.allocate((6 * (3 + 2)));
        applyVertex(fb, c0, type, 0);
        applyVertex(fb, c1, type, 1);
        applyVertex(fb, c2, type, 2);
        applyVertex(fb, c0, type, 0);
        applyVertex(fb, c2, type, 2);
        applyVertex(fb, c3, type, 3);
        return fb;
    }

    private static void applyVertex(FloatBuffer fb, Vector3f location, BlockType type, int ordinal) {
        fb.put(location.getX()).put(location.getY()).put(location.getZ());
        Vector2f texCoords = Texture.getTexture(type).getAtlasCoords();
        float xAdd = ordinal >= 2 ? (float) Texture.SIZE / Texture.atlasSize : 0;
        float yAdd = ordinal == 1 || ordinal == 2 ? (float) Texture.SIZE / Texture.atlasSize : 0;
        fb.put(texCoords.getX() + xAdd).put(texCoords.getY() + yAdd);
    }

    private static int prepareVbo(int handle, FloatBuffer vbo) {
        IntBuffer vaoHandle = BufferUtils.createIntBuffer(1);
        glGenVertexArrays(vaoHandle);
        glBindVertexArray(vaoHandle.get());
        glBindBuffer(GL_ARRAY_BUFFER, handle);

        glBufferData(GL_ARRAY_BUFFER, vbo, GL_STATIC_DRAW);

        glBindTexture(GL_TEXTURE_2D, Texture.atlasHandle);

        glEnableVertexAttribArray(positionAttrIndex);
        glEnableVertexAttribArray(texCoordAttrIndex);

        glVertexAttribPointer(positionAttrIndex, 3, GL_FLOAT, false, 20, 0);
        glVertexAttribPointer(texCoordAttrIndex, 2, GL_FLOAT, false, 20, 12);

        glBindVertexArray(0);

        vaoHandle.rewind();
        return vaoHandle.get();
    }

    public static void render(Chunk chunk) {
        glBindVertexArray(CHUNK_VAO_HANDLES.get(chunk));
        glDrawArrays(GL_TRIANGLES, 0, CHUNK_VBO_SIZES.get(chunk) / 5);
        glBindVertexArray(0);
    }

}
