package com.gn32.api.data.nbt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 *
 * @author Ben
 */
public class TagCompound extends Tag {
    static final int ID = 10;
    ArrayList<Tag> payload = new ArrayList<>();

    public TagCompound(String name, Tag... payload) {
        super(name);
        this.payload.addAll(Arrays.asList(payload));
    }
    
    public void addTag(Tag t) {
        payload.add(t);
        t.setParent(this);
    }
    public Tag getTag(String name) {
        for(Tag t: payload) {
            if(t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }
    public void remTag(String name, boolean regexp) {
        if(regexp) {
            Pattern p = Pattern.compile(name);
            for(int i=0; i<payload.size(); i++) {
                if(Pattern.matches(name, payload.get(i).name)) {
                    payload.remove(i);
                    i--;
                }
            }
        } else {
            for(int i=0; i<payload.size(); i++) {
                 if(payload.get(i).name.equals(name)) {
                    payload.remove(i);
                    i--;
                }
            }
        }
    }
    
    public void removeAll() {
        payload.clear();
    }
    
    public String toString() {
        String s = "TAG_Compound("+name + ") {";
        for(Tag t: payload) {
            s += t.toString() + " ";
        }
        if(payload.isEmpty()) {
            return s + "}";
        } else {
            return s.substring(0, s.length() - 1) + "}";
        }
    }
    
    public String toString2() {
        return toString2("", "    ");
    }
    
    public String toString2(String lineStarter, String indent) {
        String s = "TAG_Compound("+name + ") ("+payload.size()+" entries): {\n";
        for(Tag t: payload) {
            if(t instanceof TagCompound) {
                s += lineStarter + indent + ((TagCompound)t).toString2(lineStarter + indent, indent) + "\n";
            } else if(t instanceof TagList) {
                s += lineStarter + indent + ((TagList)t).toString2(lineStarter + indent, indent) + "\n";
            } else {
                s += lineStarter + indent + t.toString() + "\n";
            }
        }
        return s.substring(0, s.length() - 1) + "\n" + lineStarter + "}";
    }
}
