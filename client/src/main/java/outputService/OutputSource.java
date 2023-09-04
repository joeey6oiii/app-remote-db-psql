package outputService;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class OutputSource {
    private static OutputStream output = System.out;

    public static void setSystemOutput() {
        output = System.out;
    }

    public static void setFileOutput(String path) throws FileNotFoundException {
        output = new FileOutputStream(path);
    }

    public static OutputStream getOutputStream() {
        return output;
    }
}
