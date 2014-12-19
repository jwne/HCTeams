package com.mongodb.util.management.jmx;

import com.mongodb.util.management.*;
import java.lang.management.*;
import java.util.logging.*;
import javax.management.*;

@Deprecated
public class JMXMBeanServer implements MBeanServer
{
    private static final Logger LOGGER;
    private final javax.management.MBeanServer server;
    
    public JMXMBeanServer() {
        super();
        this.server = ManagementFactory.getPlatformMBeanServer();
    }
    
    public boolean isRegistered(final String mBeanName) {
        try {
            return this.server.isRegistered(this.createObjectName(mBeanName));
        }
        catch (MalformedObjectNameException e) {
            JMXMBeanServer.LOGGER.log(Level.WARNING, "Unable to register MBean " + mBeanName, e);
            return false;
        }
    }
    
    public void unregisterMBean(final String mBeanName) {
        try {
            this.server.unregisterMBean(this.createObjectName(mBeanName));
        }
        catch (Exception e) {
            JMXMBeanServer.LOGGER.log(Level.WARNING, "Unable to register MBean " + mBeanName, e);
        }
    }
    
    public void registerMBean(final Object mBean, final String mBeanName) {
        try {
            this.server.registerMBean(mBean, this.createObjectName(mBeanName));
        }
        catch (InstanceAlreadyExistsException e2) {
            JMXMBeanServer.LOGGER.log(Level.INFO, String.format("A JMX MBean with the name '%s' already exists", mBeanName));
        }
        catch (Exception e) {
            JMXMBeanServer.LOGGER.log(Level.WARNING, "Unable to register MBean " + mBeanName, e);
        }
    }
    
    private ObjectName createObjectName(final String mBeanName) throws MalformedObjectNameException {
        return new ObjectName(mBeanName);
    }
    
    static {
        LOGGER = Logger.getLogger("com.mongodb.driver.management");
    }
}
