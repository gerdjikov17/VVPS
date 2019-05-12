
import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings.AlgoPrefixSpan_with_Strings;
import ca.pfv.spmf.input.sequence_database_list_strings.SequenceDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FileUpload {
    // UI Components
    private JButton ChooseButton;
    private JButton RunButton;
    private JLabel FileNameLabel;
    private JPanel MainPanel;
    private JTextField MinsupField;

    private String FilePath;

    public FileUpload() {
        ChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, MainPanel);

                if (parentFrame != null) {
                    FileDialog fileDialog = new java.awt.FileDialog(parentFrame, "Choose a file:", FileDialog.LOAD);
                    fileDialog.setFile("*.csv");
                    fileDialog.setVisible(true);

                    String selectedFile = fileDialog.getFile();
                    if (selectedFile != null) {
                        FilePath = fileDialog.getDirectory() + selectedFile;
                        FileNameLabel.setText(selectedFile);
                        RunButton.setEnabled(true);
                    }
                }
            }
        });
        RunButton.addActionListener(e -> {
            File parsed = null;
            try {
                parsed = convertFileAsSequence(getFile());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            if (parsed != null) {
                try {
                    processFile(parsed);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

    private File convertFileAsSequence(File file) throws FileNotFoundException {
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

    private File getFile() {
        return new File(FilePath);
    }

    private void processFile(File file) throws IOException {
        SequenceDatabase database = new SequenceDatabase();
        database.loadFile(file.getPath());

        AlgoPrefixSpan_with_Strings algo = new AlgoPrefixSpan_with_Strings();

        double minsupD = Double.parseDouble(MinsupField.getText());
        int minsup = (int) Math.ceil((minsupD * database.size())); // we use a minimum support of 2 sequences.
        // execute the algorithm\System.out.println("Finished");
        System.out.println("START");
        algo.runAlgorithm(database, ".//output.txt", minsup);
        System.out.println("Finished");
        algo.printStatistics(database.size());
        Desktop.getDesktop().open(new File(".//output.txt"));
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Log file upload");
        frame.setContentPane(new FileUpload().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showFileUpload(JFrame frame) {
        frame.setContentPane(new FileUpload().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(400, 150));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}