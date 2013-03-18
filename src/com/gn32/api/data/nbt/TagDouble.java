package com.gn32.api.data.nbt;

/**
 *
 * @author Ben
 */
public class TagDouble extends Tag {
    static final int ID = 6;
    double payload;

    public TagDouble(String name, double payload) {
        super(name);
        this.payload = payload;
    }

    public void setPayload(double payload) {
        this.payload = payload;
    }

    public double getPayload() {
        return payload;
    }
    
    public String toString() {
        return "TAG_Double("+name+"):"+payload;
    }
}
