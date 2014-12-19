package redis.clients.util;

import java.net.*;

public class JedisURIHelper
{
    public static String getPassword(final URI uri) {
        final String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            return userInfo.split(":", 2)[1];
        }
        return null;
    }
    
    public static Integer getDBIndex(final URI uri) {
        final String[] pathSplit = uri.getPath().split("/", 2);
        if (pathSplit.length <= 1) {
            return 0;
        }
        final String dbIndexStr = pathSplit[1];
        if (dbIndexStr.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(dbIndexStr);
    }
}
