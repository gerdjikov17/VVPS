import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private JPanel MainPanel;

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        JFrame frame = new JFrame("PTS");
        frame.setContentPane(mainWindow.MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        RegisterForm rgf = new RegisterForm();
        rgf.setVisible(true);
        rgf.pack();
        rgf.setLocationRelativeTo(null);
        rgf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
