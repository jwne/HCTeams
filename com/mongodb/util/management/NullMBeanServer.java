package com.mongodb.util.management;

@Deprecated
public class NullMBeanServer implements MBeanServer
{
    public boolean isRegistered(final String mBeanName) {
        return false;
    }
    
    public void unregisterMBean(final String mBeanName) {
    }
    
    public void registerMBean(final Object mBean, final String mBeanName) {
    }
}
