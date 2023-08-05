package userModules.passwordService;

public class EncryptedPassword {
    private final byte[] hashedPassword;
    private final byte[] salt;

    public EncryptedPassword(byte[] hashedPassword, byte[] salt) {
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public byte[] getSalt() {
        return salt;
    }
}
