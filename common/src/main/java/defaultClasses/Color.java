package defaultClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class whose constant is used in another class.
 * <p>
 * Contains getter and other methods.
 */

public enum Color implements isBuildable, Serializable {
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

    private final String colorName;
    private final static Map<String, Color> colors = Arrays.stream(Color.values()).collect(Collectors.toMap(k->k.colorName, v->v));

    /**
     * Private constructor for naming the constants.
     *
     * @param colorName the name of the color
     */

    Color(String colorName){
        this.colorName = colorName;
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

    public static Color getColorByName(String colorName){
        return colors.get(colorName);
    }

}