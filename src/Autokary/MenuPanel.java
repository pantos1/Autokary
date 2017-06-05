package Autokary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Piotr on 05.06.2017.
 */
public class MenuPanel extends JPanel implements ActionListener{
    protected MainPanel mainPanel;

    public MenuPanel(MainPanel parentPanel){
        super(new FlowLayout(FlowLayout.CENTER));

        mainPanel = parentPanel;

        JPanel menuPanel = new JPanel(new GridLayout(2,1));
        JPanel butPanel = new JPanel(new GridLayout(4, 1));

        add(menuPanel);

        JLabel title = new JLabel("Panel zarządzania");
        title.setFont(new Font("Verdana", 1, 20));

        menuPanel.add(title);
        menuPanel.add(butPanel);

        JButton but1 = new JButton("Zakup biletu");
        JButton but2 = new JButton("Dodanie kursu");
        JButton but3 = new JButton("Pomoc");
        JButton but4 = new JButton("Wyjście");

        butPanel.add(but1);
        but1.setActionCommand("but1Action");
        but1.addActionListener(this);
        butPanel.add(but2);
        but2.setActionCommand("but2Action");
        but2.addActionListener(this);
        butPanel.add(but3);
        but3.setActionCommand("but3Action");
        but3.addActionListener(this);
        butPanel.add(but4);
        but4.setActionCommand("but4Action");
        but4.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "but1Action"){
            mainPanel.layoutManager.show(mainPanel, "searchPanel");
        }
        else if(e.getActionCommand() == "but2Action"){
        }
        else if(e.getActionCommand() == "but3Action"){
        }
        else if(e.getActionCommand() == "but4Action"){
            String[] options = new String[] {"Tak", "Nie"};
            int d1 = JOptionPane.showOptionDialog(null, "Czy na pewno chcesz zamknąć program?", "Kończenie pracy", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
            if (d1 == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            else if (d1 == JOptionPane.NO_OPTION) {
            }
        }
    }
}
