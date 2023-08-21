package serverModules.request.handlers.authenticationHandlers;

import databaseModule.repository.RegisteredUserRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.AuthorizationRequest;
import response.responses.AuthorizationResponse;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.RequestInfo;
import serverModules.request.handlers.RequestHandler;
import serverModules.response.sender.ChunkedResponseSender;
import serverModules.response.sender.ResponseSender;
import token.Token;
import userModules.sessionService.AuthenticatedUserRegistry;
import userModules.passwordService.MD2PasswordEncryptor;
import userModules.sessionService.Session;
import userModules.tokenService.StringTokenManager;
import userModules.tokenService.TokenManager;
import userModules.users.AuthenticatedUser;
import userModules.users.RegisteredUser;
import userModules.users.utils.UserUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthorizationHandler implements RequestHandler {
    private static final Logger logger = LogManager.getLogger("logger.AuthorizationHandler");
    private final ResponseSender responseSender;

    public AuthorizationHandler(ConnectionModule connectionModule) {
        this.responseSender = new ChunkedResponseSender(connectionModule);
    }

    @Override
    public void handleRequest(RequestInfo info) {
        boolean isSuccess = false;
        Token<?> token = null;
        TokenManager<?> tokenManager = new StringTokenManager();
        String response;

        AuthorizationRequest request = (AuthorizationRequest) info.getRequest();
        String login = request.getAuthenticationData().getLogin();

        try (RegisteredUserRepositoryImpl userRepository = new RegisteredUserRepositoryImpl()) {
            RegisteredUser registeredUser = userRepository.read(login);
            if (registeredUser == null) {
                response = "Unable to authorize you. User with this login does not exist";
            } else {
                if (!(new MD2PasswordEncryptor().checkPassword(request.getAuthenticationData(), registeredUser.getRegisteredUserData()))) {
                    response = "Unable to authorize you. Incorrect password";
                } else {
                    Session session = new Session(LocalDateTime.now(), UserUtils.INSTANCE.getSessionDurationInMinutes());
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(registeredUser, session);

                    AuthenticatedUserRegistry userRegistry = AuthenticatedUserRegistry.getInstance();

                    if (userRegistry.checkUserExistence(authenticatedUser)) {
                        response = "You are already logged in";
                    } else {
                        token = tokenManager.generateToken();

                        if (!userRegistry.addAuthenticatedUser(token, authenticatedUser)) {
                            throw new IOException("Unable to add authenticated user to the collection of authenticated users");
                        }

                        isSuccess = true;
                        response = "You are successfully logged in";
                    }
                }
            }
        } catch (IOException | SQLException | NoSuchAlgorithmException | NoSuchProviderException | NullPointerException e) {
            response = "Something went wrong. Could not authorize you. Please, try again later";
            logger.error("Error authorizing user", e);
        }

        responseSender.sendResponse(info.getRequesterUser(), new AuthorizationResponse(isSuccess, token, response));
    }
}