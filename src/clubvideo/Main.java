package clubvideo;

import clubvideo.util.UIStyles;
import clubvideo.view.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        UIStyles.applyLookAndFeel();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
