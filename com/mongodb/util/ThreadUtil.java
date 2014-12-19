package com.mongodb.util;

import java.util.*;

@Deprecated
public class ThreadUtil
{
    private static final Map<Long, FastStack<String>> _threads;
    
    public static void printStackTrace() {
        final Exception e = new Exception();
        e.fillInStackTrace();
        e.printStackTrace();
    }
    
    public static void sleep(final long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) {}
    }
    
    public static void pushStatus(final String what) {
        pushStatus(Thread.currentThread(), what);
    }
    
    public static void pushStatus(final Thread t, final String what) {
        getStatus(t).push(what);
    }
    
    public static void clearStatus() {
        clearStatus(Thread.currentThread());
    }
    
    public static void clearStatus(final Thread t) {
        getStatus(t).clear();
    }
    
    public static FastStack<String> getStatus() {
        return getStatus(Thread.currentThread());
    }
    
    public static FastStack<String> getStatus(final Thread t) {
        FastStack<String> s = ThreadUtil._threads.get(t.getId());
        if (s == null) {
            s = new FastStack<String>();
            ThreadUtil._threads.put(t.getId(), s);
        }
        return s;
    }
    
    static {
        _threads = Collections.synchronizedMap(new HashMap<Long, FastStack<String>>());
    }
}
