package utils;

public enum UserUtils {
    INSTANCE;

    private final int sessionDurationInMinutes = 5;
    private final int minLoginLength = 4;
    private final int minPasswordLength = 8;
    private final int commandHistoryMaxSize = 9;
    private final int tokenLength = 10;

    public int getSessionDurationInMinutes() {
        return sessionDurationInMinutes;
    }

    public int getMinLoginLengthValue() {
        return minLoginLength;
    }

    public int getMinPasswdLengthValue() {
        return minPasswordLength;
    }

    public int getCommandHistoryMaxSizeValue() {
        return commandHistoryMaxSize;
    }

    public int getTokenLengthValue() {
        return tokenLength;
    }
}
