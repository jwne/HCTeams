package com.mongodb.util.management;

@Deprecated
public interface MBeanServer
{
    boolean isRegistered(String p0);
    
    void unregisterMBean(String p0);
    
    void registerMBean(Object p0, String p1);
}
