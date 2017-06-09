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
    protected JComboBox<String> toList;
    protected JComboBox<Date> dateList;
    protected String selectedStopA;
    protected String selectedStopB;
    protected java.sql.Date selectedDate;
    static protected int selectedRouteID;
    private enum Actions {
        ret, from, to, date,search
    }

    public SearchPanel(MainPanel parentPanel, Connection conn){
        this.setLayout(new BorderLayout());
        mainPanel = parentPanel;
        this.conn = conn;

        contentManager = new GridLayout(4,2, 10, 10);

        JPanel titlePanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(contentManager);

        add(titlePanel,BorderLayout.PAGE_START);
        add(contentPanel, BorderLayout.CENTER);


        ImageIcon returnIcon = new ImageIcon("img/back.png","Wróć");
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);

        JLabel title = new JLabel("Wyszukaj połączenie");
        title.setFont(new Font("Verdana", 1, 20));

        titlePanel.add(returnButton, BorderLayout.WEST);
        returnButton.setActionCommand(Actions.ret.name());
        returnButton.addActionListener(this);
        titlePanel.add(title,BorderLayout.EAST);

        JLabel from = new JLabel("Z:");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel to = new JLabel("Do:");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel date = new JLabel("Data wyjazdu:");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel search = new JLabel("Szukaj");
        title.setFont(new Font("Verdana", 1, 14));

        Vector<String> fromData = Select("SELECT \"przystanek_poczatkowy\" FROM Trasy GROUP BY \"przystanek_poczatkowy\"");
        JComboBox<String> fromList = new JComboBox<String>(new DefaultComboBoxModel<String>(fromData));
        fromList.setActionCommand(Actions.from.name());
        fromList.addActionListener(this);

        toList = new JComboBox<String>();
        toList.setActionCommand(Actions.to.name());
        toList.addActionListener(this);

//        UtilDateModel model = new UtilDateModel();
//        Properties p = new Properties();
//        p.put("text.today", "Dzisiaj");
//        p.put("text.month", "Miesiąc");
//        p.put("text.year", "Year");
//        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
//        JDatePickerImpl datePicker= new JDatePickerImpl(datePanel, new DateFormatter());

        dateList = new JComboBox<Date>();
        dateList.setActionCommand(Actions.date.name());
        dateList.addActionListener(this);

        ImageIcon searchIcon = new ImageIcon("img/search.png","Szukaj");
        JButton searchButton = new JButton(searchIcon);
        searchButton.setActionCommand(Actions.search.name());
        searchButton.addActionListener(this);
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);

        contentPanel.add(from);
        contentPanel.add(fromList);
        contentPanel.add(to);
        contentPanel.add(toList);
        contentPanel.add(date);
//        contentPanel.add(datePicker);
        contentPanel.add(dateList);
        contentPanel.add(search);
        contentPanel.add(searchButton);

        contentManager.addLayoutComponent("fromLabel", from);
        contentManager.addLayoutComponent("fromList", fromList);
        contentManager.addLayoutComponent("toLabel", to);
        contentManager.addLayoutComponent("toList", toList);
        contentManager.addLayoutComponent("dateLabel", date);
//        contentManager.addLayoutComponent("datePicker", datePicker);
        contentManager.addLayoutComponent("dateList",dateList);
        contentManager.addLayoutComponent("searchLabel", search);
        contentManager.addLayoutComponent("searchButton", searchButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == Actions.ret.name()){
            mainPanel.layoutManager.show(mainPanel, "menuPanel");
        }
        if(e.getActionCommand() == Actions.from.name()){
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            selectedStopA = (String) cb.getSelectedObjects()[0];
            Vector<String> toData = Select("SELECT \"przystanek_koncowy\" " +
                    "FROM TRASY " +
                    "WHERE \"przystanek_poczatkowy\"" +
                    " = '" + selectedStopA +"'");
            toList.setModel(new DefaultComboBoxModel<String>(toData));
        }
        if(e.getActionCommand() == Actions.to.name()){
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            selectedStopB = (String) cb.getSelectedObjects()[0];
            Vector<String> routeData = Select("SELECT \"id_trasy\" FROM TRASY WHERE \"przystanek_poczatkowy\" = '" + selectedStopA + "' AND \"przystanek_koncowy\" = '" + selectedStopB +"'");
            Vector<Date> dateData =
                    SelectDate("SELECT \"data\" FROM KURSY" +
                            " INNER JOIN POZYCJE_ROZKLADU_JAZDY ON KURSY.\"id_rozkladu\" = POZYCJE_ROZKLADU_JAZDY.\"id_rozkladu\"" +
                            " WHERE POZYCJE_ROZKLADU_JAZDY.\"id_trasy\" = "
                            + routeData.lastElement());
            dateList.setModel(new DefaultComboBoxModel<Date>(dateData));
        }
        if(e.getActionCommand() == Actions.date.name()){
            JComboBox<Date> cb = (JComboBox<Date>) e.getSource();
            selectedDate = (java.sql.Date) cb.getSelectedObjects()[0];
            Vector<String> routeData = Select("SELECT \"id_kursu\" " +
                    "FROM Kursy " +
                    "WHERE \"data\" =" + "TO_DATE('" + selectedDate + "', 'yyyy-mm-dd')");
            selectedRouteID = Integer.parseInt(routeData.lastElement());
        }
        if(e.getActionCommand() == Actions.search.name()){
            mainPanel.layoutManager.show(mainPanel, "passengerPanel");
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
}
