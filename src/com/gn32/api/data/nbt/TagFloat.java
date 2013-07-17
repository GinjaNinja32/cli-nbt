package com.gn32.api.data.nbt;

/**
 *
 * @author Ben
 */
public class TagFloat extends Tag {
    static final int ID = 5;
    float payload;

    public TagFloat(String name, float payload) {
        super(name, "float");
        this.payload = payload;
    }

    public void setPayload(float payload) {
        this.payload = payload;
    }

    public float getPayload() {
        return payload;
    }

    @Override
    public Object payloadGeneric() {
        return payload;
    }
    
    public String toString() {
        return "TAG_Float("+name+"):"+payload;
    }
}
