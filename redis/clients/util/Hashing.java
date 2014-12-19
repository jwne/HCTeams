package redis.clients.util;

import java.security.*;

public interface Hashing
{
    public static final Hashing MURMUR_HASH = new MurmurHash();
    public static final ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<MessageDigest>();
    public static final Hashing MD5 = new Hashing() {
        @Override
        public long hash(String key) {
            return this.hash(SafeEncoder.encode(key));
        }
        
        @Override
        public long hash(byte[] key) {
            MessageDigest md5;
            byte[] bKey;
            long res;
            try {
                if (Hashing$1.md5Holder.get() == null) {
                    Hashing$1.md5Holder.set(MessageDigest.getInstance("MD5"));
                }
            }
            catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("++++ no md5 algorythm found");
            }
            md5 = Hashing$1.md5Holder.get();
            md5.reset();
            md5.update(key);
            bKey = md5.digest();
            res = ((bKey[3] & 0xFF) << 24 | (bKey[2] & 0xFF) << 16 | (bKey[1] & 0xFF) << 8 | (bKey[0] & 0xFF));
            return res;
        }
    };
    
    long hash(String p0);
    
    long hash(byte[] p0);
}
