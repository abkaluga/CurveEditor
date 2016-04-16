import view.MainWindow;

import javax.swing.*;

/**
 * Created by Albert on 12.03.2016.
 */
public class EntryPoint {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.print("Unable to get system look and feel");
        }
        MainWindow window = new MainWindow();
    }
}
