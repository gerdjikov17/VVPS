import javax.swing.*;

public class MainWindow implements LoginDelegate {
    private JFrame mainFrame;
    private JPanel MainPanel;

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        JFrame frame = new JFrame("PTS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        LoginForm login = new LoginForm(mainWindow);
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        mainWindow.mainFrame = frame;
    }

    @Override
    public void didLogSuccessful(LoginForm loginForm) {
        loginForm.dispose();
        FileUpload.showFileUpload(mainFrame);

    }
}
