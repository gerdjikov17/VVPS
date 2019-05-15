import java.io.*;

public class FileConverter {
    public static File convertFileAsSequence(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        StringBuilder resultStringBuilder = new StringBuilder();
        boolean skipFirst = false;
        boolean skipLast = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            String lastIP = "";
            String lastTime = "";
            while ((line = br.readLine()) != null) {
                if (skipFirst) {
                    LineModel lineModel = new LineModel(line);
                    if (lineModel.getIp() == null) {
                        System.out.println(line);
                    }
                    if (!lineModel.getTime().equals(lastTime) && skipLast) {
                        resultStringBuilder.append("-1 ");
                    }

                    if (!lineModel.getIp().equals(lastIP) && skipLast) {
                        resultStringBuilder.append("-2\n");
                    }

                    String parsed = lineModel.getTrimmedLine();
                    if (!(parsed.isEmpty() || parsed.equals(" "))) {
                        parsed = parsed + " ";
                        resultStringBuilder.append(parsed);

                        lastIP = lineModel.getIp();
                        lastTime = lineModel.getTime();
                        skipLast = true;
                    } else {
                        skipLast = false;
                    }

                } else {
                    skipFirst = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(".//parsedCSV.txt");
            fw.write(resultStringBuilder.append(" -2").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");
        return new File(".//parsedCSV.txt");
    }
}
