package Autokary;

import javax.swing.*;

/**
 * Created by Piotr on 05.06.2017.
 */
public class Autokary {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              MainFrame frame = new MainFrame();
              frame.setVisible(true);
            }
        });
    }
}
