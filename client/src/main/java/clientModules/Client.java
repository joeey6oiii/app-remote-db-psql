package clientModules;

public class Client {
    private static String login;
    private static char[] passwd;

    public static void setLogin(String login) {
        Client.login = login;
    }

    public static void setPassword(char[] passwd) {
        Client.passwd = passwd;
    }

    public static String getLogin() {
        return login;
    }

    public static char[] getPassword() {
        return passwd;
    }

}
