package fileService.YAMLTools;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class for reading YAML files.
 * <p>
 * YAMLReader uses {@link ObjectMapper} with {@link YAMLFactory} parameter to create objects from the YAML file. Uses custom
 * {@link DeserializationFeature} configuration: reads enums using <code>toString()</code>; accepts: empty array as null object,
 * single value as an array, empty string as a null object; doesn't fail on: unknown properties, ignored properties, invalid subtype,
 * missing creator properties, null creator properties and null for primitives.
 */

public class YAMLReader {
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            .findAndRegisterModules();

    /**
     * Reads the specified file. Creates object(s) of the specified class and adds them to the <code>List</code>.
     *
     * @param file the specified file
     * @param type the specified class whose objects will be created in the method
     * @param <T> arbitrary non-primitive data type
     * @return list of created elements of type T. Returns empty <code>ArrayList</code> if the size of the file is zero
     * or no objects were found in the file.
     */

    public <T> List<T> read(File file, Class<T> type) throws IOException {
        Class<T[]> arrayClass = null;
        try {
            arrayClass = (Class<T[]>) Class.forName("[L" + type.getName() + ";");
        } catch (ClassNotFoundException ignored) {}

        long fileSize = Files.size(file.toPath());
        if (fileSize == 0 || arrayClass == null) {
            return new ArrayList<>();
        }

        T[] objects;
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            objects = objectMapper.readValue(reader, arrayClass);
        }

        if (objects != null) {
            return Arrays.asList(objects);
        }
        return new ArrayList<>();
    }

}