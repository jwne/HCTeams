package net.frozenorb.foxtrot;

import net.frozenorb.foxtrot.command.*;
import java.io.*;
import java.util.jar.*;
import java.security.*;
import java.net.*;
import java.util.*;

public class CommandRegistrar
{
    public void loadCommandsFromPackage(final String packageName) {
        for (final Class<?> clazz : getClassesInPackage(packageName)) {
            CommandHandler.registerClass(clazz);
        }
    }
    
    public static ArrayList<Class<?>> getClassesInPackage(final String pkgname) {
        final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        final CodeSource codeSource = buttplug.fdsjfhkdsjfdsjhk().getClass().getProtectionDomain().getCodeSource();
        final URL resource = codeSource.getLocation();
        final String relPath = pkgname.replace('.', '/');
        final String resPath = resource.getPath().replace("%20", " ");
        final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jFile;
        try {
            jFile = new JarFile(jarPath);
        }
        catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        final Enumeration<JarEntry> entries = jFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> c = null;
                try {
                    c = Class.forName(className);
                }
                catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
                if (c == null) {
                    continue;
                }
                classes.add(c);
            }
        }
        try {
            jFile.close();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        return classes;
    }
    
    public void register() {
        this.loadCommandsFromPackage("net.frozenorb.foxtrot.command");
        this.loadCommandsFromPackage("net.frozenorb.foxtrot.koth");
        this.loadCommandsFromPackage("net.frozenorb.foxtrot.server");
        this.loadCommandsFromPackage("net.frozenorb.foxtrot.team");
        this.loadCommandsFromPackage("net.frozenorb.foxtrot.citadel");
        this.loadCommandsFromPackage("net.frozenorb.foxtrot.imagemessage");
    }
}
