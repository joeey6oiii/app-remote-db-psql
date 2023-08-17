package userModules.sessionService;

public interface SessionManager {

    void startSessionExpirationCheck(long interval);

    void stopSessionExpirationCheck();
}
