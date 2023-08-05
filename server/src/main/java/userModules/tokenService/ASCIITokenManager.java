package userModules.tokenService;

import java.security.SecureRandom;

public class ASCIITokenManager implements TokenManager {
    private static final SecureRandom random = new SecureRandom();
    private final int TOKEN_LENGTH = 10;
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-=_+[]{}|;:,.<>?";

    public String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            token.append(randomChar);
        }

        return token.toString();
    }
}
