import javax.swing.*;

public class MainWindow {
    private JPanel MainPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("PTS");
        frame.setContentPane(new MainWindow().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
