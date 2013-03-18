package com.gn32.api.data.nbt;

/**
 *
 * @author Ben
 */
public class TagString extends Tag {
    static final int ID = 8;
    String payload;

    public TagString(String name, String payload) {
        super(name);
        this.payload = payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
    
    public String toString() {
        return "TAG_String("+name+"):\""+payload + "\"";
    }
}
