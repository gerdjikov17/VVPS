import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FileConverterTest {



    @Test
    void convertFileAsSequence() {
        File testFile = new File(".//logs_UTF8.csv");
        File outputFile = null;
        try {
            outputFile = FileConverter.convertFileAsSequence(testFile);
        } catch (FileNotFoundException e) {
            assert false;
            e.printStackTrace();
        }
        assertNotNull(outputFile);
    }
}