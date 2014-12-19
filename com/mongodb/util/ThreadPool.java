package com.mongodb.util;

import java.util.concurrent.atomic.*;
import java.util.*;
import java.util.concurrent.*;

@Deprecated
public abstract class ThreadPool<T>
{
    final String _name;
    final int _maxThreads;
    private final AtomicInteger _inProgress;
    private final List<MyThread> _threads;
    private final BlockingQueue<T> _queue;
    private final MyThreadGroup _myThreadGroup;
    
    public ThreadPool(final String name, final int numThreads) {
        this(name, numThreads, Integer.MAX_VALUE);
    }
    
    public ThreadPool(final String name, final int numThreads, final int maxQueueSize) {
        super();
        this._inProgress = new AtomicInteger(0);
        this._threads = new Vector<MyThread>();
        this._name = name;
        this._maxThreads = numThreads;
        this._queue = new LinkedBlockingQueue<T>(maxQueueSize);
        this._myThreadGroup = new MyThreadGroup();
        this._threads.add(new MyThread());
    }
    
    public abstract void handle(final T p0) throws Exception;
    
    public abstract void handleError(final T p0, final Exception p1);
    
    public int queueSize() {
        return this._queue.size();
    }
    
    public boolean offer(final T t) {
        if ((this._queue.size() > 0 || this._inProgress.get() == this._threads.size()) && this._threads.size() < this._maxThreads) {
            this._threads.add(new MyThread());
        }
        return this._queue.offer(t);
    }
    
    public int inProgress() {
        return this._inProgress.get();
    }
    
    public int numThreads() {
        return this._threads.size();
    }
    
    class MyThreadGroup extends ThreadGroup
    {
        MyThreadGroup() {
            super("ThreadPool.MyThreadGroup:" + ThreadPool.this._name);
        }
        
        public void uncaughtException(final Thread t, final Throwable e) {
            for (int i = 0; i < ThreadPool.this._threads.size(); ++i) {
                if (ThreadPool.this._threads.get(i) == t) {
                    ThreadPool.this._threads.remove(i);
                    break;
                }
            }
        }
    }
    
    class MyThread extends Thread
    {
        MyThread() {
            super(ThreadPool.this._myThreadGroup, "ThreadPool.MyThread:" + ThreadPool.this._name + ":" + ThreadPool.this._threads.size());
            this.setDaemon(true);
            this.start();
        }
        
        public void run() {
            while (true) {
                T t = null;
                try {
                    t = ThreadPool.this._queue.take();
                }
                catch (InterruptedException ex) {}
                if (t == null) {
                    continue;
                }
                try {
                    ThreadPool.this._inProgress.incrementAndGet();
                    ThreadPool.this.handle(t);
                }
                catch (Exception e) {
                    ThreadPool.this.handleError(t, e);
                }
                finally {
                    ThreadPool.this._inProgress.decrementAndGet();
                }
            }
        }
    }
}
