import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings.AlgoPrefixSpan_with_Strings;
import ca.pfv.spmf.input.sequence_database_list_strings.SequenceDatabase;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PrefixSpanAlgorithm {
    public static File prefixSpan(File file, double minsupD) throws IOException {
        String outputFilePath = ".//output.txt";
        SequenceDatabase database = new SequenceDatabase();
        database.loadFile(file.getPath());

        AlgoPrefixSpan_with_Strings algo = new AlgoPrefixSpan_with_Strings();

        int minsup = (int) Math.ceil((minsupD * database.size())); // we use a minimum support of 2 sequences.
        algo.runAlgorithm(database, outputFilePath, minsup);
        algo.printStatistics(database.size());
        File f = new File(outputFilePath);
        return f;
    }
}
