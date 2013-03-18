package com.gn32.api.data.nbt;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ben
 */
public class TagIntArray extends Tag {
    static final int ID = 11;
    ArrayList<Integer> payload = new ArrayList<>();

    public TagIntArray(String name, ArrayList<Integer> payload) {
        super(name);
        if(payload != null) {
            this.payload = payload;
        }
    }
    
    public TagIntArray(String name, Integer[] payload) {
        super(name);
        this.payload.addAll(Arrays.asList(payload));
    }

    public Integer[] getPayload() {
        return payload.toArray(new Integer[0]);
    }

    public ArrayList<Integer> getPayloadArrayList() {
        return payload;
    }

    public void setPayload(ArrayList<Integer> payload) {
        if(payload == null) {
            this.payload.clear();
        } else {
            this.payload = payload;
        }
    }

    public void setPayload(Integer[] payload) {
        this.payload.clear();
        this.payload.addAll(Arrays.asList(payload));
    }
    
    public String toString() {
        String s = "TAG_Int_Array("+name+"): {";
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
        String s = "TAG_Int_Array("+name+"): {\n";
        for(int i=0; i<payload.size(); i++) {
            s += "  " + i + ": " + payload.get(i) + "\n";
        }
        return s + "}";
    }
}
