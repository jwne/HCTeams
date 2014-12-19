package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.*;
import java.lang.reflect.*;
import net.sf.cglib.proxy.*;

class CglibProxyHandler<T> extends BaseProxyHandler<T> implements MethodInterceptor
{
    CglibProxyHandler(final T pooledObject, final UsageTracking<T> usageTracking) {
        super(pooledObject, usageTracking);
    }
    
    public Object intercept(final Object object, final Method method, final Object[] args, final MethodProxy methodProxy) throws Throwable {
        return this.doInvoke(method, args);
    }
}
