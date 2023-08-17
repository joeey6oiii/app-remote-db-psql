package userModules.sessionService;

import java.time.LocalDateTime;

public class Session {
    private final LocalDateTime loginTime;
    private final int sessionDurationInMinutes;

    public Session(LocalDateTime loginTime, int sessionDurationInMinutes) {
        this.loginTime = loginTime;
        this.sessionDurationInMinutes = sessionDurationInMinutes;
    }

    public LocalDateTime getLoginTime() {
        return this.loginTime;
    }

    public int getSessionDurationInMinutes() {
        return this.sessionDurationInMinutes;
    }

    public boolean isSessionExpired(LocalDateTime currentTime) {
        LocalDateTime sessionEndTime = loginTime.plusMinutes(sessionDurationInMinutes);
        return currentTime.isAfter(sessionEndTime);
    }
}

