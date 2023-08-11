package objectBuilder;

import model.Buildable;

/**
 * FunctionalInterface that allows a class to be used as an objects' builder.
 */
@FunctionalInterface
public interface ObjectBuilder {

    /**
     * A method to build object.
     */
    Buildable buildObject();
}