package Autokary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;

/**
 * Created by Piotr on 05.06.2017.
 */
public class MainFrame extends JFrame {
    protected Dimension d;

    public MainFrame(){
        super("Autokary - panel zarządzania");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel mainPanel = new MainPanel();
        add(mainPanel);
        pack();
        setMinimumSize(getMinimumSize());
        setSize(400,500);
        d = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int)screenSize.getWidth()-d.width)/2, ((int)screenSize.getHeight()-d.height)/2);
    }
}
