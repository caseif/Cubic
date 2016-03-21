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
