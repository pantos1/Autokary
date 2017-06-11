package Autokary;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.Calendar;

/**
 * Created by Kuba on 2017-06-09.
 */
public class PlannerPanel extends JPanel implements ActionListener{
    protected MainPanel mainPanel;
    protected GridLayout contentManager;
    protected Connection conn;
    protected ResultSet rs;
    protected Statement stmt;
    protected JComboBox<Date> dateList;
    static protected Date selectedDate;
    static protected int dayOfWeek;
    static protected String dzien;
    private enum Actions {
        ret, date,search
    }

    public PlannerPanel(MainPanel parentPanel, Connection conn){
        this.setLayout(new BorderLayout());
        mainPanel = parentPanel;
        this.conn = conn;

        contentManager = new GridLayout(3,2);

        JPanel titlePanel = new JPanel();
        JPanel contentPanel = new JPanel(contentManager);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.SOUTH);


        ImageIcon returnIcon = new ImageIcon("img/back.png","Wróć");
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);

        JLabel title = new JLabel("Wybierz konkretną datę nowego kursu:");
        title.setFont(new Font("Verdana", 1, 20));

        titlePanel.add(returnButton, BorderLayout.EAST);
        returnButton.setActionCommand("returnButtonAction");
        returnButton.addActionListener(this);
        titlePanel.add(title,BorderLayout.CENTER);

        JLabel date = new JLabel("Data kursu:");
        JLabel search = new JLabel("Szukaj");

        //dateList = new JComboBox<Date>();
        //dateList.setActionCommand(Actions.date.name());
        //dateList.addActionListener(this);

        ImageIcon searchIcon = new ImageIcon("img/search.png","Szukaj");
        JButton searchButton = new JButton(searchIcon);
        searchButton.setActionCommand(Actions.search.name());
        searchButton.addActionListener(this);
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);

        Vector<Date> Data = SelectDate("SELECT \"DATA\" FROM Kalendarz GROUP BY \"DATA\"");
        JComboBox<Date> dateList = new JComboBox<Date>(new DefaultComboBoxModel<Date>(Data));
        dateList.setActionCommand(Actions.date.name());
        dateList.addActionListener(this);

        contentPanel.add(date);
        contentPanel.add(dateList);
        contentPanel.add(search);
        contentPanel.add(searchButton);

        contentManager.addLayoutComponent("dateLabel", date);
        //contentManager.addLayoutComponent("datePicker", datePicker);
        contentManager.addLayoutComponent("searchLabel", search);
        contentManager.addLayoutComponent("searchButton", searchButton);


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "returnButtonAction"){
            mainPanel.layoutManager.show(mainPanel, "menuPanel");
        }
        else if(e.getActionCommand() == Actions.date.name()){

            JComboBox<Date> cb = (JComboBox<Date>) e.getSource();
            selectedDate = (Date) cb.getSelectedObjects()[0];
            //Calendar c = Calendar.getInstance();
            //c.setTime(selectedDate);
            //dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            //if (dayOfWeek == 2){
                    //dzien = "Poniedzialek";}
        }
        else if(e.getActionCommand() == Actions.search.name()){

            mainPanel.layoutManager.show(mainPanel, "addingPanel");
        }
    }


    protected Vector<Date> SelectDate(String input){
        try {
            stmt = conn.createStatement();
            String sql = input;
            Vector <Date> v = new Vector<Date>();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                v.addElement(rs.getDate(1));
            }
            rs.close();
            stmt.close();
            return v;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    protected Vector<String> Select(String input){
        try {
            stmt = conn.createStatement();
            String sql = input;
            Vector <String> v = new Vector<String>();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                v.addElement(rs.getString(1));
            }
            rs.close();
            stmt.close();
            return v;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}