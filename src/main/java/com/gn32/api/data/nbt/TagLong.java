package com.gn32.api.data.nbt;

/**
 *
 * @author Ben
 */
public class TagLong extends Tag {
    static final int ID = 4;
    long payload;

    public TagLong(String name, long payload) {
        super(name, "long");
        this.payload = payload;
    }

    public long getPayload() {
        return payload;
    }

    public void setPayload(long payload) {
        this.payload = payload;
    }
    
    public String toString() {
        return "TAG_Long("+name+"):"+payload;
    }

    @Override
    public Object payloadGeneric() {
        return payload;
    }
}
