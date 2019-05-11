import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainWindow implements LoginDelegate {
    private JPanel MainPanel;
    private JLabel helloLabel;
    private JButton chooseFileButton;

    public MainWindow() {
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFileAction();
            }
        });
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        JFrame frame = new JFrame("PTS");
        frame.setContentPane(mainWindow.MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(400, 400));

        LoginForm lgf = new LoginForm(mainWindow);
        lgf.setVisible(true);
        lgf.pack();
        lgf.setLocationRelativeTo(null);
        lgf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    @Override
    public void didLogSuccessful(LoginForm loginForm) {
        loginForm.dispose();
        helloLabel.setText("You signed in successfully!");
        chooseFileButton.setEnabled(true);
    }

    private void chooseFileAction() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV and TXT files", "txt", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            try {
                processFile(chooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        System.out.println(resultStringBuilder.toString());

    }
}
