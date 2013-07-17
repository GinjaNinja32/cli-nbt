package com.gn32.apps.clinbt;

import com.gn32.api.Utils;
import com.gn32.api.data.nbt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author GinjaNinja32
 */
public class Main {
    static NBT nbt;
    static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) {
        try {
            if(args.length == 0) {
                blank();
            } else {
                switch(args[0]) {
                    case "-c": {
                        if(args.length < 3) {
                            System.out.println("Must specify a file to compile and a file to save compiled data to after -c!");
                            System.exit(0);
                        }
                        DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(args[2])));
                        nbt = new NBT(NBTMarkupTranslator.compile(Utils.readFile(args[1])));
                        nbt.writeNBT(out);
                        System.out.println("Converted successfully.");
                        System.exit(0);
                        break;
                    }
                    case "-d": {
                        if(args.length < 3) {
                            System.out.println("Must specify a file to decode and a file to save decoded data to after -d!");
                            System.exit(0);
                        }
                        DataInputStream in = new DataInputStream(new FileInputStream(new File(args[1])));
                        DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(args[2])));
                        nbt = NBT.readNBT(in);
                        String s = NBTMarkupTranslator.output(nbt.getMain(), true, "", "    ");
                        for(char c: s.toCharArray()) {
                            out.write(c);
                        }
                        System.out.println("Converted successfully.");
                        System.exit(0);
                        break;
                    }
                    case "-m": {
                        if(args.length < 2) {
                            System.out.println("Must specify a file to read after -m!");
                            System.exit(0);
                        }
                        nbt = new NBT(NBTMarkupTranslator.compile(Utils.readFile(args[1])));
                        currentTag = nbt.getMain();
                        break;
                    }
                    default:
                        println("Reading file '" + args[0] + "'");
                        load(args[0]);
                }
            }
            while(true) {
                print("> ");
                action(in.readLine());
            }
        } catch(IOException ioe) {
            println("Unexpected error!");
            println("Error: " + ioe.getMessage());
            println("Stack trace: ");
            for(StackTraceElement ste: ioe.getStackTrace()) {
                println("    " + ste);
            }
        }
    }
    
    static Tag currentTag;
    
    static void action(String input) throws IOException {
        if(input.startsWith("add ")) {
            addTag(input.substring(4));
        } else if(input.startsWith("rem ")) {
            remTag(input.substring(4));
        } else if(input.startsWith("cd ")) {
            cd(input.substring(3));
        } else if(input.startsWith("save ")) {
            save(input.substring(5));
        } else if(input.startsWith("load ")) {
            load(input.substring(5));
        } else {
            switch(input) {
                case "ls":  if(currentTag instanceof TagIntArray) {
                                System.out.println(((TagIntArray)currentTag).toString3());
                            } else if(currentTag instanceof TagByteArray) {
                                System.out.println(((TagByteArray)currentTag).toString3());
                            } else {
                                System.out.println(currentTag.toString2());
                            } break;
                case "new": blank(); break;
                case "clr": clear(); break;
                case "exit": exit("Exiting...", 0);
                case "help": help(); break;
                default:
                    println("Unrecognised command. Try 'help' for help.");
            }
        }
    }
    
    static void help() {
        println(" Command            \tResult");
        println("  add <type>        \t Add a new tag");
        println("  rem <name|index>  \t Remove a tag");
        println("  rem -r <regexp>   \t Remove all tags matching a regular expression");
        println("  ls                \t View the contents of the current tag");
        println("  cd                \t Change to another tag. Path is '/' or '\\'-separated, '..' is parent.");
        println("  clr               \t Clear (empty) the current tag");
        println("  exit              \t Exits the program without saving");
        println("  save <filename>   \t Save to the specified file.");
        println("  save -g <filename>\t Save to the specified file with GZIP compression.");
        println("  load <filename>   \t Load from the specified file.");
        println("  load -g <filename>\t Load from the specified file with GZIP compression.");
        println("  help              \t Show this help text");
    }
    
    static void addTag(String arg) throws IOException {
        if(currentTag instanceof TagCompound) {
            TagCompound ct = (TagCompound)currentTag;
            switch(arg) {
                case "byte": ct.addTag(newByte(true)); break;
                case "short": ct.addTag(newShort(true)); break;
                case "int": ct.addTag(newInt(true)); break;
                case "long": ct.addTag(newLong(true)); break;
                case "float": ct.addTag(newFloat(true)); break;
                case "double": ct.addTag(newDouble(true)); break;
                case "bytearr": ct.addTag(newByteArr(true)); break;
                case "string": ct.addTag(newString(true)); break;
                case "list": ct.addTag(newList(true)); break;
                case "comp": ct.addTag(newCompound(true)); break;
                case "intarr": ct.addTag(newIntArr(true)); break;
            }
        } else if(currentTag instanceof TagList) {
            TagList ct = (TagList)currentTag;
            try {
                switch(arg) {
                    case "byte": ct.addTag(newByte(false)); break;
                    case "short": ct.addTag(newShort(false)); break;
                    case "int": ct.addTag(newInt(false)); break;
                    case "long": ct.addTag(newLong(false)); break;
                    case "float": ct.addTag(newFloat(false)); break;
                    case "double": ct.addTag(newDouble(false)); break;
                    case "bytearr": ct.addTag(newByteArr(false)); break;
                    case "strint": ct.addTag(newString(false)); break;
                    case "list": ct.addTag(newList(false)); break;
                    case "comp": ct.addTag(newCompound(false)); break;
                    case "intarr": ct.addTag(newIntArr(false)); break;
                }
            } catch(NBTException nbte) {
                if("Wrong tag type!".equals(nbte.getMessage())) {
                    println("Wrong tag type!");
                } else {
                    throw nbte;
                }
            }
        } else if(currentTag instanceof TagByteArray) {
            TagByteArray tag = (TagByteArray)currentTag;
            print("Enter position to insert at: ");
            int pos = Integer.parseInt(in.readLine());
            if(pos > tag.getPayloadArrayList().size()) {
                pos = tag.getPayloadArrayList().size();
            } else if(pos < 0) {
                pos = 0;
            }
            tag.getPayloadArrayList().add(pos, Byte.parseByte(arg));
        } else if(currentTag instanceof TagIntArray) {
            TagIntArray tag = (TagIntArray)currentTag;
            print("Enter position to insert at: ");
            int pos = Integer.parseInt(in.readLine());
            if(pos > tag.getPayloadArrayList().size()) {
                pos = tag.getPayloadArrayList().size();
            } else if(pos < 0) {
                pos = 0;
            }
            tag.getPayloadArrayList().add(pos, Integer.parseInt(arg));
        } else {
            println("Current tag cannot be added to!");
        }
    }
    
    static void remTag(String arg) throws IOException {
            if(currentTag instanceof TagCompound) {
                if(arg.startsWith("-r ")) {
                    arg = arg.substring(3);
                    try {
                        ((TagCompound)currentTag).remTag(arg, true);
                    } catch(PatternSyntaxException pse) {
                        println("Invalid regular expression: " + pse.getMessage());
                    }
                } else {
                    ((TagCompound)currentTag).remTag(arg, false);
                }
            } else if(currentTag instanceof TagList) {
                ((TagList)currentTag).remTag(Integer.parseInt(arg));
            } else if(currentTag instanceof TagByteArray) {
                TagByteArray tag = (TagByteArray)currentTag;
                tag.getPayloadArrayList().remove(Integer.parseInt(arg));
            } else if(currentTag instanceof TagIntArray) {
                TagIntArray tag = (TagIntArray)currentTag;
                tag.getPayloadArrayList().remove(Integer.parseInt(arg));
            } else {
                throw new IllegalStateException("Illegal state!");
            }
    }
    
    static void clear() {
        if(currentTag instanceof TagCompound) {
            ((TagCompound)currentTag).removeAll();
        } else if(currentTag instanceof TagList) {
            ((TagList)currentTag).removeAll();
        }
    }
    
    static void cd(String arg) throws NBTException {
        String[] path = arg.split("\\|/");
        for(String s: path) {
            if("..".equals(s)) {
                if(currentTag != nbt.getMain()) {
                    currentTag = currentTag.getParent();
                } else {
                    println("Cannot go up!");
                }
            } else {
                if(currentTag instanceof TagCompound) {
                    boolean found = false;
                    for(Tag t: currentTag.payloadTagCompound()) {
                        if(t.getName().equals(s) && (t instanceof TagCompound || t instanceof TagList || t instanceof TagByteArray || t instanceof TagIntArray)) {
                            currentTag = t;
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        println("Invalid path: '" + arg + "'. Tag either does not exist or is not a TAG_Compound, TAG_List, TAG_Byte_Array, ot TAG_Int_Array.");
                    }
                } else {
                    println("Invalid path: '" + arg + "'. Tag either does not exist or is not a TAG_Compound, TAG_List, TAG_Byte_Array, ot TAG_Int_Array.");
                }
            }
        }
    }
    
    static void save(String arg) throws IOException {
        try {
            if(arg.startsWith("-g ")) {
                nbt.writeNBT(Utils.setResourceAsGZIPStream(arg.substring(3)));
            } else {
                nbt.writeNBT(Utils.setResourceAsStream(arg));
            }
        } catch(NBTException nbte) {
            println("Error: " + nbte.getMessage());
        }
    }
    
    static void load(String arg) throws IOException {
        try {
            if(arg.startsWith("-g ")) {
                nbt = NBT.readNBT(Utils.getResourceAsGZIPStream(arg.substring(3)));
            } else {
                nbt = NBT.readNBT(Utils.getResourceAsStream(arg));
            }
            currentTag = nbt.getMain();
        } catch(NBTException nbte) {
            println("Error: " + nbte.getMessage());
            println("Try using a GZIP stream (use load -g <filename>).");
        } catch(FileNotFoundException fnfe) {
            println("File not found.");
        }
    }
    
    static void blank() throws IOException {
        print("Starting new blank file. Please input name of top-level tag: ");
        nbt = NBT.blankNBT(in.readLine());
        currentTag = nbt.getMain();
    }
    
    static void print(String s) {
        System.out.print(s);
    }
    static void println(String s) {
        System.out.println(s);
    }
    static void exit(String s, int code) {
        System.out.println(s);
        System.exit(code);
    }
    
    
    static TagByte newByte(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (byte): ");
        return new TagByte(name, Byte.parseByte(in.readLine()));
    }
    static TagShort newShort(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (short): ");
        return new TagShort(name, Short.parseShort(in.readLine()));
    }
    static TagInt newInt(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (int): ");
        return new TagInt(name, Integer.parseInt(in.readLine()));
    }
    static TagLong newLong(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (long): ");
        return new TagLong(name, Long.parseLong(in.readLine()));
    }
    static TagFloat newFloat(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (float): ");
        return new TagFloat(name, Float.parseFloat(in.readLine()));
    }
    static TagDouble newDouble(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (double): ");
        return new TagDouble(name, Double.parseDouble(in.readLine()));
    }
    static TagByteArray newByteArr(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        ArrayList<Byte> array = new ArrayList<>();
        
        String s;
        while(true) {
            print("Enter a byte, or 'end' to finish: ");
            if(!"end".equals(s = in.readLine())) {
                try {
                    array.add(Byte.parseByte(s));
                } catch(NumberFormatException nfe) {
                    println("Invalid entry!");
                }
            } else {
                break;
            }
        }
        return new TagByteArray(name, array);
    }
    static TagString newString(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        print("Enter payload (string): ");
        return new TagString(name, in.readLine());
    }
    static TagList newList(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        return new TagList(name, new Tag[0]);
    }
    static TagCompound newCompound(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        return new TagCompound(name, new Tag[0]);
    }
    static TagIntArray newIntArr(boolean named) throws IOException {
        String name = "";
        if(named) {
            print("Enter name: ");
            name = in.readLine();
        }
        ArrayList<Integer> array = new ArrayList<>();
        
        String s;
        while(true) {
            print("Enter an int, or 'end' to finish: ");
            if(!"end".equals(s = in.readLine())) {
                try {
                    array.add(Integer.parseInt(s));
                } catch(NumberFormatException nfe) {
                    println("Invalid entry!");
                }
            } else {
                break;
            }
        }
        return new TagIntArray(name, array);
    }
    
    
}
