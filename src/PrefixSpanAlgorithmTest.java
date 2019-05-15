import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PrefixSpanAlgorithmTest {
    private File testFile;
    @BeforeEach
    void setUp() {
        testFile = new File(".//logs_UTF8.csv");
        try {
            testFile = FileConverter.convertFileAsSequence(testFile);
        } catch (FileNotFoundException e) {
            assert false;
            e.printStackTrace();
        }
        assertNotNull(testFile);
    }

    @Test
    void prefixSpan() {
        assertNotNull(testFile);
        if (!testFile.exists()) return;
        File outputFile = null;
        try {
            outputFile = PrefixSpanAlgorithm.prefixSpan(testFile, 0.5);
        } catch (IOException e) {
            assert false;
            e.printStackTrace();
        }
        assertNotNull(outputFile);
    }
}