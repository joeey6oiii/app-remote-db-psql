package userModules.tokenService;

import token.StringToken;
import utils.UserUtils;

import java.security.SecureRandom;

public class StringTokenManager implements TokenManager<String> {
    private final SecureRandom random;
    private final int TOKEN_LENGTH;
    private final String CHARACTERS;

    {
        random = new SecureRandom();
        TOKEN_LENGTH = UserUtils.INSTANCE.getTokenLengthValue();
        CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-=_+[]{}|;:,.<>?";
    }

    public StringToken generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = this.random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            token.append(randomChar);
        }

        return new StringToken(token.toString());
    }
}