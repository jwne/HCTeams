package org.apache.commons.pool2.impl;

import java.util.*;
import java.security.*;

class EvictionTimer
{
    private static Timer _timer;
    private static int _usageCount;
    
    static synchronized void schedule(final TimerTask task, final long delay, final long period) {
        if (null == EvictionTimer._timer) {
            final ClassLoader ccl = AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedGetTccl());
            try {
                AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedSetTccl(EvictionTimer.class.getClassLoader()));
                EvictionTimer._timer = new Timer("commons-pool-EvictionTimer", true);
            }
            finally {
                AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedSetTccl(ccl));
            }
        }
        ++EvictionTimer._usageCount;
        EvictionTimer._timer.schedule(task, delay, period);
    }
    
    static synchronized void cancel(final TimerTask task) {
        task.cancel();
        --EvictionTimer._usageCount;
        if (EvictionTimer._usageCount == 0) {
            EvictionTimer._timer.cancel();
            EvictionTimer._timer = null;
        }
    }
    
    private static class PrivilegedGetTccl implements PrivilegedAction<ClassLoader>
    {
        @Override
        public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
    
    private static class PrivilegedSetTccl implements PrivilegedAction<Void>
    {
        private final ClassLoader cl;
        
        PrivilegedSetTccl(final ClassLoader cl) {
            super();
            this.cl = cl;
        }
        
        @Override
        public Void run() {
            Thread.currentThread().setContextClassLoader(this.cl);
            return null;
        }
    }
}
