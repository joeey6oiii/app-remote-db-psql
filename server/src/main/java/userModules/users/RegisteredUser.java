package userModules.users;

import userModules.users.data.RegisteredUserData;

import java.util.Objects;

public class RegisteredUser extends User {
    private Integer id;
    private final RegisteredUserData registeredUserData;

    public RegisteredUser(RegisteredUserData registeredUserData, User user) {
        super(user.getAddress(), user.getPort());
        this.registeredUserData = registeredUserData;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RegisteredUserData getRegisteredUserData() {
        return this.registeredUserData;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "id=" + id +
                ", registeredUserData=" + registeredUserData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegisteredUser that = (RegisteredUser) o;
        return Objects.equals(id, that.id) && Objects.equals(registeredUserData, that.registeredUserData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, registeredUserData);
    }
}