package com.gn32.api.data.nbt;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ben
 */
public class TagByteArray extends Tag {
    static final int ID = 7;
    ArrayList<Byte> payload = new ArrayList<>();

    public TagByteArray(String name, ArrayList<Byte> payload) {
        super(name, "bytearr");
        if(payload != null) {
            this.payload = payload;
        }
    }
    
    public TagByteArray(String name, Byte[] payload) {
        super(name, "bytearr");
        this.payload.addAll(Arrays.asList(payload));
    }

    public Byte[] getPayload() {
        return payload.toArray(new Byte[0]);
    }

    public ArrayList<Byte> getPayloadArrayList() {
        return payload;
    }

    public void setPayload(ArrayList<Byte> payload) {
        if(payload == null) {
            this.payload.clear();
        } else {
            this.payload = payload;
        }
    }

    public void setPayload(Byte[] payload) {
        this.payload.clear();
        this.payload.addAll(Arrays.asList(payload));
    }
    
    public String toString() {
        String s = "TAG_Byte_Array("+name+"): {";
        for(int i: payload) {
            s += i + " ";
        }
        if(payload.isEmpty()) {
            return s + "}";
        } else {
            return s.substring(0, s.length() - 1) + "}";
        }
    }
    
    public String toString3() {
        String s = "TAG_Byte_Array("+name+"): {";
        for(int i=0; i<payload.size(); i++) {
            s += "  " + i + ": " + payload.get(i) + "\n";
        }
        return s + "}";
    }

    @Override
    public Object payloadGeneric() {
        return payload.toArray(new Byte[0]);
    }
}
