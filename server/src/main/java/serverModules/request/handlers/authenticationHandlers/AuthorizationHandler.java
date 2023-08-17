package serverModules.request.handlers.authenticationHandlers;

import databaseModule.repository.RegisteredUserRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.RegistrationRequest;
import response.responses.AuthorizationResponse;
import response.responses.RegistrationResponse;
import serverModules.request.data.ClientRequestInfo;
import serverModules.request.handlers.RequestHandler;
import serverModules.response.sender.ResponseSender;
import token.Token;
import userModules.AuthenticatedUserRegistry;
import userModules.passwordService.MD2PasswordEncryptor;
import userModules.tokenService.StringTokenManager;
import userModules.tokenService.TokenManager;
import userModules.users.AuthenticatedUser;
import userModules.users.RegisteredUser;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;

public class AuthorizationHandler implements RequestHandler {
    private static final Logger logger = LogManager.getLogger("logger.AuthorizationHandler");

    @Override
    public void handleRequest(ClientRequestInfo info) {
        boolean isSuccess = false;
        Token<?> token = null;
        TokenManager<?> tokenManager = new StringTokenManager();
        String response;

        RegistrationRequest request = (RegistrationRequest) info.getRequest();
        String login = request.getAuthenticationData().getLogin();

        try (RegisteredUserRepositoryImpl userRepository = new RegisteredUserRepositoryImpl()) {
            RegisteredUser registeredUser = userRepository.read(login);
            if (registeredUser == null) {
                response = "Unable to authorize you. User with this login does not exist";
            } else {
                if (!(new MD2PasswordEncryptor().checkPassword(request.getAuthenticationData(), registeredUser.getRegisteredUserData()))) {
                    response = "Unable to authorize you. Incorrect password";
                } else {
                    isSuccess = true;
                    token = tokenManager.generateToken();
                    response = "You have been successfully authorized";

                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(registeredUser);
                    // todo session
                    AuthenticatedUserRegistry.getInstance().addAuthenticatedUser(token, authenticatedUser);
                }
            }
        } catch (IOException | SQLException | NoSuchAlgorithmException | NoSuchProviderException | NullPointerException e) {
            response = "Something went wrong. Could not authorize you. Please, try again later";
            logger.error("Error authorizing user", e);
        }

        new ResponseSender().sendResponse(info.getConnectionModule(), info.getRequestOrigin(), new AuthorizationResponse(isSuccess, token, response));
    }
}