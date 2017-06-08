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
    private enum Actions {
        b1, b2, b3, b4, b5, b6
    }
    public MenuPanel(MainPanel parentPanel){
        super(new FlowLayout(FlowLayout.CENTER));

        mainPanel = parentPanel;

        JPanel menuPanel = new JPanel(new GridLayout(2,1));
        JPanel butPanel = new JPanel(new GridLayout(4, 1, 5,10));

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
        but1.setActionCommand(Actions.b1.name());
        but1.addActionListener(this);
        butPanel.add(but2);
        but2.setActionCommand(Actions.b2.name());
        but2.addActionListener(this);
        butPanel.add(but3);
        but3.setActionCommand(Actions.b3.name());
        but3.addActionListener(this);
        butPanel.add(but4);
        but4.setActionCommand(Actions.b4.name());
        but4.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == Actions.b1.name()){
            mainPanel.layoutManager.show(mainPanel, "searchPanel");
        }
        else if(e.getActionCommand() == Actions.b2.name()){
        }
        else if(e.getActionCommand() == Actions.b3.name()){
        }
        else if(e.getActionCommand() == Actions.b4.name()){
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
