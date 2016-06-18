import view.MainWindow;

import javax.swing.*;

/**
 * Created by Albert on 12.03.2016.
 */
/* TODO 0. Naprawić kolory, Numerowanie :DONE
   TODO 1. Dodawanie punktu pomiędzy punkty. :DONE
   TODO 2. Obniżanie/podwyższanie stopnia krzywych Beziera o kilka stopni :DONE
   TODO 3. Podział krzywej conajmniej na pół. :DONE
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
