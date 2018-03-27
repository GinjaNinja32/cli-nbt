package com.gn32.api.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ben
 */
public abstract class Tag {
    Tag parent;
    
    String name;
    
    final String type;

    Tag(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    Tag setParent(Tag parent) {
        this.parent = parent;
        return this;
    }
    
    public Tag getParent() {
        return parent;
    }
    
    public String toString2() {
        if(this instanceof TagCompound) {
            return ((TagCompound)this).toString2();
        } else if(this instanceof TagList) {
            return ((TagList)this).toString2();
        } else {
            return this.toString();
        }
    }
    
    static String readString(DataInputStream is) throws IOException {
        int len = (is.read()<<8) | is.read();
        String st = "";
        for(int i=0; i<len; i++) {
            st += (char)is.read();
        }
        return st;
    }
    
    static TagByte decodeTagByte(DataInputStream is) throws IOException {
        return new TagByte(readString(is), is.readByte());
    }
    static TagShort decodeTagShort(DataInputStream is) throws IOException {
        return new TagShort(readString(is), is.readShort());
    }
    static TagInt decodeTagInt(DataInputStream is) throws IOException {
        return new TagInt(readString(is), is.readInt());
    }
    static TagLong decodeTagLong(DataInputStream is) throws IOException {
        return new TagLong(readString(is), is.readLong());
    }
    static TagFloat decodeTagFloat(DataInputStream is) throws IOException {
        return new TagFloat(readString(is), is.readFloat());
    }
    static TagDouble decodeTagDouble(DataInputStream is) throws IOException {
        return new TagDouble(readString(is), is.readDouble());
    }
    static TagByteArray decodeTagByteArray(DataInputStream is) throws IOException {
        String name = readString(is);
        int len = is.readInt();
        Byte[] b = new Byte[len];
        for(int i=0; i<len; i++) { b[i] = is.readByte(); }
        return new TagByteArray(name, b);
    }
    static TagString decodeTagString(DataInputStream is) throws IOException {
        return new TagString(readString(is), readString(is));
    }
    static TagList decodeTagList(DataInputStream is) throws IOException {
        // less to debug/keep updated this way, plus smaller executable size!
        String name = readString(is);
        TagList tag = decodeTagListUnnamed(is);
        tag.name = name;
        return tag;
    }
    static TagList decodeTagListUnnamed(DataInputStream is) throws IOException {
        int type = is.read();
        int len = is.readInt();
        Tag[] tags = new Tag[len];
        switch(type) {
            case 1: // byte
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagByte("", (byte)is.read());
                } break;
            case 2: // short
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagShort("", (short)((is.read()<<8) + is.read()));
                } break;
            case 3: // int
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagInt("", is.readInt());
                } break;
            case 4: // long
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagLong("", (((long)is.readInt())<<32) + is.readInt());
                } break;
            case 5: // float
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagFloat("", Float.intBitsToFloat(is.readInt()));
                } break;
            case 6: // double
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagDouble("", Double.longBitsToDouble((((long)is.readInt())<<32) + is.readInt()));
                } break;
            case 7: // byte array
                for(int i=0 ;i<len; i++) {
                    int len2 = is.readInt();
                    Byte[] b = new Byte[len];
                    for(int j=0; j<len2; j++) { b[j] = (byte)is.read(); }
                    tags[i] = new TagByteArray("", b);
                } break;
            case 8: // string
                for(int i=0 ;i<len; i++) {
                    tags[i] = new TagString("", readString(is));
                } break;
            case 9: // list
                for(int i=0 ;i<len; i++) {
                    tags[i] = decodeTagListUnnamed(is);
                } break;
            case 10: // compound
                for(int i=0 ;i<len; i++) {
                    tags[i] = decodeTagCompoundUnnamed(is);
                } break;
            case 11: // int array
                for(int i=0 ;i<len; i++) {
                    int len2 = is.readInt();
                    Integer[] arr = new Integer[len];
                    for(int j=0; j<len2; j++) { arr[j] = is.readInt(); }
                    tags[i] = new TagIntArray("", arr);
                } break;
            default:
                throw new NBTException("Illegal Tag ID!");
        }
        TagList tl = new TagList("", tags);
        for(Tag t: tl.payload) {
            t.setParent(tl);
        }
        return tl;
    }
    static TagCompound decodeTagCompound(DataInputStream is) throws IOException {
        // less to debug/keep updated this way, plus smaller executable size!
        String name = readString(is);
        TagCompound tag = decodeTagCompoundUnnamed(is);
        tag.name = name;
        return tag;
    }
    static TagCompound decodeTagCompoundUnnamed(DataInputStream is) throws IOException {
        List<Tag> l = new ArrayList<Tag>();
        loop: while(true) {
            switch(is.read()) {
                case 0: // terminator
                    break loop;
                case 1: // byte
                    l.add(decodeTagByte(is));
                    break;
                case 2: // short
                    l.add(decodeTagShort(is));
                    break;
                case 3: // int
                    l.add(decodeTagInt(is));
                    break;
                case 4: // long
                    l.add(decodeTagLong(is));
                    break;
                case 5: // float
                    l.add(decodeTagFloat(is));
                    break;
                case 6: // double
                    l.add(decodeTagDouble(is));
                    break;
                case 7: // byte array
                    l.add(decodeTagByteArray(is));
                    break;
                case 8: // string
                    l.add(decodeTagString(is));
                    break;
                case 9: // list
                    l.add(decodeTagList(is));
                    break;
                case 10: // compound
                    l.add(decodeTagCompound(is));
                    break;
                case 11: // int array
                    l.add(decodeTagIntArray(is));
                    break;
                default:
                    throw new NBTException("Illegal Tag ID!");
            }
        }
        TagCompound tc = new TagCompound("", l.toArray(new Tag[0]));
        for(Tag t: tc.payload) {
            t.setParent(tc);
        }
        return tc;
    }
    static TagIntArray decodeTagIntArray(DataInputStream is) throws IOException {
        String name = readString(is);
        int len = is.readInt();
        Integer[] arr = new Integer[len];
        for(int i=0; i<len; i++) { arr[i] = is.readInt(); }
        return new TagIntArray(name, arr);
    }
    
    static void encode(TagByte tag, DataOutputStream os) throws IOException {
        os.writeByte(1);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeByte(tag.payload);
    }
    static void encode(TagShort tag, DataOutputStream os) throws IOException {
        os.writeByte(2);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeShort(tag.payload);
    }
    static void encode(TagInt tag, DataOutputStream os) throws IOException {
        os.writeByte(3);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeInt(tag.payload);
    }
    static void encode(TagLong tag, DataOutputStream os) throws IOException {
        os.writeByte(4);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeLong(tag.payload);
    }
    static void encode(TagFloat tag, DataOutputStream os) throws IOException {
        os.writeByte(5);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeFloat(tag.payload);
    }
    static void encode(TagDouble tag, DataOutputStream os) throws IOException {
        os.writeByte(6);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeDouble(tag.payload);
    }
    static void encode(TagByteArray tag, DataOutputStream os) throws IOException {
        os.writeByte(7);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeInt(tag.payload.size());
        for(Byte b: tag.payload) {
            os.write(b);
        }
    }
    static void encode(TagString tag, DataOutputStream os) throws IOException {
        os.writeByte(8);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeShort(tag.payload.length());
        os.writeBytes(tag.payload);
    }
    static void encode(TagList tag, DataOutputStream os) throws IOException {
        os.writeByte(9);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        encodeNoName(tag, os);
    }
    static void encodeNoName(TagList tag, DataOutputStream os) throws IOException {
        if(!tag.payload.isEmpty()) {
                if(tag.payload.get(0) instanceof TagByte) {
                    os.writeByte(1);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeByte(((TagByte)t).payload);
                    }
                }
           else if(tag.payload.get(0) instanceof TagShort) {
                    os.writeByte(2);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeShort(((TagShort)t).payload);
                    }
           }
           else if(tag.payload.get(0) instanceof TagInt) {
                    os.writeByte(3);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeInt(((TagInt)t).payload);
                    }
           }
           else if(tag.payload.get(0) instanceof TagLong) {
                    os.writeByte(4);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeLong(((TagLong)t).payload);
                    }
           }
           else if(tag.payload.get(0) instanceof TagFloat) {
                    os.writeByte(5);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeFloat(((TagFloat)t).payload);
                    }
           }
           else if(tag.payload.get(0) instanceof TagDouble) {
                    os.writeByte(6);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeDouble(((TagDouble)t).payload);
                    }
           }
           else if(tag.payload.get(0) instanceof TagByteArray) {
                    os.writeByte(7);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeInt(((TagByteArray)t).payload.size());
                        for(byte b: ((TagByteArray)t).payload) {
                            os.writeByte(b);
                        }
                    }
           }
           else if(tag.payload.get(0) instanceof TagString) {
                    os.writeByte(8);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeShort(((TagString)t).payload.length());
                        os.writeBytes(((TagString)t).payload);
                    }
           }
           else if(tag.payload.get(0) instanceof TagList) {
                    os.writeByte(9);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        Tag.encodeNoName((TagList)t, os);
                    }
           }
           else if(tag.payload.get(0) instanceof TagCompound) {
                    os.writeByte(10);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        Tag.encodeNoName((TagCompound)t, os);
                    }
           }
           else if(tag.payload.get(0) instanceof TagIntArray) {
                    os.writeByte(11);
                    os.writeInt(tag.payload.size());
                    for(Tag t: tag.payload) {
                        os.writeInt(((TagIntArray)t).payload.size());
                        for(int i: ((TagIntArray)t).payload) {
                            os.writeInt(i);
                        }
                    }
           }
           else {
               throw new Error("Internal Error (if this happens, you've tried to create a new tag type.");
           }
        } else {
            // pretend it's an empty byte list
            os.writeByte(1);
            os.writeInt(0);
        }
    }
    static void encode(TagCompound tag, DataOutputStream os) throws IOException {
        os.writeByte(10);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        encodeNoName(tag, os);
    }
    static void encodeNoName(TagCompound tag, DataOutputStream os) throws IOException {
        for(Tag t: tag.payload) {
                if(t instanceof TagByte) {encode((TagByte)t, os);}
           else if(t instanceof TagShort) {encode((TagShort)t, os);}
           else if(t instanceof TagInt) {encode((TagInt)t, os);}
           else if(t instanceof TagLong) {encode((TagLong)t, os);}
           else if(t instanceof TagFloat) {encode((TagFloat)t, os);}
           else if(t instanceof TagDouble) {encode((TagDouble)t, os);}
           else if(t instanceof TagByteArray) {encode((TagByteArray)t, os);}
           else if(t instanceof TagString) {encode((TagString)t, os);}
           else if(t instanceof TagList) {encode((TagList)t, os);}
           else if(t instanceof TagCompound) {encode((TagCompound)t, os);}
           else if(t instanceof TagIntArray) {encode((TagIntArray)t, os);}
           else {
               throw new Error("Internal Error (if this happens, you've tried to create a new tag type).");
           }
        }
        os.writeByte(0); // TAG_End
    }
    static void encode(TagIntArray tag, DataOutputStream os) throws IOException {
        os.writeByte(11);
        os.writeShort(tag.name.length());
        os.writeBytes(tag.name);
        os.writeInt(tag.payload.size());
        for(int i: tag.payload) {
            os.writeInt(i);
        }
    }
    
    public byte payloadByte() throws NBTException {
        if(this instanceof TagByte) {
            return ((TagByte)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public short payloadShort() throws NBTException {
        if(this instanceof TagShort) {
            return ((TagShort)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public int payloadInt() throws NBTException {
        if(this instanceof TagInt) {
            return ((TagInt)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public long payloadLong() throws NBTException {
        if(this instanceof TagLong) {
            return ((TagLong)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public float payloadFloat() throws NBTException {
        if(this instanceof TagFloat) {
            return ((TagFloat)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public double payloadDouble() throws NBTException {
        if(this instanceof TagDouble) {
            return ((TagDouble)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public Byte[] payloadByteArray() throws NBTException {
        if(this instanceof TagByteArray) {
            return ((TagByteArray)this).payload.toArray(new Byte[0]);
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public String payloadString() throws NBTException {
        if(this instanceof TagString) {
            return ((TagString)this).payload;
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public Tag[] payloadTagList() throws NBTException {
        if(this instanceof TagList) {
            return ((TagList)this).payload.toArray(new Tag[0]);
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public Tag[] payloadTagCompound() throws NBTException {
        if(this instanceof TagCompound) {
            return ((TagCompound)this).payload.toArray(new Tag[0]);
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    public Integer[] payloadIntArray() throws NBTException {
        if(this instanceof TagIntArray) {
            return ((TagIntArray)this).payload.toArray(new Integer[0]);
        } else {
            throw new NBTException("Invalid payload type!");
        }
    }
    
    
    
    
    public TagByte asByteTag() throws NBTException {
        if(this instanceof TagByte) {
            return (TagByte)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagShort asShortTag() throws NBTException {
        if(this instanceof TagShort) {
            return (TagShort)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagInt asIntTag() throws NBTException {
        if(this instanceof TagInt) {
            return (TagInt)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagLong asLongTag() throws NBTException {
        if(this instanceof TagLong) {
            return (TagLong)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagFloat asFloatTag() throws NBTException {
        if(this instanceof TagFloat) {
            return (TagFloat)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagDouble asDoubleTag() throws NBTException {
        if(this instanceof TagDouble) {
            return (TagDouble)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagByteArray asByteArrayTag() throws NBTException {
        if(this instanceof TagByteArray) {
            return (TagByteArray)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagString asStringTag() throws NBTException {
        if(this instanceof TagString) {
            return (TagString)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagList asListTag() throws NBTException {
        if(this instanceof TagList) {
            return (TagList)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagCompound asCompoundTag() throws NBTException {
        if(this instanceof TagCompound) {
            return (TagCompound)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    public TagIntArray asIntArrayTag() throws NBTException {
        if(this instanceof TagIntArray) {
            return (TagIntArray)this;
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    
    public abstract Object payloadGeneric();
}
