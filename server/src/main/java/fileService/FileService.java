package fileService;

import fileService.YAMLTools.YAMLReader;
import fileService.YAMLTools.YAMLWriter;
import main.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * A class for working with a file.
 */

public class FileService {
    private static final Logger logger = LogManager.getLogger("logger.FileService");
    private static File workPathAsFile;
    private static boolean runningFromJar;

    static {
        try {
            workPathAsFile = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

            runningFromJar = Objects.requireNonNull(Server.class.getResource("/" + Server.class.
                    getName().replace('.', '/') + ".class")).toString().startsWith("jar:");
        } catch (URISyntaxException | NullPointerException e) {
            logger.error("Failed to find program file. Can not continue program execution");
            System.exit(-99);
        }
    }

    /**
     * A method that returns current program working path as a file.
     */

    @Deprecated
    public static File getWorkPathAsFile() {
        return workPathAsFile;
    }

    /**
     * A method that checks if the program is running from a jar file.
     *
     * @return true if program is running from a jar file, otherwise false
     */

    @Deprecated
    public static boolean isProgramRunningFromJar() {
        return runningFromJar;
    }

    /**
     * Reads the specified resource file as an InputStream.
     *
     * @param resourceName name of the resource file
     * @return InputStream of the resource file
     */

    public static InputStream getResourceAsStream(String resourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resourceName);
    }

    /**
     * Reads the specified file using {@link YAMLReader}.
     *
     * @param file file to read
     * @param type type of the objects to create
     * @param <T> arbitrary non-primitive data type
     * @return list with elements of type T or empty <code>ArrayList</code>
     * @throws IOException if failed during I/O operations
     */

    @Deprecated
    public <T> List<T> readObjectsFromFile(File file, Class<T> type) throws IOException {
        return new YAMLReader().read(file, type);
    }

    /**
     * Writes the specified objects to the specified file using {@link YAMLWriter}.
     *
     * @param file file to write to
     * @param obj object to write
     * @throws IOException if failed during I/O operations
     */

    @Deprecated
    public void writeObjectToFile(File file, Object obj) throws IOException {
        if (!file.exists()) {
            this.createFile(file);
        }
        new YAMLWriter().writeYAML(obj, file);
    }

    /**
     * The method reads the content of an InputStream and returns an array of strings,
     * where each string represents a line of the input stream.
     * The method assumes that the input stream uses UTF-8 encoding.
     *
     * @param inputStream The input stream from which to read the lines.
     * @return An array of strings, where each string represents a line of the input stream.
     * @throws IOException If an I/O error occurs while reading the input stream.
     */

    public String[] readLines(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
            return stringBuilder.toString().split(System.lineSeparator());
        }
    }

    /**
     * Creates a new file.
     *
     * @param file file to create
     * @throws IOException if failed during I/O operations
     */

    public void createFile(File file) throws IOException {
        if (!file.createNewFile()) {
            throw new IOException("Unable to create file " + file.getPath());
        }
    }

}