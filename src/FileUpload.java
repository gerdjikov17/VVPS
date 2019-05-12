import ca.pfv.spmf.algorithms.frequentpatterns.dci_closed_optimized.AlgoDCI_Closed_Optimized;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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

                if (parentFrame != null)
                {
                    FileDialog fileDialog = new java.awt.FileDialog(parentFrame, "Choose a file:", FileDialog.LOAD);
                    fileDialog.setVisible(true);

                    String selectedFile = fileDialog.getFile();
                    if( selectedFile != null)
                    {
                        FilePath = fileDialog.getDirectory() + selectedFile;
                        FileNameLabel.setText(selectedFile);
                        RunButton.setEnabled(true);
                    }
                }
            }
        });
        RunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Reader reader = Files.newBufferedReader(Paths.get(FilePath));
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                            .withFirstRecordAsHeader()
                            .withIgnoreHeaderCase()
                            .withTrim());

                    // read log file
                    HashMap<String, Integer> eventToIndex = new HashMap<>();
                    HashMap<Integer, String> indexToEvent = new HashMap<>();
                    Queue<AlgorithmLine> algorithmLines = new LinkedList<>();

                    AlgorithmLine currentAlgoLine = new AlgorithmLine();
                    String lastIP = "";
                    Integer lastEventIndex = 1;

                    for (CSVRecord csvRecord : csvParser) {
                        LogFileRow logFileRow = new LogFileRow(csvRecord);
                        Integer eventIndex = eventToIndex.putIfAbsent(logFileRow.GetEventName(), lastEventIndex);

                        if(eventIndex == null){
                            eventIndex = lastEventIndex++;
                        }
                        indexToEvent.putIfAbsent(eventIndex, logFileRow.GetEventName());

                        String thisIP = logFileRow.GetIP();

                        if(!lastIP.isEmpty() && !currentAlgoLine.IsEmpty() && !lastIP.equals(logFileRow.GetIP())){
                            algorithmLines.add(currentAlgoLine);

                            currentAlgoLine = new AlgorithmLine();
                        }
                        else{
                            currentAlgoLine.Add(eventIndex);
                        }

                        lastIP = thisIP;
                    }
                    reader.close();

                    // write convertedLog
                    String convertedLog = ".//convertedLog.txt";
                    PrintWriter writer = new PrintWriter(convertedLog, "UTF-8");
                    for(AlgorithmLine line : algorithmLines){
                        writer.println(line);
                    }
                    writer.close();

                    AlgoDCI_Closed_Optimized algoDCIClosed = new AlgoDCI_Closed_Optimized();
                    // Set this variable to true to show the transaction identifiers where patterns appear in the output file
                   // algoDCIClosed.setShowTransactionIdentifiers(true);

                    // execute the algorithm
                    try {
                        int minsup = 2;
                        try {
                            minsup = Integer.parseInt(MinsupField.getText());
                        } catch (NumberFormatException numEx) {
                            MinsupField.setText(Integer.toString(2));
                        }

                        String convertedLogParsed = ".//convertedLogParsed.txt";
                        String output = ".//output.txt";
                        algoDCIClosed.runAlgorithm(convertedLog, convertedLogParsed, minsup);

                        PrintWriter finalWriter = new PrintWriter(output);
                        Files.lines(Paths.get(convertedLogParsed)).forEach(s ->
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            Boolean passedSup = false;

                            for (char ch : s.toCharArray()) {
                                if(ch == '#'){
                                    passedSup = true;
                                }

                                if (!passedSup && Character.isDigit(ch)) {
                                    stringBuilder.append(indexToEvent.get(Character.getNumericValue(ch))).append('|');
                                } else if (ch != ' '){
                                    stringBuilder.append(ch);
                                }
                            }

                            finalWriter.println(stringBuilder.toString());
                        });
                        finalWriter.close();

                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.OPEN)) {
                            try {
                                desktop.open(new File(output));
                            } catch (Exception fileEx) {
                                JOptionPane.showMessageDialog(null,
                                        "An error occured while opening the output file. ERROR MESSAGE = " + e.toString(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (IOException ex) {
                        RunButton.setEnabled(false);
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "An error occured while converting the file. ERROR MESSAGE = " + e.toString(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (IOException ex){
                    RunButton.setEnabled(false);

                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "An error occured opening the input file. ERROR MESSAGE = " + e.toString(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    public static void main(String[] args)
    {
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
        frame.setVisible(true);
    }
}