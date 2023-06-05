package objectBuilder;

import defaultClasses.isBuildable;

/**
 * FunctionalInterface that allows a class to create a defaultClass.
 */

@FunctionalInterface
public interface BuildAble {

    /**
     * A method to build object.
     */

    isBuildable buildObject();
}
