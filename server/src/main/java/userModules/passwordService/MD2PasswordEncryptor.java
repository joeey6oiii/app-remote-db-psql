package userModules.passwordService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import utils.ByteArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

public class MD2PasswordEncryptor implements PasswordEncryptor {
    private static final Logger logger = LogManager.getLogger("logger.MD2PasswordEncryptor");
    private static final SecureRandom random = new SecureRandom();
    private final int SALT_LENGTH = 16;
    private static final String PEPPER_FILE = "pepper.txt";

    public EncryptedPassword encryptPassword(char[] password) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        byte[] salt = this.generateSalt();

        byte[] seasonedPasswd = ByteArrayUtils.concatByteArrays(salt,
                ByteArrayUtils.charArrayToByteArray(password), this.generatePepper().getBytes(StandardCharsets.UTF_8));

        MessageDigest md = MessageDigest.getInstance("MD2", "BC");
        byte[] hashedBytes = md.digest(seasonedPasswd);

        return new EncryptedPassword(hashedBytes, salt);
    }

    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public String generatePepper() {
        String pepper = "One Piece";

        try {
            InputStream inputStream = getClass().getResourceAsStream(PEPPER_FILE);

            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append(System.lineSeparator());
                }

                pepper = Arrays.toString(stringBuilder.toString().split(System.lineSeparator()));
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Failed to read pepper file: {}", e.getMessage());
            logger.info("Using default pepper");
        }

        return pepper;
    }
}
