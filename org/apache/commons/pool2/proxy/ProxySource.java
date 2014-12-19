package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.*;

interface ProxySource<T>
{
    T createProxy(T p0, UsageTracking<T> p1);
    
    T resolveProxy(T p0);
}
