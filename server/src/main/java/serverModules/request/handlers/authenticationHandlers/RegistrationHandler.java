package serverModules.request.handlers.authenticationHandlers;

import databaseModule.repository.RegisteredUserRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.RegistrationRequest;
import response.responses.RegistrationResponse;
import serverModules.request.data.ClientRequestInfo;
import serverModules.request.handlers.RequestHandler;
import serverModules.response.sender.ResponseSender;
import token.Token;
import userModules.AuthenticatedUserRegistry;
import userModules.passwordService.MD2PasswordEncryptor;
import userModules.sessionService.Session;
import userModules.tokenService.StringTokenManager;
import userModules.tokenService.TokenManager;
import userModules.users.AuthenticatedUser;
import userModules.users.RegisteredUser;
import userModules.users.data.RegisteredUserData;
import utils.UserUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RegistrationHandler implements RequestHandler {
    private static final Logger logger = LogManager.getLogger("logger.RegistrationHandler");

    @Override
    public void handleRequest(ClientRequestInfo info) {
        boolean isSuccess = false;
        Token<?> token = null;
        TokenManager<?> tokenManager = new StringTokenManager();
        String response;

        RegistrationRequest request = (RegistrationRequest) info.getRequest();
        String login = request.getAuthenticationData().getLogin();

        try (RegisteredUserRepositoryImpl userRepository = new RegisteredUserRepositoryImpl()) {
            if (userRepository.read(login) != null) {
                response = "Unable to register your account. User with this login already exists";
            } else if (request.getAuthenticationData().getPassword().length < UserUtils.INSTANCE.getMinPasswdLengthValue()) {
                response = "Unable to register your account. Password must be at least "
                        + UserUtils.INSTANCE.getMinPasswdLengthValue() + " characters long";
            } else {
                RegisteredUser registeredUser = new RegisteredUser(new RegisteredUserData(login,
                        new MD2PasswordEncryptor().encryptPassword(request.getAuthenticationData().getPassword())),
                        info.getRequestOrigin());

                if (userRepository.insert(registeredUser)) {
                    isSuccess = true;
                    token = tokenManager.generateToken();
                    response = "Your account has been successfully registered";

                    registeredUser.setId(userRepository.getElementId(registeredUser));

                    Session session = new Session(LocalDateTime.now(), UserUtils.INSTANCE.getSessionDurationInMinutes());
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(registeredUser, session);
                    AuthenticatedUserRegistry.getInstance().addAuthenticatedUser(token, authenticatedUser);
                } else {
                    response = "Unable to register your account. Please, try again later";
                }
            }
        } catch (IOException | SQLException | NoSuchAlgorithmException | NoSuchProviderException | NullPointerException e) {
            response = "Something went wrong. Could not register your account. Please, try again later";
            logger.error("Error registering user", e);
        }

        new ResponseSender().sendResponse(info.getConnectionModule(), info.getRequestOrigin(), new RegistrationResponse(isSuccess, token, response));
    }
}