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
import java.util.Properties;
import java.util.Vector;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 * Created by Piotr on 05.06.2017.
 */
public class SearchPanel extends JPanel implements ActionListener{
    protected MainPanel mainPanel;
    protected GridLayout contentManager;
    protected Connection conn;
    protected ResultSet rs;
    protected Statement stmt;

    public SearchPanel(MainPanel parentPanel, Connection conn){
        mainPanel = parentPanel;
        this.conn = conn;

        contentManager = new GridLayout(4,2);

        JPanel titlePanel = new JPanel();
        JPanel contentPanel = new JPanel(contentManager);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.SOUTH);


        ImageIcon returnIcon = new ImageIcon("img/back.png","Wróć");
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);

        JLabel title = new JLabel("Wyszukaj połączenie");
        title.setFont(new Font("Verdana", 1, 20));

        titlePanel.add(returnButton, BorderLayout.EAST);
        returnButton.setActionCommand("returnButtonAction");
        returnButton.addActionListener(this);
        titlePanel.add(title,BorderLayout.CENTER);

        JLabel from = new JLabel("Z:");
        JLabel to = new JLabel("Do:");
        JLabel date = new JLabel("Data wyjazdu:");
        JLabel search = new JLabel("Szukaj");

        Select("SELECT \"przystanek_poczatkowy\" FROM Trasy GROUP BY \"przystanek_poczatkowy\"");
        Vector<String> v = new Vector<String>();
        try {
            while (rs.next()){
                v.addElement(rs.getString("przystanek_poczatkowy"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JComboBox fromList = new JComboBox(new DefaultComboBoxModel(v));
        JComboBox toList = new JComboBox();

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dzisiaj");
        p.put("text.month", "Miesiąc");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker= new JDatePickerImpl(datePanel, new DateFormatter());

        ImageIcon searchIcon = new ImageIcon("img/search.png","Szukaj");
        JButton searchButton = new JButton(searchIcon);
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);

        contentPanel.add(from);
        contentPanel.add(fromList);
        contentPanel.add(to);
        contentPanel.add(toList);
        contentPanel.add(date);
        contentPanel.add(datePicker);
        contentPanel.add(search);
        contentPanel.add(searchButton);

        contentManager.addLayoutComponent("fromLabel", from);
        contentManager.addLayoutComponent("fromList", fromList);
        contentManager.addLayoutComponent("toLabel", to);
        contentManager.addLayoutComponent("toList", toList);
        contentManager.addLayoutComponent("dateLabel", date);
        contentManager.addLayoutComponent("datePicker", datePicker);
        contentManager.addLayoutComponent("searchLabel", search);
        contentManager.addLayoutComponent("searchButton", searchButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "returnButtonAction"){
            mainPanel.layoutManager.show(mainPanel, "menuPanel");
        }
    }

    protected void Select (String input){
        try {
            stmt = conn.createStatement();
            String sql = input;
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
