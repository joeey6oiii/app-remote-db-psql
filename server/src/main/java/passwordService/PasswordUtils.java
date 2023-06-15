package passwordService;

public enum PasswordUtils {
    INSTANCE;

    private final int SALT_LENGTH = 16;

    public int getSaltLength() {
        return SALT_LENGTH;
    }

}

