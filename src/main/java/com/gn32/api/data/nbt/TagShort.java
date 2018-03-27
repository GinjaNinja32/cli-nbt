package com.gn32.api.data.nbt;

import com.gn32.api.data.nbt.Tag;

/**
 *
 * @author Ben
 */
public class TagShort extends Tag {
    static final int ID = 2;
    short payload;

    public TagShort(String name, short payload) {
        super(name, "short");
        this.payload = payload;
    }

    public void setPayload(short payload) {
        this.payload = payload;
    }

    public short getPayload() {
        return payload;
    }
    
    public String toString() {
        return "TAG_Short("+name+"):"+payload;
    }

    @Override
    public Object payloadGeneric() {
        return payload;
    }
}
