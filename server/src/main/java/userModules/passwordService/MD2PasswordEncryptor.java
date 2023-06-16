package userModules.passwordService;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import utils.ByteArrayUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class MD2PasswordEncryptor implements PasswordEncryptor {

    public EncryptedPassword encryptPassword(char[] password) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        byte[] salt = PasswordUtils.INSTANCE.generateSalt();

        byte[] seasonedPasswd = ByteArrayUtils.concatByteArrays(salt, ByteArrayUtils.charArrayToByteArray(password),
                PasswordUtils.INSTANCE.generatePepper().getBytes(StandardCharsets.UTF_8));

        MessageDigest md = MessageDigest.getInstance("MD2", "BC");
        byte[] hashedBytes = md.digest(seasonedPasswd);

        return new EncryptedPassword(hashedBytes, salt);
    }

}
