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
        //		Math.pow(Block.length, 2)));
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
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
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
