package objectBuilder;

import defaultClasses.isBuildable;

/**
 * FunctionalInterface that allows a class to be used as an objects' builder.
 */
@FunctionalInterface
public interface BuildAble {

    /**
     * A method to build object.
     */
    isBuildable buildObject();
}