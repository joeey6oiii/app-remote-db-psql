package userModules.passwordService;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface PasswordEncryptor {

    EncryptedPassword encryptPassword(char[] password) throws NoSuchAlgorithmException, NoSuchProviderException;

    byte[] generateSalt();

    String generatePepper();
}
