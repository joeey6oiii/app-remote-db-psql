package response.visitor;

import response.responses.*;

public interface ResponseVisitor {

    boolean visit(ErrorResponse response);

    boolean visit(ClientCommandsResponse response);

    boolean visit(CommandExecutionResponse response);

    boolean visit(AuthorizationResponse response);

    boolean visit(RegistrationResponse response);
}
