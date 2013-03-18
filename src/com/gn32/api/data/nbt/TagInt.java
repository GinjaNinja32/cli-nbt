package com.gn32.api.data.nbt;

/**
 *
 * @author Ben
 */
public class TagInt extends Tag {
    static final int ID = 3;
    int payload;

    public TagInt(String name, int payload) {
        super(name);
        this.payload = payload;
    }

    public void setPayload(int payload) {
        this.payload = payload;
    }

    public int getPayload() {
        return payload;
    }
    
    public String toString() {
        return "TAG_Int("+name+"):"+payload;
    }
}
