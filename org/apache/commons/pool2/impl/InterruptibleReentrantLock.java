package org.apache.commons.pool2.impl;

import java.util.concurrent.locks.*;
import java.util.*;

class InterruptibleReentrantLock extends ReentrantLock
{
    private static final long serialVersionUID = 1L;
    
    public void interruptWaiters(final Condition condition) {
        final Collection<Thread> threads = this.getWaitingThreads(condition);
        for (final Thread thread : threads) {
            thread.interrupt();
        }
    }
}
