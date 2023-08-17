package utils;

public enum UserUtils {
    INSTANCE;

    public int getMinPasswdLengthValue() {
        return 8;
    }

    public int getCommandHistoryMaxSizeValue() {
        return 9;
    }

    public int getTokenLengthValue() {
        return 10;
    }
}
