package net.frozenorb.foxtrot.util;

import net.frozenorb.foxtrot.*;
import org.bukkit.command.*;
import java.io.*;

public class BackupUtils
{
    public static void fullBackup(final FoxCallback callback) {
        buttplug.fdsjfhkdsjfdsjhk().getServer().dispatchCommand((CommandSender)buttplug.fdsjfhkdsjfdsjhk().getServer().getConsoleSender(), "save-off");
        buttplug.fdsjfhkdsjfdsjhk().getServer().dispatchCommand((CommandSender)buttplug.fdsjfhkdsjfdsjhk().getServer().getConsoleSender(), "save-all");
        try {
            final Process proc = Runtime.getRuntime().exec("./fullBackup.sh");
            final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String aux = "";
            while ((aux = stdInput.readLine()) != null) {
                System.out.println(aux);
            }
            stdInput.close();
            proc.destroy();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        buttplug.fdsjfhkdsjfdsjhk().getServer().dispatchCommand((CommandSender)buttplug.fdsjfhkdsjfdsjhk().getServer().getConsoleSender(), "save-on");
        callback.call(null);
    }
}
