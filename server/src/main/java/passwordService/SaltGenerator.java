package passwordService;

import java.security.SecureRandom;

public class SaltGenerator {

    public static byte[] generateSalt() {
        byte[] salt = new byte[PasswordUtils.INSTANCE.getSaltLength()];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

}
