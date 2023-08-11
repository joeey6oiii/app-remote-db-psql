package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class with some useful constants and methods.
 */
public enum Color implements Buildable, Serializable {
    RED("red"),
    ORANGE("orange"),
    BROWN("brown"),
    GRAY("gray"),
    GREEN("green"),
    BLACK("black"),
    YELLOW("yellow"),
    BLUE("blue"),
    PURPLE("purple"),
    WHITE("white");

    private final String label;
    private final static Map<String, Color> colors = Arrays.stream(Color.values()).collect(Collectors.toMap(k -> k.label, v -> v));

    /**
     * Private constructor for naming the constants.
     *
     * @param label the name of the color
     */
    Color(String label){
        this.label = label;
    }

    /**
     * @return list of the available colors
     */
    public static ArrayList<Color> listValues(){
        return new ArrayList<>(Arrays.asList(Color.values()));
    }

    /**
     * @param colorName the name of the color
     * @return {@link Color} if a list contains the specified name of the color, otherwise null
     */
    public static Color getColorFromLabel(String colorName){
        return colors.get(colorName);
    }

    public String getLabel() {
        return label;
    }
}