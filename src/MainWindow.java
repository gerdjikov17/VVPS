import java.io.IOException;
import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings.AlgoPrefixSpan_with_Strings;
import ca.pfv.spmf.input.sequence_database_list_strings.SequenceDatabase;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.prefs.Preferences;

public class MainWindow implements LoginDelegate {
    private static final String LAST_USED_FOLDER = "lastUsedFolder";
    private JPanel MainPanel;
    private JLabel helloLabel;
    private JButton chooseFileButton;
    private JButton convertButton;

    public MainWindow() {
        chooseFileButton.addActionListener(e -> chooseFileAction());
        convertButton.addActionListener(e -> convertFileAction());
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        JFrame frame = new JFrame("PTS");
        frame.setContentPane(mainWindow.MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(400, 400));

//        LoginForm lgf = new LoginForm(mainWindow);
//        lgf.setVisible(true);
//        lgf.pack();
//        lgf.setLocationRelativeTo(null);
//        lgf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void didLogSuccessful(LoginForm loginForm) {
        loginForm.dispose();
        helloLabel.setText("You signed in successfully!");
        chooseFileButton.setEnabled(true);
    }

    private File chooseFile() {
        Preferences prefs = Preferences.userRoot().node(getClass().getName());
        JFileChooser chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
                new File(".").getAbsolutePath()));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV and TXT files", "txt", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            prefs.put(LAST_USED_FOLDER, chooser.getSelectedFile().getParent());
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            return chooser.getSelectedFile();
        }
        else {
            return null;
        }
    }

    private void convertFile(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String newLine = line.replaceAll(",", " -1 ");
                newLine = newLine + " -1 -2\n";
                newLine = newLine.replaceAll("  "," ");
                newLine = newLine.replaceAll("   "," ");
                resultStringBuilder.append(newLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(".//testOutput.txt");
            fw.write(resultStringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");

    }

    private void convertFileAsSequence(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        StringBuilder resultStringBuilder = new StringBuilder();
        boolean skipFirst = false;
        boolean skipLast = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            String lastIP = "";
            String lastTime = "";
            while ((line = br.readLine()) != null) {
//                int index = line.lastIndexOf(",") + 1;
//                String ip = line.substring(index);
//                if (!ip.equals(lastIP)) {
//                    resultStringBuilder.append("-2 ");
//                }
//                lastIP = ip;
//
//                //drop the date
//                int lastIndexOfQuotes = line.lastIndexOf("\"");
//                line = line.substring(lastIndexOfQuotes + 1);
//                line = line.replaceAll(",", " ");
//                line = line + " -1\n";
                if(skipFirst) {
                    LineModel lineModel = new LineModel(line);
                    if (!lineModel.time.equals(lastTime) && skipLast) {
                        resultStringBuilder.append("-1 ");
                    }

                    if (!lineModel.ip.equals(lastIP) && skipLast) {
                        resultStringBuilder.append("-2\n");
                    }

                    String parsed = lineModel.getTrimmedLine();
                    if (!(parsed.isEmpty() || parsed.equals(" "))) {
                        parsed = parsed + " ";
                        resultStringBuilder.append(parsed);

                        lastIP = lineModel.ip;
                        lastTime = lineModel.time;
                        skipLast = true;
                    } else {
                        skipLast = false;
                    }

                }
                else {
                    skipFirst = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(".//testOutput.txt");
            fw.write(resultStringBuilder.append(" -2").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");

    }

    private void chooseFileAction() {
        File chosenFile = this.chooseFile();
        if (chosenFile != null) {
            try {
                processFile(chosenFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertFileAction() {
        File chosenFile = this.chooseFile();
        if (chosenFile != null) {
            try {
                convertFileAsSequence(chosenFile);
//                convertFile(chosenFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void processFile(File file) throws IOException {
        SequenceDatabase database = new SequenceDatabase();
        database.loadFile(file.getPath());

        AlgoPrefixSpan_with_Strings algo = new AlgoPrefixSpan_with_Strings();

        int minsup = (int) Math.ceil((0.4 * database.size())); // we use a minimum support of 2 sequences.
        // execute the algorithm\System.out.println("Finished");
        System.out.println("START");
        algo.runAlgorithm(database, ".//output.txt", minsup);
        System.out.println("Finished");
        algo.printStatistics(database.size());

    }
}
