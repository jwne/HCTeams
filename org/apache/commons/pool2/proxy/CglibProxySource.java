package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.*;
import net.sf.cglib.proxy.*;

public class CglibProxySource<T> implements ProxySource<T>
{
    private final Class<? extends T> superclass;
    
    public CglibProxySource(final Class<? extends T> superclass) {
        super();
        this.superclass = superclass;
    }
    
    @Override
    public T createProxy(final T pooledObject, final UsageTracking<T> usageTracking) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass((Class)this.superclass);
        final CglibProxyHandler<T> proxyInterceptor = new CglibProxyHandler<T>(pooledObject, usageTracking);
        enhancer.setCallback((Callback)proxyInterceptor);
        final T proxy = (T)enhancer.create();
        return proxy;
    }
    
    @Override
    public T resolveProxy(final T proxy) {
        final CglibProxyHandler<T> cglibProxyHandler = (CglibProxyHandler<T>)((Factory)proxy).getCallback(0);
        final T pooledObject = cglibProxyHandler.disableProxy();
        return pooledObject;
    }
}
