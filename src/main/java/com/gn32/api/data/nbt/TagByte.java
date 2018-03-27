package com.gn32.api.data.nbt;

/**
 *
 * @author Ben
 */
public class TagByte extends Tag {
    static final int ID = 1;
    byte payload;

    public TagByte(String name, byte payload) {
        super(name, "byte");
        this.payload = payload;
    }

    public byte getPayload() {
        return payload;
    }

    public void setPayload(byte payload) {
        this.payload = payload;
    }
    
    public String toString() {
        return "TAG_Byte("+name+"):"+payload;
    }

    @Override
    public Object payloadGeneric() {
        return payload;
    }
}
