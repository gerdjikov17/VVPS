import ca.pfv.spmf.algorithms.frequentpatterns.dci_closed_optimized.AlgoDCI_Closed_Optimized;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoBIDEPlus;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.SequentialPatterns;

import javax.swing.*;
import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
                // Create an instance of the algorithm
                AlgoDCI_Closed_Optimized algoDCIClosed = new AlgoDCI_Closed_Optimized();

                // Set this variable to true to show the transaction identifiers where patterns appear in the output file
                algoDCIClosed.setShowTransactionIdentifiers(true);

                // execute the algorithm
                try {
                    int minsup = 2;
                    try {
                        minsup = Integer.parseInt(MinsupField.getText());
                    }
                    catch(NumberFormatException numEx){
                        MinsupField.setText(Integer.toString(2));
                    }

                    String output = ".//output.txt";
                    File file = new File( output );
                    algoDCIClosed.runAlgorithm(FilePath, output, minsup);

                    Desktop desktop = Desktop.getDesktop();
                    // check first if we can open it on this operating system:
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        try {
                            // if yes, open it
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
                    JOptionPane.showMessageDialog(MainPanel, "Invalid file!", "Error", JOptionPane.ERROR_MESSAGE);
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
}