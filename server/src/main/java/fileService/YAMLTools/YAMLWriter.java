package fileService.YAMLTools;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fileService.FileService;

/**
 * A class that implements the save command, it saves the collection to a file in the specified path.
 *
 * @author Dmitrii Chebanenko
 */

public class YAMLWriter {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Method that saves collection to the specified file.
     *
     * @param object - object to save to the file
     * @throws IOException if the input of the object parameter fails
     */

    public void writeYAML(Object object, File file) throws IOException {
        if (!file.exists()) {
            new FileService().createFile(file);
        }

        this.mapper.writeValue(file, object);
    }

}