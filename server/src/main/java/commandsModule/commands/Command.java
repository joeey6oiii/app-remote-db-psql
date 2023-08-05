package commandsModule.commands;

import java.lang.annotation.*;

/**
 * Custom annotation to mark command classes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {}