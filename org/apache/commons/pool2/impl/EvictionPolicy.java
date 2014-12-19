package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.*;

public interface EvictionPolicy<T>
{
    boolean evict(EvictionConfig p0, PooledObject<T> p1, int p2);
}
