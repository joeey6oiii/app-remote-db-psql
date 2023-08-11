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

/**
 * A class to encrypt passwords with salt and pepper using MD2 encryption algorithm.
 */
public class MD2PasswordEncryptor implements PasswordEncryptor {
    private static final Logger logger = LogManager.getLogger("logger.MD2PasswordEncryptor");
    private final SecureRandom random;
    private final int SALT_LENGTH;
    private final String PEPPER_FILE;
    private final byte[] pepper;

    {
        random = new SecureRandom();
        SALT_LENGTH = 16;
        PEPPER_FILE = "pepper.txt";
        pepper = readPepperFromFile().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Encrypts password using MD2 encryption algorithm.
     *
     * @param password char array of specified symbols
     * @return an object with information about salt and hashed password
     * @throws NoSuchAlgorithmException thrown when the MD2 algorithm is requested but is not available in the environment
     * @throws NoSuchProviderException thrown when the bouncy castle provider is requested but is not available in the environment
     */
    public EncryptedPassword encryptPassword(char[] password) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        byte[] salt = this.generateSalt();

        byte[] seasonedPasswd = ByteArrayUtils.concatByteArrays(salt,
                ByteArrayUtils.charArrayToByteArray(password), this.generatePepper());

        MessageDigest md = MessageDigest.getInstance("MD2", "BC");
        byte[] hashedBytes = md.digest(seasonedPasswd);

        return new EncryptedPassword(hashedBytes, salt);
    }

    /**
     * Generates a new byte array with random bytes using {@link SecureRandom#nextBytes(byte[])}.
     *
     * @return byte array with randomly generated bytes
     */
    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    @Override
    public byte[] generatePepper() {
        return this.pepper;
    }

    /**
     * Reads the pepper file from the server resources.
     *
     * @return string with symbols from the file in the server resources or default string with symbols if an error
     * occurred while reading pepper file
     */
    private String readPepperFromFile() {
        String pepper = null;

        try {
            InputStream inputStream = getClass().getResourceAsStream(PEPPER_FILE);

            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append(System.lineSeparator());
                    }

                    pepper = Arrays.toString(stringBuilder.toString().split(System.lineSeparator()));
                }
            } else {
                logger.error("Pepper file not found");
            }
        } catch (IOException e) {
            logger.error("Failed to read pepper file: {}", e.getMessage());
        } catch (NullPointerException e) {
            logger.error("Received null while accessing resources: {}", e.getMessage());
        }

        return pepper;
    }
}