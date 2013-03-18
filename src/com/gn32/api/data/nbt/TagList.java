package com.gn32.api.data.nbt;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ben
 */
public class TagList extends Tag {
    static final int ID = 9;
    ArrayList<Tag> payload = new ArrayList<>();

    public TagList(String name, Tag... payload) {
        super(name);
        this.payload.addAll(Arrays.asList(payload));
    }
    
    public void addTag(Tag t) throws NBTException {
        if(payload.isEmpty()) {
            payload.add(t);
            t.setParent(this);
        } else if(payload.get(0).getClass() == t.getClass()) {
            payload.add(t);
            t.setParent(this);
        } else {
            throw new NBTException("Wrong tag type!");
        }
    }
    
    public void remTag(int index) {
        payload.remove(index);
    }
    
    public void removeAll() {
        payload.clear();
    }

    public void setPayload(Tag[] payload) {
        this.payload = new ArrayList<>(Arrays.asList(payload));
    }

    public Tag[] getPayload() {
        return payload.toArray(new Tag[0]);
    }
    
    public String toString() {
        String s = "TAG_List("+name+"): {";
        for(Tag t: payload) {
            s += t.toString();
        }
        return s + "}";
    }
    
    public String toString2() {
        return toString2("", "    ");
    }
    
    public String toString2(String lineStarter, String indent) {
        String s = "TAG_List("+name+") ("+payload.size()+" entries): {\n";
        for(Tag t: payload) {
            s += lineStarter + indent + payload.indexOf(t) + ": ";
            if(t instanceof TagCompound) {
                s += ((TagCompound)t).toString2(lineStarter + indent, indent) + "\n";
            } else if(t instanceof TagList) {
                s += ((TagList)t).toString2(lineStarter + indent, indent) + "\n";
            } else {
                s += t.toString() + "\n";
            }
        }
        return s.substring(0, s.length() - 1) + "\n" + lineStarter + "}";
    }
}
