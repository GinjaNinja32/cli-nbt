package com.gn32.api.data.nbt;

import java.util.ArrayList;

/**
 *
 * @author GinjaNinja32
 */
public class NBTMarkupTranslator {
    private NBTMarkupTranslator() {}
    
    static final String sep1=":", sep2= "=", end=";";
    
    private static class ModifiableString {
        public String s;

        public ModifiableString(String s) {
            this.s = s;
        }
    }
    
    public static TagCompound compile(String s) {
        s = stripWhitespace(s);
        ModifiableString ms = new ModifiableString(s);
        return readCompound(ms, true);
    }
    
    private static TagCompound readCompound(ModifiableString s, boolean named) {
        confirm("compound"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        confirm("{", s);
        TagCompound tag = new TagCompound(name);
        while(!s.s.startsWith("}")) {
            if("".equals(s.s)) {
                throw new IllegalArgumentException("Reached end of file while parsing!");
            }
            // test "bytearr" and "intarr" BEFORE "byte" and "int"
            if(s.s.startsWith("bytearr")) {
                tag.addTag(readByteArr(s, true));
            } else if(s.s.startsWith("intarr")) {
                tag.addTag(readIntArr(s, true));
            } else if(s.s.startsWith("byte")) {
                tag.addTag(readByte(s, true));
            } else if(s.s.startsWith("short")) {
                tag.addTag(readShort(s, true));
            } else if(s.s.startsWith("int")) {
                tag.addTag(readInt(s, true));
            } else if(s.s.startsWith("long")) {
                tag.addTag(readLong(s, true));
            } else if(s.s.startsWith("float")) {
                tag.addTag(readFloat(s, true));
            } else if(s.s.startsWith("double")) {
                tag.addTag(readDouble(s, true));
            } else if(s.s.startsWith("string")) {
                tag.addTag(readStringTag(s, true));
            } else if(s.s.startsWith("list")) {
                tag.addTag(readList(s, true));
            } else if(s.s.startsWith("compound")) {
                tag.addTag(readCompound(s, true));
            }
        }
        s.s = s.s.substring(1);
        return tag;
    }
    
    private static TagList readList(ModifiableString s, boolean named) {
        confirm("list"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        confirm("[", s);
        TagList tag = new TagList(name);
        while(!s.s.startsWith("]")) {
            if("".equals(s.s)) {
                throw new IllegalArgumentException("Reached end of file while parsing!");
            }
            try {
                // test "bytearr" and "intarr" BEFORE "byte" and "int"
                if(s.s.startsWith("bytearr")) {
                    tag.addTag(readByteArr(s, false));
                } else if(s.s.startsWith("intarr")) {
                    tag.addTag(readIntArr(s, false));
                } else if(s.s.startsWith("byte")) {
                    tag.addTag(readByte(s, false));
                } else if(s.s.startsWith("short")) {
                    tag.addTag(readShort(s, false));
                } else if(s.s.startsWith("int")) {
                    tag.addTag(readInt(s, false));
                } else if(s.s.startsWith("long")) {
                    tag.addTag(readLong(s, false));
                } else if(s.s.startsWith("float")) {
                    tag.addTag(readFloat(s, false));
                } else if(s.s.startsWith("double")) {
                    tag.addTag(readDouble(s, false));
                } else if(s.s.startsWith("string")) {
                    tag.addTag(readStringTag(s, false));
                } else if(s.s.startsWith("list")) {
                    tag.addTag(readList(s, false));
                } else if(s.s.startsWith("compound")) {
                    tag.addTag(readCompound(s, false));
                }
            } catch(NBTException nbte) {
                throw new IllegalArgumentException("Attempted to add tag of wrong type to a list. All tags in a list must be the same type!", nbte);
            }
        }
        s.s = s.s.substring(1);
        return tag;
    }
    
    private static TagByte readByte(ModifiableString s, boolean named) {
        confirm("byte"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        byte val = Byte.parseByte(readNumber(s));
        confirm(end, s);
        return new TagByte(name, val);
    }
    private static TagShort readShort(ModifiableString s, boolean named) {
        confirm("short"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        short val = Short.parseShort(readNumber(s));
        confirm(end, s);
        return new TagShort(name, val);
    }
    private static TagInt readInt(ModifiableString s, boolean named) {
        confirm("int"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        int val = Integer.parseInt(readNumber(s));
        confirm(end, s);
        return new TagInt(name, val);
    }
    private static TagLong readLong(ModifiableString s, boolean named) {
        confirm("long"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        long val = Long.parseLong(readNumber(s));
        confirm(end, s);
        return new TagLong(name, val);
    }
    private static TagFloat readFloat(ModifiableString s, boolean named) {
        confirm("float"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        float val = Float.parseFloat(readNumber(s));
        confirm(end, s);
        return new TagFloat(name, val);
    }
    private static TagDouble readDouble(ModifiableString s, boolean named) {
        confirm("double"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        double val = Double.parseDouble(readNumber(s));
        confirm(end, s);
        return new TagDouble(name, val);
    }
    private static TagByteArray readByteArr(ModifiableString s, boolean named) {
        confirm("bytearr"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        ArrayList<Byte> arr = new ArrayList<>();
        arr.add(Byte.parseByte(readNumber(s)));
        while(s.s.startsWith(",")) {
            s.s = s.s.substring(1);
            arr.add(Byte.parseByte(readNumber(s)));
        }
        confirm(end, s);
        return new TagByteArray(name, arr);
    }
    private static TagString readStringTag(ModifiableString s, boolean named) {
        confirm("string"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        String val = readString(s);
        confirm(end, s);
        return new TagString(name, val);
    }
    private static TagIntArray readIntArr(ModifiableString s, boolean named) {
        confirm("intarr"+sep1, s);
        String name = "";
        if(named) {
            name = readString(s);
            confirm(sep2, s);
        }
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(Integer.parseInt(readNumber(s)));
        while(s.s.startsWith(",")) {
            s.s = s.s.substring(1);
            arr.add(Integer.parseInt(readNumber(s)));
        }
        confirm(end, s);
        return new TagIntArray(name, arr);
    }
    
    private static void confirm(String s, ModifiableString ms) {
        if(ms.s.startsWith(s)) {
            ms.s = ms.s.substring(s.length());
        } else {
            throw new IllegalArgumentException("Error parsing file. Remaining string:" + ms.s);
        }
    }
    
    private static String readNumber(ModifiableString s) {
        String s1 = "";
        int chars = 0;
        boolean hasDecimal = false;
        boolean hasE = false;
        if(s.s.startsWith("-")) {
            s1 += "-";
            s.s = s.s.substring(1);
        }
        loop:
        for(char c: s.s.toCharArray()) {
            switch(c) {
                case 'E':
                    if(hasE) {
                        throw new IllegalArgumentException("Too many 'E's! Remaining string:"+s.s);
                    }
                    hasE = true;
                    
                    s1 += c;
                    chars++;
                    break;
                case '.':
                    if(hasDecimal) {
                        throw new IllegalArgumentException("Too many decimal points! Remaining string:"+s.s);
                    }
                    hasDecimal = true;
                    // FALL THRU
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    s1 += c;
                    chars++;
                    break;
                default:
                    break loop;
            }
        }
        s.s = s.s.substring(chars);
        return s1;
    }
    
    private static String readString(ModifiableString s) {
        if(!s.s.startsWith("\"")) {
            throw new IllegalArgumentException("Expected quote. Remaining string:" + s.s);
        }
        s.s = s.s.substring(1);
        String s1 = "";
        boolean literal = false;
        int extraChars = 0;
        for(char c: s.s.toCharArray()) {
            switch(c) {
                case '"':
                    System.out.println("Found \": literal="+literal);
                    if(!literal) {
                        s.s = s.s.substring(1+s1.length()+extraChars);
                        return s1;
                    } else {
                        literal = false;
                        s1 += '"';
                    }
                    break;
                case '\\':
                    System.out.println("Found \\: literal="+literal);
                    if(literal) {
                        s1 += '\\';
                    } else {
                        extraChars++;
                    }
                    literal = !literal;
                    break;
                default:
                    s1 += c;
            }
        }
        throw new IllegalArgumentException("Reached end of file while parsing!");
    }
    
    public static String output(TagCompound rootTag, boolean named, String indent, String indentBase) throws NBTException {
        String s = "";
        if(named) {
            s += indent+"compound"+sep1+"\""+name(rootTag)+"\""+sep2+"{\n";
        } else {
            s += indent+"compound"+sep1+"{\n";
        }
        for(Tag t: rootTag.payload) {
            if(t instanceof TagByteArray) {
                s += indent+indentBase+"bytearr"+sep1+"\""+name(t)+"\""+sep2+toStr(t.payloadByteArray())+end+"\n";
            } else if(t instanceof TagIntArray) {
                s += indent+indentBase+"intarr"+sep1+"\""+name(t)+"\""+sep2+toStr(t.payloadIntArray())+end+"\n";
            } else if(t instanceof TagString) {
                s += indent+indentBase+"string"+sep1+"\""+name(t)+"\""+sep2+"\""+t.payloadString()+"\""+end+"\n";
            } else if(t instanceof TagList) {
                s += outputList(t.asListTag(), indent+indentBase, indentBase);
            } else if(t instanceof TagCompound) {
                s += output(t.asCompoundTag(), true, indent+indentBase, indentBase);
            } else {
                s += indent+indentBase+t.type + ""+sep1+"\"" + name(t) + "\""+sep2 + t.payloadGeneric() + end+"\n";
            }
        }
        return s + indent+"}\n";
    }
    
    public static String outputList(TagList tag, String indent, String indentBase) throws NBTException {
        String s = "";
        s += indent+"list"+sep1+"\""+name(tag)+"\""+sep2+"[\n";
            for(Tag t: tag.payload) {
                if(t instanceof TagByteArray) {
                    s += indent+indentBase+"bytearr"+sep1+toStr(t.payloadByteArray())+end+"\n";
                } else if(t instanceof TagIntArray) {
                    s += indent+indentBase+"intarr"+sep1+toStr(t.payloadIntArray())+end+"\n";
                } else if(t instanceof TagString) {
                    s += indent+indentBase+"string"+sep1+t.payloadString()+"\""+end+"\n";
                } else if(t instanceof TagList) {
                    s += outputList(t.asListTag(), indent+indentBase, indentBase);
                } else if(t instanceof TagCompound) {
                    s += output(t.asCompoundTag(), false, indent+indentBase, indentBase);
                } else {
                    s += indent+indentBase+t.type + sep1 + t.payloadGeneric() + end+"\n";
                }
            }
        return s + indent+"]\n";
    }
    private static String name(Tag t) {
        return t.name.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    private static String toStr(Object[] o) {
        String s = "";
        for(Object obj: o) {
            s += ", " + obj;
        }
        return s.substring(2);
    }

    public static String stripWhitespace(String s) {
        String newS = "";
        boolean inQuote = false;
        boolean literal = false;
        for(char c: s.toCharArray()) {
            switch(c) {
                case '"':
                    if(!literal) {
                        inQuote = !inQuote;
                    } else {
                        literal = false;
                    }
                    newS += '"';
                    break;
                case '\\':
                    literal = !literal;
                    newS += '\\';
                    break;
                case '\t':
                case ' ':
                case '\n':
                    if(!inQuote) {
                        break;
                    }
                default:
                    newS += c;
            }
        }
        return newS;
    }
}
