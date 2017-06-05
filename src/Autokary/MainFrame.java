package Autokary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

/**
 * Created by Piotr on 05.06.2017.
 */
public class MainFrame extends JFrame {
    public MainFrame(){
        super("Autokary - panel zarządzania");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel mainPanel = new MainPanel();
        add(mainPanel);
        pack();
        int width = 500;
        int height = 300;
        setSize(width, height);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int)screenSize.getWidth()-width)/2, ((int)screenSize.getHeight()-height)/2);
    }
}
