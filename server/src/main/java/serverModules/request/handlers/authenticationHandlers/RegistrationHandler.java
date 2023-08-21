package serverModules.request.handlers.authenticationHandlers;

import databaseModule.repository.RegisteredUserRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.RegistrationRequest;
import response.responses.RegistrationResponse;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.ClientRequestInfo;
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
import userModules.users.data.RegisteredUserData;
import userModules.users.utils.UserUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RegistrationHandler implements RequestHandler {
    private static final Logger logger = LogManager.getLogger("logger.RegistrationHandler");
    private final ResponseSender responseSender;

    public RegistrationHandler(ConnectionModule connectionModule) {
        this.responseSender = new ChunkedResponseSender(connectionModule);
    }

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
            } else if (login.length() < UserUtils.INSTANCE.getMinLoginLengthValue()) {
                response = "Unable to register your account. Login must be at least "
                        + UserUtils.INSTANCE.getMinLoginLengthValue() + " characters long";
            } else if (request.getAuthenticationData().getPassword().length < UserUtils.INSTANCE.getMinPasswdLengthValue()) {
                response = "Unable to register your account. Password must be at least "
                        + UserUtils.INSTANCE.getMinPasswdLengthValue() + " characters long";
            } else {
                RegisteredUser registeredUser = new RegisteredUser(new RegisteredUserData(login,
                        new MD2PasswordEncryptor().encryptPassword(request.getAuthenticationData().getPassword())),
                        info.getRequesterUser());

                if (userRepository.insert(registeredUser) != null) {
                    isSuccess = true;
                    token = tokenManager.generateToken();
                    response = "Your account has been successfully registered";

                    Session session = new Session(LocalDateTime.now(), UserUtils.INSTANCE.getSessionDurationInMinutes());
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(registeredUser, session);
                    if (!AuthenticatedUserRegistry.getInstance().addAuthenticatedUser(token, authenticatedUser)) {
                        response = "Server registered your account, but failed to authorize you. Please, try to authorize later";
                    }
                } else {
                    response = "Unable to register your account. Please, try again later";
                }
            }
        } catch (IOException | SQLException | NoSuchAlgorithmException | NoSuchProviderException | NullPointerException e) {
            response = "Something went wrong. Could not register your account. Please, try again later";
            logger.error("Error registering user", e);
        }

        responseSender.sendResponse(info.getRequesterUser(), new RegistrationResponse(isSuccess, token, response));
    }
}