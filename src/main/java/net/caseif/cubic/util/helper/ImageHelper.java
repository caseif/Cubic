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

package net.caseif.cubic.util.helper;

import static org.lwjgl.opengl.GL11.*;

import net.caseif.cubic.gl.texture.Texture;
import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.world.block.BlockType;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class ImageHelper {

    public static BufferedImage scaleImage(BufferedImage img, int width, int height) {
        //TODO: Change argument to InputStream and move platform-dependent code here
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) newImg.getGraphics();
        g.scale(((double) width / (double) img.getWidth()), ((double) height / (double) img.getHeight()));
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newImg;
    }

    public static InputStream asInputStream(BufferedImage bi) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static void createAtlas() {
        //TODO: Rewrite to use something other than BufferedImage
        //int finalSize = NumUtil.nextPowerOfTwo((int)Math.sqrt(GraphicsUtil.textures.size() *
        //        Math.pow(Block.length, 2)));
        int width = Texture.TEXTURES.size() * Texture.SIZE;
        int height = Texture.SIZE;
        BufferedImage atlas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = atlas.createGraphics();
        int y = 0;
        int i = 0;
        for (BlockType type : Texture.TEXTURES.keySet()) {
            int x = i * Texture.SIZE;
            g.drawImage(Texture.TEXTURES.get(type).getImage(), x, y, null);
            Texture.TEXTURES.get(type).setAtlasCoords(new Vector2f((float) x / width, (float) y / height));
        }
        Texture.atlasSize = width;

        try {
            Texture.atlasHandle = ImageHelper.createTextureFromStream(ImageHelper.asInputStream(atlas));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to load block atlas as texture");
            System.exit(-1);
        }

    }

    private static int createTextureFromStream(InputStream stream) {
        try {
            PNGDecoder decoder = new PNGDecoder(stream);
            ByteBuffer buffer = BufferUtils.createByteBuffer(decoder.getWidth() * decoder.getHeight() * 4);
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();

            int handle = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, handle);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, buffer);
            glBindTexture(GL_TEXTURE_2D, 0);

            return handle;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Failed to load texture!");
            System.exit(-1);
        }

        return -1;
    }

}
