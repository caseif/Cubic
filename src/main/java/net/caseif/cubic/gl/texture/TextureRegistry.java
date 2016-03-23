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

package net.caseif.cubic.gl.texture;

import static com.google.common.base.Preconditions.checkState;
import static net.caseif.cubic.gl.texture.Texture.SIZE;
import static org.lwjgl.opengl.ARBTextureStorage.glTexStorage3D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;

import net.caseif.cubic.gl.GraphicsMain;
import net.caseif.cubic.util.helper.ImageHelper;
import net.caseif.cubic.world.block.BlockFace;
import net.caseif.cubic.world.block.BlockType;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TextureRegistry {

    private final Map<TextureKey, Texture> textures = new HashMap<>();

    private boolean areTexturesRegistered = false;

    public Texture getTexture(BlockType type, BlockFace face) {
        TextureKey key = new TextureKey(type, face);
        if (textures.containsKey(key)) {
            return textures.get(key);
        } else {
            key = new TextureKey(type, null);
            if (textures.containsKey(key)) {
                return textures.get(key);
            } else {
                throw new IllegalArgumentException("Game requested unregistered texture (type=" + type.name()
                        + ", face=" + face.name());
            }
        }
    }

    private void registerTexture(BlockType type, BlockFace face, Texture texture) {
        textures.put(new TextureKey(type, face), texture);
    }

    private void createTexture(BlockType type, BlockFace face) {
        try {
            InputStream is = Texture.class.getResourceAsStream("/textures/block/" + type.name().toLowerCase()
                    + (face != null ? "_" + face.toString().toLowerCase() : "") + ".png");
            InputStream newIs = ImageHelper.asInputStream(ImageHelper.scaleImage(ImageIO.read(is), SIZE, SIZE));
            BufferedImage bi = ImageIO.read(newIs);
            GraphicsMain.TEXTURE_REGISTRY.registerTexture(type, face, new Texture(bi));

        } catch (Exception ex) {
            System.err.println("Exception occurred while preparing texture for material " + type.toString());
            ex.printStackTrace();
        }
    }

    private void createTexture(BlockType type) {
        createTexture(type, null);
    }

    private void createTextureArray() {
        int handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_ARRAY, handle);

        glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, Texture.SIZE, Texture.SIZE, textures.size());

        try {
            int layer = 0;
            for (Texture tex : textures.values()) {
                PNGDecoder decoder = new PNGDecoder(ImageHelper.asInputStream(tex.getImage()));
                ByteBuffer buffer = BufferUtils.createByteBuffer(decoder.getWidth() * decoder.getHeight() * 4);
                decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
                buffer.flip();

                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, layer, decoder.getWidth(), decoder.getHeight(), 1,
                        GL_RGBA, GL_UNSIGNED_BYTE, buffer);

                tex.setLayer(layer);

                layer++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    public void registerTextures() {
        checkState(!areTexturesRegistered, "Cannot register textures more than once");

        createTexture(BlockType.STONE);
        createTexture(BlockType.GRASS);
        createTexture(BlockType.GRASS, BlockFace.TOP);
        createTexture(BlockType.GRASS, BlockFace.BOTTOM);

        createTextureArray();

        this.areTexturesRegistered = true;
    }

}
