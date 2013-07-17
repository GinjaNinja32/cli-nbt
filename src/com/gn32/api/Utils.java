package com.gn32.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Methods that don't fit elsewhere
 * @author GinjaNinja32
 */
public class Utils {
    /**
     * The first 50 powers of 2, starting with 2^0 (i.e. {@code pow2[2] == 4} and {@code pow2[8] == 256}).
     */
    public static final long pow2[];
    static {
        pow2 = new long[50];
        for(int i=0; i<50; i++) {
            pow2[i] = (long)Math.pow(2, i);
        }
    }
    /** No debug info at all */
    public static final int DEBUG_NONE = 0;
    /** Only on crash */
    public static final int DEBUG_CRASH = 1;
    /** On error and crash (default) */
    public static final int DEBUG_ERROR = 2;
    /** On error, possible error, and crash */
    public static final int DEBUG_WARN = 3;
    /** Anything that is likely to be useful to the user */
    public static final int DEBUG_INFO = 4;
    /** Anything that has a slim chance of being useful to the user */
    public static final int DEBUG_VERBOSE = 5;
    /** Everything (for debugging) */
    public static final int DEBUG_VERBOSE2 = 6;
    
    private static final String[] levelNames = new String[] {"", "CRASH", "ERROR", "WARN", "INFO", "VERBOSE", "VERBOSE2"};
    
    private static int debugMode = DEBUG_ERROR;
    /**
     * List of primes less than 100.
     */
    private static short[] prime = new short[] {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 51, 53, 57, 59, 61, 67, 71, 73, 79, 81, 83, 87, 89, 91, 97
    };
    public static LinkedHashSet<Long> getFactors(long val) {
        if(val < 0) {
            return getFactors(-val);
        }
        LinkedHashSet<Long> s = new LinkedHashSet<>();
        LinkedHashSet<Long> s1 = new LinkedHashSet<>();
        double sqrt = Math.sqrt(val);
        for(long l=1; l <= sqrt; l++) {
            if((double)val % (double)l == 0) {
                s.add(l);
                s1.add(val/l);
            }
        }
        Long[] s1a = s1.toArray(new Long[0]);
        for(int i=s1a.length-1; i>=0; i--) {s.add(s1a[i]);}
        s.add(val);
        return s;
    }
    public static long getHCF(long val, long val2) {
        if(val < 0) { val = -val; }
        if(val2 < 0) { val2 = -val2; }
        
        if(val > val2) {
            for(long l=val2; l>0; l--) {
                if((double)val % (double)l == 0 && (double)val2 % (double)l == 0) {
                    return l;
                }
            }
        } else {
            for(long l=val; l>0; l--) {
                if((double)val2 % (double)l == 0) {
                    if((double)val % (double)l == 0) {
                        return l;
                    }
                }
            }
        }
        return 1;
    }
    public static boolean getNthBit(long val, int bit) {
        long bitval = (long)Math.pow(2, bit); // integer unless Math.pow breaks
        if(val < bitval) {
            // if number is too small, bit CANNOT be 1
            return false;
        }
        return ((val >> bit) % 2) == 1;
    }
    public static int bit(long val, int bit) {
        long bitval = (long)Math.pow(2, bit); // integer unless Math.pow breaks
        if(val < bitval) {
            // if number is too small, bit CANNOT be 1
            return 0;
        }
        long v = val >> bit;
        v = v % 2;
        return (int)v;
    }
    public static String formatNumber(int length, long val, boolean cut, String separator) {
        String s = Long.toString(val);
        if(s.length() > length && cut) {
            s = s.substring(0, length);
        }
        while(s.length() < length) {
            s = "0" + s;
        }
        if(!"".equals(separator)) {
            for(int i=s.length() - 3; i>0; i -= 3) {
                s = s.substring(0, i) + separator + s.substring(i);
            }
        }
        return s;
    }
    
    public static String formatNumber(int dp, double val, boolean extend) {
        String fmtString = "0.0";
        for(int i=1; i<dp; i++) {
            fmtString += extend ? "0" : "#";
        }
        return new DecimalFormat(fmtString).format(val);
    }
    public static long pow(long base, long exponent) {
        return (long)Math.pow(base, exponent);
    }
    public static long powExperimental(long base, long exponent) {
        // only need to check primes; x^4 == (x^2)^2
        for(int i=0; i<prime.length; i++) {
            while((exponent%prime[i])==0) { // while we can simplify a square
                base = pow(prime[i], base);
                exponent /= prime[i];
            }
            if(exponent == 1) {return base;}
        }
        return pow(base, exponent);
    }
    public static void print(String... s) {
        for(String s1 : s) {
            System.out.print(s1 + "\t");
        }
    }
    public static void print(boolean... b) {
        for(boolean b1: b) {
            System.out.print((b1?"true\t":"false\t"));
        }
    }
    public static void println(String... s) {
        print(s);
        System.out.println();
    }
    public static void println(boolean... b) {
        print(b);
        System.out.println();
    }
    public static void setDebug(int mode) {debugMode = mode;}
    public static void debug(Object caller, int level, String message) {
        if(debugMode >= level) {
            String s = "";
            if(level < levelNames.length && level >= 0) {
                s = "[" + levelNames[level] + "] ";
            }
            GregorianCalendar c = new GregorianCalendar();
            System.out.println("[" + Utils.formatNumber(2, c.get(GregorianCalendar.HOUR_OF_DAY), false, "")
                             + ":" + Utils.formatNumber(2, c.get(GregorianCalendar.MINUTE), false, "")
                             + ":" + Utils.formatNumber(2, c.get(GregorianCalendar.SECOND), false, "")
                             + "] " + s + caller.getClass().getSimpleName() + ": " + message);
        }
    }
    public static String explode(Object... o) {
        String s = "";
        for(Object o1 : o) {s += String.valueOf(o1) + " ";}
        return s.substring(0, s.length()-1);
    }
    public static String explode(boolean... o) {
        String s = "";
        for(boolean o1 : o) {s += (o1?"true ":"false ");}
        return s.substring(0, s.length()-1);
    }
//    public static String explode(long... o) {
//        String s = "";
//        for(long o1 : o) {s += Long.toString(o1) + " ";}
//        return s.substring(0, s.length()-1);
//    }
    public static String explode(int... o) {
        String s = "";
        for(int o1 : o) {s += Integer.toString(o1) + " ";}
        return s.substring(0, s.length()-1);
    }
    public static String explode(short... o) {
        String s = "";
        for(short o1 : o) {s += Short.toString(o1) + " ";}
        return s.substring(0, s.length()-1);
    }
//    public static String explode(double... o) {
//        String s = "";
//        for(double o1 : o) {s += Double.toString(o1) + " ";}
//        return s.substring(0, s.length()-1);
//    }
//    public static String explode(float... o) {
//        String s = "";
//        for(float o1 : o) {s += Float.toString(o1) + " ";}
//        return s.substring(0, s.length()-1);
//    }
    public static String explode(char... o) {
        return new String(o);
    }
    public static String explode(byte... o) {
        String s = "";
        for(byte o1 : o) {s += Byte.toString(o1) + " ";}
        return s.substring(0, s.length()-1);
    }
    
    public static double clamp(double min, double val, double max) {
        if(max < min) {throw new IllegalArgumentException("Maximum is less than minimum!");}
        return min > val ? min :
               max < val ? max : val;
    }
    public static double clamp2(double bounda, double val, double boundb) {
        if(bounda < boundb) {
            return clamp(bounda, val, boundb);
        } else {
            return clamp(boundb, val, bounda);
        }
    }
    public static double wrap(double min, double val, double max) {
        if(max < min) {throw new IllegalArgumentException("Maximum is less than minimum!");}
        double d = max - min;
        while(val >= max) {val -= d;}
        while(val < min) {val += d;}
        return val;
    }
    public static DataOutputStream setResourceAsStream(String filename) throws IOException {
        return new DataOutputStream(new FileOutputStream(new File(filename)));
    }
    public static DataOutputStream setResourceAsGZIPStream(String filename) throws IOException {
        return new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File(filename))));
    }
    public static DataInputStream getResourceAsStream(String filename) throws IOException {
        return new DataInputStream(new FileInputStream(new File(filename)));
    }
    public static DataInputStream getResourceAsGZIPStream(String filename) throws IOException {
        return new DataInputStream(new GZIPInputStream(new FileInputStream(new File(filename))));
    }
    public static String readFile(String filename) throws IOException {
        String s = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(getResourceAsStream(filename)));
        while(br.ready()) {
            s += br.readLine() + "\n";
        }
        return s;
    }
    public static String readFile(InputStream in) throws IOException {
        String s = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        while(br.ready()) {
            s += br.readLine() + "\n";
        }
        return s;
    }
    public static URL asURL(String fileLocation) throws MalformedURLException {
        return new File(fileLocation).toURI().toURL();
    }
    public static byte[] bytes(int... ints) {
        byte[] b = new byte[ints.length];
        for(int i=0; i<b.length; i++) {
            b[i] = (byte)ints[i];
        }
        return b;
    }
    public static short[] shorts(int... ints) {
        short[] s = new short[ints.length];
        for(int i=0; i<s.length; i++) {
            s[i] = (short)ints[i];
        }
        return s;
    }
    public static short byteBits(byte b) {
        return b < 0 ? (short)((short)b + pow2[8]) : b;
    }
    public static int shortBits(short s) {
        return s < 0 ? (int)s + (int)pow2[16] : s;
    }
    public static long intBits(int i) {
        return i < 0 ? (long)i + pow2[32] : i;
    }
    public static int addTillPositive(int val, int add) {
        while(val < 0) {
            val += add;
        }
        return val;
    }
    public static double transformRange(double value, double originalLow, double originalHigh, double finalLow, double finalHigh) {
        value -= originalLow; // [oL, oH] - oL : [0, oR]
        value /= originalHigh - originalLow; // [0, oR] / oR : [0, 1]
        value *= finalHigh - finalLow; // [0, 1] * fR : [0, fR]
        value += finalLow; // [0, fR] + fL : [fL, fH]
        return value;
    }
    public static boolean inRange(double low, double high, double val) {
        return low <= val && val <= high;
    }
    public static boolean inRangeExclusive(double low, double high, double val) {
        return low < val && val < high;
    }
    
    public static String hex(int i) {
        return Long.toString(intBits(i), 16);
    }
    public static String hex(short i) {
        return Long.toString(shortBits(i), 16);
    }
    public static String hex(byte i) {
        return Long.toString(byteBits(i), 16);
    }
    public static double moveTowards(double current, double target, double maxstep) {
        if(Math.abs(current-target) < maxstep) {return target;}
        return current - Math.signum(current-target)*maxstep;
    }
    public static double moveTowardsExpDecAccel(double current, double target, double decay) {
        return clamp2(0, current + (target-current)*decay, target);
    }
    
    public static int toInt(Integer i) {
        return i == null ? 0 : i;
    }
}