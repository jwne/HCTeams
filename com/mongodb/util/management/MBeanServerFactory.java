package com.mongodb.util.management;

import com.mongodb.util.management.jmx.*;

@Deprecated
public class MBeanServerFactory
{
    private static final MBeanServer mBeanServer;
    
    public static MBeanServer getMBeanServer() {
        return MBeanServerFactory.mBeanServer;
    }
    
    static {
        MBeanServer tmp;
        try {
            tmp = new JMXMBeanServer();
        }
        catch (Throwable e) {
            tmp = new NullMBeanServer();
        }
        mBeanServer = tmp;
    }
}
