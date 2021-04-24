package common;

import common.Config;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    public static String encryptString(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(Config.CRYPTO);
            byte[] digest = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            BigInteger num = new BigInteger(1, digest);
            StringBuilder hashedString = new StringBuilder(num.toString(16));
            while (hashedString.length() < 32)
                hashedString.insert(0, "0");
            return hashedString.toString();
        } catch (NoSuchAlgorithmException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}
