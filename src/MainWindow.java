import javax.swing.*;

public class MainWindow implements LoginDelegate {
    private JPanel MainPanel;
    private JFrame mainFrame;

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        JFrame frame = new JFrame("PTS");
        mainWindow.mainFrame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

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
        FileUpload.showFileUpload(mainFrame);
    }
}
