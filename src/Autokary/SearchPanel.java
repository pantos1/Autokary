package Autokary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Piotr on 05.06.2017.
 */
public class SearchPanel extends JPanel implements ActionListener{
    protected MainPanel mainPanel;
    protected GridLayout contentManager;
    protected Connection conn;
    protected ResultSet rs;
    protected Statement stmt;
    protected JComboBox<String> fromList;
    protected JComboBox<String> toList;
    protected JComboBox<Date> dateList;
    protected String selectedStopA;
    protected String selectedStopB;
    protected java.sql.Date selectedDate;
    protected Vector<String> routeData;
    static protected Integer selectedRouteID;
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

        fromList = new JComboBox<String>();
        fromList.setActionCommand(Actions.from.name());
        fromList.addActionListener(this);

        toList = new JComboBox<String>();
        toList.setActionCommand(Actions.to.name());
        toList.addActionListener(this);

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
        contentPanel.add(dateList);
        contentPanel.add(search);
        contentPanel.add(searchButton);

        contentManager.addLayoutComponent("fromLabel", from);
        contentManager.addLayoutComponent("fromList", fromList);
        contentManager.addLayoutComponent("toLabel", to);
        contentManager.addLayoutComponent("toList", toList);
        contentManager.addLayoutComponent("dateLabel", date);
        contentManager.addLayoutComponent("dateList",dateList);
        contentManager.addLayoutComponent("searchLabel", search);
        contentManager.addLayoutComponent("searchButton", searchButton);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                Vector<String> fromData = Select("SELECT \"przystanek_poczatkowy\" FROM Trasy GROUP BY \"przystanek_poczatkowy\"");
                fromList.setModel(new DefaultComboBoxModel<String>(fromData));
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == Actions.ret.name()){
            clearForm();
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
            routeData = Select("SELECT \"id_trasy\" FROM TRASY WHERE \"przystanek_poczatkowy\" = '" + selectedStopA + "' AND \"przystanek_koncowy\" = '" + selectedStopB +"'");
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
            Vector<String> selectedRoute = Select("SELECT \"id_kursu\" " +
                    "FROM Kursy FULL OUTER JOIN POZYCJE_ROZKLADU_JAZDY ON KURSY.\"id_rozkladu\" = POZYCJE_ROZKLADU_JAZDY.\"id_rozkladu\"" +
                    "WHERE \"data\" =" + "TO_DATE('" + selectedDate + "', 'yyyy-mm-dd') AND  \"id_trasy\" = " + routeData.lastElement());
            selectedRouteID = Integer.parseInt(selectedRoute.lastElement());
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

    public void clearForm(){
        fromList.setSelectedItem(null);
        toList.setSelectedItem(null);
        dateList.setSelectedItem(null);

        selectedStopA = null;
        selectedStopB = null;
        selectedDate = null;
        selectedRouteID = null;
    }
}
