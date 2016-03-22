package net.caseif.cubic.gl.texture;

import static com.google.common.base.Preconditions.checkState;
import static net.caseif.cubic.gl.texture.Texture.SIZE;
import static org.lwjgl.opengl.GL11.*;

import net.caseif.cubic.gl.GraphicsMain;
import net.caseif.cubic.math.vector.Vector2f;
import net.caseif.cubic.util.helper.ImageHelper;
import net.caseif.cubic.world.block.BlockFace;
import net.caseif.cubic.world.block.BlockType;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import java.awt.Graphics2D;
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

    private void createAtlas() {
        int width = textures.size() * Texture.SIZE;
        int height = Texture.SIZE;
        BufferedImage atlas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = atlas.createGraphics();
        int y = 0;
        int i = 0;
        for (TextureKey key : textures.keySet()) {
            int x = i * Texture.SIZE;
            g.drawImage(textures.get(key).getImage(), x, y, null);
            textures.get(key).setAtlasCoords(new Vector2f((float) x / width, (float) y / height));
            i++;
        }
        Texture.atlasSize = width;

        try {
            Texture.atlasHandle = createGLTextureFromStream(ImageHelper.asInputStream(atlas));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to load block atlas as texture");
            System.exit(-1);
        }
    }

    private int createGLTextureFromStream(InputStream stream) {
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

    public void registerTextures() {
        checkState(!areTexturesRegistered, "Cannot register textures more than once");

        createTexture(BlockType.STONE);
        createTexture(BlockType.GRASS);
        createTexture(BlockType.GRASS, BlockFace.TOP);
        createTexture(BlockType.GRASS, BlockFace.BOTTOM);

        createAtlas();

        this.areTexturesRegistered = true;
    }

}
