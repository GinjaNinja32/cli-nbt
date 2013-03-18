package com.gn32.api.data.nbt;

import com.gn32.api.data.nbt.Tag;

/**
 *
 * @author Ben
 */
public class TagLong extends Tag {
    static final int ID = 4;
    long payload;

    public TagLong(String name, long payload) {
        super(name);
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
}
