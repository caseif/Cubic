package net.caseif.cubic.gl.texture;

import net.caseif.cubic.world.block.BlockFace;
import net.caseif.cubic.world.block.BlockType;

import java.util.Objects;

public class TextureKey {

    private BlockType type;
    private BlockFace face;

    public TextureKey(BlockType type, BlockFace face) {
        this.type = type;
        this.face = face;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TextureKey)) {
            return false;
        }
        TextureKey tk = (TextureKey) obj;
        return this.type == tk.type && this.face == tk.face;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.face);
    }

}
