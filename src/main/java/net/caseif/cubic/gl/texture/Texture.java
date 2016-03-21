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

import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.util.helper.ImageHelper;
import net.caseif.cubic.world.block.BlockType;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Texture {

    public static final int SIZE = 128;

    public static final Map<BlockType, Texture> TEXTURES = new HashMap<>();
    public static int atlasHandle;
    public static float atlasSize;

    private BlockType type;
    private BufferedImage image;
    private Vector2f atlasCoords;

    private Texture(BlockType type, BufferedImage image) {
        this.type = type;
        this.image = image;
    }

    public BlockType getType() {
        return type;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Vector2f getAtlasCoords() {
        return atlasCoords;
    }

    public void setAtlasCoords(Vector2f coords) {
        this.atlasCoords = coords;
    }

    public static void registerTexture(BlockType type) {
        try {
            InputStream is = Texture.class.getResourceAsStream("/textures/block/" + type.name().toLowerCase() + ".png");
            InputStream newIs = ImageHelper.asInputStream(ImageHelper.scaleImage(ImageIO.read(is), SIZE, SIZE));
            BufferedImage b = ImageIO.read(newIs);
            TEXTURES.put(type, new Texture(type, b));

        } catch (Exception ex) {
            System.err.println("Exception occurred while preparing texture for material " + type.toString());
            ex.printStackTrace();
        }
    }

    public static Texture getTexture(BlockType type) {
        return TEXTURES.get(type);
    }

}
