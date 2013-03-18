package com.gn32.api.data.nbt;

import com.gn32.api.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Ben
 */
public class NBT {
    TagCompound mainTag; // "Every NBT file will always begin with a TAG_Compound. No exceptions."
    private NBT(TagCompound mainTag) {
        this.mainTag = mainTag;
    }
    
    public TagCompound getMain() {
        return mainTag;
    }
    
    public static NBT blankNBT() {
        return new NBT(new TagCompound("/", new Tag[0]));
    }
    public static NBT blankNBT(String topLevelName) {
        return new NBT(new TagCompound(topLevelName, new Tag[0]));
    }
    
    public static NBT readNBT(DataInputStream is) throws IOException {
        if(is.read() != 10) {
            throw new NBTException("First tag MUST be a compound tag!");
        }
        return new NBT(Tag.decodeTagCompound(is));
    }
    
    public void writeNBT(DataOutputStream os) throws IOException {
        Tag.encode(mainTag, os);
    }
    public String toString() {
        // delegate to toString2() with a default indent
        return toString2("    ");
    }
    public String toString2(String indent) {
        return mainTag.toString2("", indent);
    }
}
