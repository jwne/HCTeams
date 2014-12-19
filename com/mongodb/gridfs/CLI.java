package com.mongodb.gridfs;

import com.mongodb.*;
import java.io.*;
import java.security.*;
import com.mongodb.util.*;
import java.util.*;

public class CLI
{
    private static String db;
    private static String uri;
    private static Mongo _mongo;
    private static GridFS _gridfs;
    
    private static void printUsage() {
        System.out.println("Usage : [--db database] action");
        System.out.println("  where  action is one of:");
        System.out.println("      list                      : lists all files in the store");
        System.out.println("      put filename              : puts the file filename into the store");
        System.out.println("      get filename1 filename2   : gets filename1 from store and sends to filename2");
        System.out.println("      md5 filename              : does an md5 hash on a file in the db (for testing)");
    }
    
    private static Mongo getMongo() throws Exception {
        if (CLI._mongo == null) {
            CLI._mongo = new MongoClient(new MongoClientURI(CLI.uri));
        }
        return CLI._mongo;
    }
    
    private static GridFS getGridFS() throws Exception {
        if (CLI._gridfs == null) {
            CLI._gridfs = new GridFS(getMongo().getDB(CLI.db));
        }
        return CLI._gridfs;
    }
    
    public static void main(final String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            return;
        }
        for (int i = 0; i < args.length; ++i) {
            final String s = args[i];
            if (s.equals("--db")) {
                CLI.db = args[i + 1];
                ++i;
            }
            else if (s.equals("--host")) {
                CLI.uri = "mongodb://" + args[i + 1];
                ++i;
            }
            else if (s.equals("--uri")) {
                CLI.uri = args[i + 1];
                ++i;
            }
            else {
                if (s.equals("help")) {
                    printUsage();
                    return;
                }
                if (s.equals("list")) {
                    final GridFS fs = getGridFS();
                    System.out.printf("%-60s %-10s\n", "Filename", "Length");
                    for (final DBObject o : fs.getFileList()) {
                        System.out.printf("%-60s %-10d\n", o.get("filename"), ((Number)o.get("length")).longValue());
                    }
                    return;
                }
                if (s.equals("get")) {
                    final GridFS fs = getGridFS();
                    final String fn = args[i + 1];
                    final GridFSDBFile f = fs.findOne(fn);
                    if (f == null) {
                        System.err.println("can't find file: " + fn);
                        return;
                    }
                    f.writeTo(f.getFilename());
                    return;
                }
                else {
                    if (s.equals("put")) {
                        final GridFS fs = getGridFS();
                        final String fn = args[i + 1];
                        final GridFSInputFile f2 = fs.createFile(new File(fn));
                        f2.save();
                        f2.validate();
                        return;
                    }
                    if (!s.equals("md5")) {
                        System.err.println("unknown option: " + s);
                        return;
                    }
                    final GridFS fs = getGridFS();
                    final String fn = args[i + 1];
                    final GridFSDBFile f = fs.findOne(fn);
                    if (f == null) {
                        System.err.println("can't find file: " + fn);
                        return;
                    }
                    final MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.reset();
                    final DigestInputStream is = new DigestInputStream(f.getInputStream(), md5);
                    int read = 0;
                    while (is.read() >= 0) {
                        ++read;
                        final int r = is.read(new byte[17]);
                        if (r < 0) {
                            break;
                        }
                        read += r;
                    }
                    final byte[] digest = md5.digest();
                    System.out.println("length: " + read + " md5: " + Util.toHex(digest));
                    return;
                }
            }
        }
    }
    
    static {
        CLI.db = "test";
        CLI.uri = "mongodb://127.0.0.1";
        CLI._mongo = null;
    }
}
