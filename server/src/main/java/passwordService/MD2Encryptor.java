package passwordService;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class MD2Encryptor {

    public static byte[] encryptPassword(byte[] salt, char[] password) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        String passwordString = new String(password);

        String saltedPassword = byteArrayToHexString(salt) + passwordString;

        MessageDigest md = MessageDigest.getInstance("MD2", "BC");
        byte[] hashedBytes = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

        return hashedBytes;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
