package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.*;

public class DefaultEvictionPolicy<T> implements EvictionPolicy<T>
{
    @Override
    public boolean evict(final EvictionConfig config, final PooledObject<T> underTest, final int idleCount) {
        return (config.getIdleSoftEvictTime() < underTest.getIdleTimeMillis() && config.getMinIdle() < idleCount) || config.getIdleEvictTime() < underTest.getIdleTimeMillis();
    }
}
