package Autokary;

import com.sun.org.apache.bcel.internal.generic.Select;

import javax.swing.*;
import javax.swing.text.DateFormatter;
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
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Kuba on 2017-06-09.
 */
public class AddingPanel extends JPanel implements ActionListener{
    protected MainPanel mainPanel;
    protected GridLayout contentManager;
    protected Connection conn;
    protected ResultSet rs;
    protected Statement stmt;
    protected JComboBox<String> routeList;
    protected JComboBox<String> busList;
    protected JComboBox<String> driverList;
    protected JComboBox<String> steward1List;
    protected JComboBox<String> steward2List;
    protected Date selectedDate;
    protected String selectedRoute;
    protected String selectedBus;
    protected String selectedDriver;
    protected String selectedSteward1;
    protected String selectedSteward2;
    private enum Actions {
        ret, route, bus, driver, steward1, steward2, ad
    }

    public AddingPanel(MainPanel parentPanel, Connection conn){
        this.setLayout(new BorderLayout());
        mainPanel = parentPanel;
        this.conn = conn;

        contentManager = new GridLayout(6,2);

        JPanel titlePanel = new JPanel();
        JPanel contentPanel = new JPanel(contentManager);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.SOUTH);


        ImageIcon returnIcon = new ImageIcon("img/back.png","Wróć");
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);

        JLabel title = new JLabel("Wybierz trasę kursu, autokar i członków załogi:");
        title.setFont(new Font("Verdana", 1, 20));

        titlePanel.add(returnButton, BorderLayout.EAST);
        returnButton.setActionCommand("returnButtonAction");
        returnButton.addActionListener(this);
        titlePanel.add(title,BorderLayout.CENTER);

        JLabel route = new JLabel("Trasa:");
        JLabel bus = new JLabel("Autobus:");
        JLabel driver = new JLabel("Kierowca:");
        JLabel steward1 = new JLabel("Steward1:");
        JLabel steward2 = new JLabel("Steward2:");
        JLabel ad = new JLabel("Dodaj:");


        ImageIcon adIcon = new ImageIcon("img/plus.png","Dodaj");
        JButton adButton = new JButton(adIcon);
        adButton.setActionCommand(Actions.ad.name());
        adButton.addActionListener(this);
        adButton.setBorderPainted(false);
        adButton.setContentAreaFilled(false);

        //Vector<String> routeData = Select("SELECT \"przystanek_poczatkowy\", \"przystanek_koncowy\" FROM Trasy AS t RIGHT OUTER JOIN Pozycje_rozkladu_jazdy AS prj " +
        //        "WHERE t.id_trasy = prj.id_trasy AND prj.dzien_tygodnia = to_char(to_date("+PlannerPanel.selectedDate+",'dd/mm/yyyy'), 'Day')");
        //JComboBox<String> routeList = new JComboBox<String>(new DefaultComboBoxModel<String>(routeData));

        JComboBox<String> routeList = new JComboBox<String>();
        routeList.setActionCommand(Actions.route.name());
        routeList.addActionListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                //Vector<String> dzien_z_daty = Select("SELECT to_char(to_date(" + PlannerPanel.selectedDate + ",'yyyy-mm-dd'), 'Day');");
                //Vector<String> dzien_pozycji = Select("SELECT \"dzien_tygodnia\" From pozycje_rozkladu_jazdy;");

                Vector<String> routeData = Select("SELECT \"id_rozkladu\" FROM Pozycje_rozkladu_jazdy " +
                        "LEFT OUTER JOIN Trasy " +
                        "ON trasy.\"id_trasy\" = Pozycje_rozkladu_jazdy.\"id_trasy\"" +
                        "RIGHT OUTER JOIN kalendarz ON pozycje_rozkladu_jazdy.\"dzien_tygodnia\" = kalendarz.\"DZIEN_TYGODNIA\"" +
                        "Where kalendarz.\"DATA\" = '" + PlannerPanel.selectedDate +"'");



                routeList.setModel(new DefaultComboBoxModel<String>(routeData));
            }
        });



        busList = new JComboBox<String>();
        busList.setActionCommand(Actions.bus.name());
        busList.addActionListener(this);

        driverList = new JComboBox<String>();
        driverList.setActionCommand(Actions.driver.name());
        driverList.addActionListener(this);

        steward1List = new JComboBox<String>();
        steward1List.setActionCommand(Actions.steward1.name());
        steward1List.addActionListener(this);

        steward2List = new JComboBox<String>();
        steward2List.setActionCommand(Actions.steward2.name());
        steward2List.addActionListener(this);

        steward2List.setEditable(false);
        steward2List.setEnabled(false);

        contentPanel.add(route);
        contentPanel.add(routeList);
        contentPanel.add(bus);
        contentPanel.add(busList);
        contentPanel.add(driver);
        contentPanel.add(driverList);
        contentPanel.add(steward1);
        contentPanel.add(steward1List);
        contentPanel.add(steward2);
        contentPanel.add(steward2List);
        contentPanel.add(ad);
        contentPanel.add(adButton);

        contentManager.addLayoutComponent("routeLabel", route);
        contentManager.addLayoutComponent("busLabel", bus);
        contentManager.addLayoutComponent("driverLabel", driver);
        contentManager.addLayoutComponent("steward1Label", steward1);
        contentManager.addLayoutComponent("steward2Label", steward2);
        contentManager.addLayoutComponent("searchLabel", ad);
        contentManager.addLayoutComponent("searchButton", adButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "returnButtonAction"){
            mainPanel.layoutManager.show(mainPanel, "plannerPanel");
        }
        else if(e.getActionCommand() == Actions.route.name()){
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            selectedRoute = (String) cb.getSelectedObjects()[0];
            Vector<String> busData = Select("SELECT \"id_autokaru\" " +
                    "FROM AUTOKARY " +
                    "RIGHT OUTER JOIN Pozycje_rozkladu_jazdy ON autokary.\"id_kategorii\" = Pozycje_rozkladu_jazdy.\"id_kategorii\"" +
                    "RIGHT OUTER JOIN grafiki_autokarow ON autokary.\"id_autokaru\" = grafiki_autokarow.\"ID_AUTOKARU\"" +
                    "WHERE Pozycje_rozkladu_jazdy.\"id_rozkladu\" = '" + selectedRoute +"'" +
                    "AND grafiki_autokarow.\"STATUS\" = 1 AND grafiki_autokarow.\"DATA\" = '" + PlannerPanel.selectedDate +"'");
            busList.setModel(new DefaultComboBoxModel<String>(busData));

            Vector<String> numberOfStewards = Select("SELECT Pozycje_rozkladu_jazdy.\"liczba_stewardow\" FROM Pozycje_rozkladu_jazdy " +
                   "WHERE Pozycje_rozkladu_jazdy.\"id_rozkladu\" = " + selectedRoute +"" +
                    "AND Pozycje_rozkladu_jazdy.\"liczba_stewardow\" = 2");

            if (!(numberOfStewards.isEmpty())){
            steward2List.setEditable(true);
            steward2List.setEnabled(true);
            }

        }
        else if(e.getActionCommand() == Actions.bus.name()){
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            selectedBus = (String) cb.getSelectedObjects()[0];
            Vector<String> driverData = Select("SELECT pracownicy.\"id_pracownika\" " +
                    "FROM PRACOWNICY " +
                    "LEFT OUTER JOIN  Stanowiska ON pracownicy.\"id_stanowiska\" = stanowiska.\"id_stanowiska\"" +
                    "LEFT OUTER JOIN grafiki_pracownikow ON grafiki_pracownikow.\"id_pracownika\" = pracownicy.\"id_pracownika\"" +
                    "WHERE stanowiska.\"nazwa_stanowiska\" = 'Kierowca'" +
                    "AND grafiki_pracownikow.\"status\" = 1 AND grafiki_pracownikow.\"DATA\" = '" + PlannerPanel.selectedDate +"'");
            driverList.setModel(new DefaultComboBoxModel<String>(driverData));
        }
        else if(e.getActionCommand() == Actions.driver.name()){
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            selectedDriver = (String) cb.getSelectedObjects()[0];
            Vector<String> steward1Data = Select("SELECT pracownicy.\"id_pracownika\" " +
                    "FROM PRACOWNICY " +
                    "LEFT OUTER JOIN  Stanowiska ON pracownicy.\"id_stanowiska\" = stanowiska.\"id_stanowiska\"" +
                    "LEFT OUTER JOIN grafiki_pracownikow ON grafiki_pracownikow.\"id_pracownika\" = pracownicy.\"id_pracownika\"" +
                    "WHERE stanowiska.\"nazwa_stanowiska\" = 'Stewardessa'" +
                    "AND grafiki_pracownikow.\"status\" = 1 AND grafiki_pracownikow.\"DATA\" = '" + PlannerPanel.selectedDate +"'");
            steward1List.setModel(new DefaultComboBoxModel<String>(steward1Data));
        }
        else if(e.getActionCommand() == Actions.steward1.name()){
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            selectedSteward1 = (String) cb.getSelectedObjects()[0];
            Vector<String> steward2Data = Select("SELECT pracownicy.\"id_pracownika\" " +
                    "FROM PRACOWNICY " +
                    "LEFT OUTER JOIN  Stanowiska ON pracownicy.\"id_stanowiska\" = stanowiska.\"id_stanowiska\"" +
                    "LEFT OUTER JOIN grafiki_pracownikow ON grafiki_pracownikow.\"id_pracownika\" = pracownicy.\"id_pracownika\"" +
                    "WHERE stanowiska.\"nazwa_stanowiska\" = 'Stewardessa'" +
                    "AND grafiki_pracownikow.\"status\" = 1 AND pracownicy.\"id_pracownika\" != "+selectedSteward1+"" +
                    "AND grafiki_pracownikow.\"DATA\" = '" + PlannerPanel.selectedDate +"'");
            steward2List.setModel(new DefaultComboBoxModel<String>(steward2Data));
        }
        else if(e.getActionCommand() == Actions.steward2.name()){
            JComboBox<Date> cb = (JComboBox<Date>) e.getSource();
            selectedSteward2 = (String) cb.getSelectedObjects()[0];
        }
        else if(e.getActionCommand() == Actions.ad.name()){
            int n;
            try{
                stmt = conn.createStatement();
                String sql = "INSERT INTO Kursy " +
                        "(\"id_kursu\",\"id_rozkladu\", \"id_autokaru\", \"ID_KIEROWCY\", \"ID_STEWARDA1\", \"ID_STEWARDA2\", \"DATA\") " +
                        "VALUES (seq_kursy.nextval,"+selectedRoute+","+selectedBus+ ","+selectedDriver+","+selectedSteward1+","+selectedSteward2+"," +
                        "TO_DATE('"+PlannerPanel.selectedDate+"', 'yyyy-mm-dd'))";
                n = stmt.executeUpdate(sql);
                rs.close();
                stmt.close();
            } catch (SQLException ex){
                ex.printStackTrace();
            }

            try{
                stmt = conn.createStatement();
                String sql = "UPDATE Grafiki_autokarow SET \"STATUS\" = '0'"+
                            "WHERE \"ID_AUTOKARU\" = '"+selectedBus+ "' AND \"DATA\" = '"+PlannerPanel.selectedDate+"'";
                n = stmt.executeUpdate(sql);
                rs.close();
                stmt.close();
            } catch (SQLException ex){
                ex.printStackTrace();
            }

            try{
                stmt = conn.createStatement();
                String sql = "UPDATE GRAFIKI_PRACOWNIKOW SET \"status\" = '0'"+
                    "WHERE \"id_pracownika\" = '"+selectedDriver+ "' AND \"DATA\" = '"+PlannerPanel.selectedDate+"'";
                n = stmt.executeUpdate(sql);
                rs.close();
                stmt.close();
            } catch (SQLException ex){
                ex.printStackTrace();
            }


            try{
                stmt = conn.createStatement();
                String sql = "UPDATE GRAFIKI_PRACOWNIKOW SET \"status\" = '0'"+
                        "WHERE \"id_pracownika\" = '"+selectedSteward1+ "' AND \"DATA\" = '"+PlannerPanel.selectedDate+"'";
                n = stmt.executeUpdate(sql);
                rs.close();
                stmt.close();
            } catch (SQLException ex){
                ex.printStackTrace();
            }

            if (!(selectedSteward2 == null)) {
                try {
                    stmt = conn.createStatement();
                    String sql = "UPDATE GRAFIKI_PRACOWNIKOW SET \"status\" = '0'"+
                            "WHERE \"id_pracownika\" = '"+selectedSteward2+ "' AND \"DATA\" = '"+PlannerPanel.selectedDate+"'";
                    n = stmt.executeUpdate(sql);
                    rs.close();
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            mainPanel.layoutManager.show(mainPanel, "menuPanel");
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
