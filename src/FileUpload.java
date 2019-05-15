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
                parsed = FileConverter.convertFileAsSequence(getFile());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            if (parsed != null) {
                try {
                    double minsupD = Double.parseDouble(MinsupField.getText());
                    File outputFile = PrefixSpanAlgorithm.prefixSpan(parsed, minsupD);
                    if (outputFile.exists())
                        Desktop.getDesktop().open(outputFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

    private File getFile() {
        return new File(FilePath);
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