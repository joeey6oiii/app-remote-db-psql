package userModules.sessionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import token.Token;
import userModules.users.AuthenticatedUser;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthenticatedUserRegistrySessionManager implements SessionManager {
    private static final Logger logger = LogManager.getLogger("logger.AuthenticatedUserRegistrySessionManager");
    private final ScheduledExecutorService executorService;
    private final AuthenticatedUserRegistry userRegistry;

    public AuthenticatedUserRegistrySessionManager() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        userRegistry = AuthenticatedUserRegistry.getInstance();
    }

    public void startSessionExpirationCheck(long intervalMinutes) {
        executorService.scheduleAtFixedRate(() -> {
            logger.info("Started session expiration check");
            LocalDateTime currentTime = LocalDateTime.now();
            for (Map.Entry<Token<?>, AuthenticatedUser> entry : userRegistry.getEntrySet()) {
                Token<?> token = entry.getKey();
                AuthenticatedUser user = entry.getValue();
                Session userSession = user.getSession();

                if (userSession != null && userSession.isSessionExpired(currentTime)) {
                    userRegistry.removeAuthenticatedUser(token);
                }
            }
            logger.info("Ended session expiration check");
        }, 0, intervalMinutes, TimeUnit.MINUTES);
    }

    public void stopSessionExpirationCheck() {
        logger.info("Shutdown session expiration checker");
        executorService.shutdown();
    }
}
