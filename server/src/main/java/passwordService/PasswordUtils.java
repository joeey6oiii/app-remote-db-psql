package passwordService;

import fileService.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Arrays;

public enum PasswordUtils {
    INSTANCE;

    private static final Logger logger = LogManager.getLogger("logger.PasswordUtils");
    private static final SecureRandom random = new SecureRandom();
    private final int SALT_LENGTH = 16;
    private static final String PEPPER_FILE = "pepper.txt";

    public int getSaltLength() {
        return SALT_LENGTH;
    }

    public byte[] generateSalt() {
        byte[] salt = new byte[PasswordUtils.INSTANCE.getSaltLength()];
        random.nextBytes(salt);
        return salt;
    }

    public String generatePepper() {
        String pepper = "Black pepper (Piper nigrum) is a flowering vine in the family Piperaceae, cultivated" +
                " for its fruit (the peppercorn), which is usually dried and used as a spice and seasoning";

        try {
            InputStream inputStream = FileService.getResourceAsStream(PEPPER_FILE);
            pepper = Arrays.toString(new FileService().readLines(inputStream));

        } catch (IOException e) {
            logger.error("Failed to read pepper file: {}", e.getMessage());
            logger.info("Using default pepper");
        }

        return pepper;
    }

}
