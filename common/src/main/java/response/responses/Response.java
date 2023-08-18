package response.responses;

import response.visitor.ResponseVisitor;

/**
 * An interface for all response-implementers.
 */
public interface Response {

    boolean accept(ResponseVisitor visitor);
}
