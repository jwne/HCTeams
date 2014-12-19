package com.mongodb.tools;

import java.lang.management.*;
import java.io.*;
import javax.management.remote.*;
import java.util.*;
import javax.management.openmbean.*;
import javax.management.*;

@Deprecated
public class ConnectionPoolStat
{
    private final MBeanServerConnection mBeanConnection;
    
    public ConnectionPoolStat(final MBeanServerConnection mBeanConnection) {
        super();
        this.mBeanConnection = mBeanConnection;
    }
    
    public ConnectionPoolStat() {
        super();
        this.mBeanConnection = ManagementFactory.getPlatformMBeanServer();
    }
    
    public String getStats() throws JMException, IOException {
        final CharArrayWriter charArrayWriter = new CharArrayWriter();
        final PrintWriter printWriter = new PrintWriter(charArrayWriter);
        this.print(printWriter);
        return charArrayWriter.toString();
    }
    
    public static void main(final String[] args) throws Exception {
        String host = "localhost";
        int port = -1;
        long rowCount = 0L;
        int sleepTime = 1000;
        int pos;
        for (pos = 0; pos < args.length; ++pos) {
            if (args[pos].equals("--help")) {
                printUsage();
                System.exit(0);
            }
            else if (args[pos].equals("--host") || args[pos].equals("-h")) {
                host = args[++pos];
            }
            else if (args[pos].equals("--port")) {
                port = getIntegerArg(args[++pos], "--port");
            }
            else if (args[pos].equals("--rowcount") || args[pos].equals("-n")) {
                rowCount = getIntegerArg(args[++pos], "--rowCount");
            }
            else {
                if (!args[pos].startsWith("-")) {
                    sleepTime = getIntegerArg(args[pos++], "sleep time") * 1000;
                    break;
                }
                printErrorAndUsageAndExit("unknown option " + args[pos]);
            }
        }
        if (pos != args.length) {
            printErrorAndUsageAndExit("too many positional options");
        }
        if (port == -1 && !host.contains(":")) {
            printErrorAndUsageAndExit("port is required");
        }
        final String hostAndPort = (port != -1) ? (host + ":" + port) : host;
        if (rowCount == 0L) {
            rowCount = Long.MAX_VALUE;
        }
        final JMXServiceURL u = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + hostAndPort + "/jmxrmi");
        final JMXConnector connector = JMXConnectorFactory.connect(u);
        final MBeanServerConnection mBeanConnection = connector.getMBeanServerConnection();
        try {
            final ConnectionPoolStat printer = new ConnectionPoolStat(mBeanConnection);
            for (int i = 0; i < rowCount; ++i) {
                System.out.println(printer.getStats());
                if (i != rowCount - 1L) {
                    Thread.sleep(sleepTime);
                }
            }
        }
        finally {
            connector.close();
        }
    }
    
    private static int getIntegerArg(final String arg, final String argName) {
        try {
            return Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            printErrorAndUsageAndExit(argName + " arg must be an integer");
            throw new IllegalStateException();
        }
    }
    
    private static void printErrorAndUsageAndExit(final String error) {
        System.err.println("ERROR: " + error);
        System.out.println();
        printUsage();
        System.exit(1);
    }
    
    private static void printUsage() {
        System.out.println("View live MongoDB connection pool statistics from a remote JMX server.");
        System.out.println();
        System.out.println("usage: java com.mongodb.tools.ConnectionPoolStat [options] [sleep time");
        System.out.println("sleep time: time to wait (in seconds) between calls. Defaults to 1");
        System.out.println("options:");
        System.out.println("  --help                 produce help message");
        System.out.println("  --port arg             JMX remote port. Required. Can also use --host hostname:port");
        System.out.println("  -h [ --host ] arg      JMX remote host. Defaults to localhost");
        System.out.println("  -n [ --rowcount ] arg  number of times to print stats (0 for indefinite)");
        System.out.println();
        System.out.println("Fields");
        System.out.println("  objectName                     - name of the JMX bean for this connection pool");
        System.out.println("  host                           - host of the mongod/mongos server");
        System.out.println("  port                           - port of the mongod/mongos server");
        System.out.println("  maxSize                        - max # of connections allowed");
        System.out.println("  total                          - # of connections allocated");
        System.out.println("  inUse                          - # of connections in use");
        System.out.println("  inUseConnections               - list of all in use connections");
        System.out.println("  inUseConnections.namespace     - namespace on which connection is operating");
        System.out.println("  inUseConnections.opCode        - operation connection is executing");
        System.out.println("  inUseConnections.query         - query the connection is executing (for query/update/remove)");
        System.out.println("  inUseConnections.numDocuments  - # of documents in the message (mostly relevant for batch inserts)");
        System.out.println("  inUseConnections.threadName    - name of thread on which connection is executing");
        System.out.println("  inUseConnections.durationMS    - duration that the operation has been executing so far");
        System.out.println("  inUseConnections.localPort     - local port of the connection");
    }
    
    private void print(final PrintWriter pw) throws JMException, IOException {
        final Set<ObjectName> beanSet = this.mBeanConnection.queryNames(new ObjectName("com.mongodb:type=ConnectionPool,*"), null);
        pw.println("{ pools : [");
        int i = 0;
        for (final ObjectName objectName : beanSet) {
            pw.print("   { ");
            this.printAttribute("ObjectName", objectName.toString(), pw);
            pw.println();
            pw.print("     ");
            this.printAttribute("Host", objectName, pw);
            this.printAttribute("Port", objectName, pw);
            this.printAttribute("MaxSize", objectName, pw);
            this.printStatistics(pw, objectName);
            pw.println("   }" + ((i == beanSet.size() - 1) ? "" : ","));
            ++i;
        }
        pw.println("  ]");
        pw.println("}");
    }
    
    private void printStatistics(final PrintWriter pw, final ObjectName objectName) throws InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        final String key = "Statistics";
        final CompositeData statistics = (CompositeData)this.mBeanConnection.getAttribute(objectName, key);
        this.printSimpleStatistics(pw, statistics);
        this.printInUseConnections(statistics, pw);
    }
    
    private void printSimpleStatistics(final PrintWriter pw, final CompositeData statistics) throws InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        this.printCompositeDataAttribute("total", statistics, pw);
        this.printCompositeDataAttribute("inUse", statistics, pw);
        pw.println();
    }
    
    private void printInUseConnections(final CompositeData statistics, final PrintWriter pw) throws InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        final String key = "inUseConnections";
        final CompositeData[] compositeDataArray = (CompositeData[])statistics.get(key);
        pw.println("     " + this.getKeyString(key) + ": [");
        for (int i = 0; i < compositeDataArray.length; ++i) {
            final CompositeData compositeData = compositeDataArray[i];
            pw.print("      { ");
            this.printCompositeDataAttribute("namespace", compositeData, pw);
            this.printCompositeDataAttribute("opCode", compositeData, pw);
            this.printCompositeDataAttribute("query", compositeData, pw, StringType.JSON);
            this.printCompositeDataAttribute("numDocuments", compositeData, pw);
            this.printCompositeDataAttribute("threadName", compositeData, pw);
            this.printCompositeDataAttribute("durationMS", compositeData, pw);
            this.printCompositeDataAttribute("localPort", compositeData, pw, Position.LAST);
            pw.println(" }" + ((i == compositeDataArray.length - 1) ? "" : ", "));
        }
        pw.println("     ]");
    }
    
    private void printCompositeDataAttribute(final String key, final CompositeData compositeData, final PrintWriter pw) {
        this.printCompositeDataAttribute(key, compositeData, pw, Position.REGULAR);
    }
    
    private void printCompositeDataAttribute(final String key, final CompositeData compositeData, final PrintWriter pw, final Position position) {
        this.printCompositeDataAttribute(key, compositeData, pw, position, StringType.REGULAR);
    }
    
    private void printCompositeDataAttribute(final String key, final CompositeData compositeData, final PrintWriter pw, final StringType stringType) {
        this.printCompositeDataAttribute(key, compositeData, pw, Position.REGULAR, stringType);
    }
    
    private void printCompositeDataAttribute(final String key, final CompositeData compositeData, final PrintWriter pw, final Position position, final StringType stringType) {
        this.printAttribute(key, compositeData.get(key), pw, position, stringType);
    }
    
    private void printAttribute(final String key, final ObjectName objectName, final PrintWriter pw) throws InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        this.printAttribute(key, this.mBeanConnection.getAttribute(objectName, key), pw);
    }
    
    private void printAttribute(final String key, final Object value, final PrintWriter pw) {
        this.printAttribute(key, value, pw, Position.REGULAR, StringType.REGULAR);
    }
    
    private void printAttribute(final String key, final Object value, final PrintWriter pw, final Position position, final StringType stringType) {
        if (value != null) {
            pw.print(this.getKeyString(key) + ": " + this.getValueString(value, stringType) + ((position == Position.LAST) ? "" : ", "));
        }
    }
    
    private String getKeyString(final String key) {
        return Character.toLowerCase(key.charAt(0)) + key.substring(1);
    }
    
    private String getValueString(final Object value, final StringType stringType) {
        if (value instanceof String && stringType == StringType.REGULAR) {
            return "'" + value + "'";
        }
        return value.toString();
    }
    
    enum StringType
    {
        REGULAR, 
        JSON;
    }
    
    enum Position
    {
        REGULAR, 
        LAST;
    }
}
