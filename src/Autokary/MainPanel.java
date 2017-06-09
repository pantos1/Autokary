package Autokary;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Created by Piotr on 05.06.2017.
 */
public class MainPanel extends JPanel {

    protected CardLayout layoutManager;

    public MainPanel(Connection conn){
        layoutManager = new CardLayout();
        setLayout(layoutManager);

        MenuPanel menu = new MenuPanel(this);
        SearchPanel search = new SearchPanel(this, conn);
//        ResultsPanel results = new ResultsPanel();
        PassengerPanel passenger = new PassengerPanel(this, conn);

        add(menu);
        add(search);
//        add(results);
        add(passenger);

        layoutManager.addLayoutComponent(menu, "menuPanel");
        layoutManager.addLayoutComponent(search, "searchPanel");
//        layoutManager.addLayoutComponent(results, "resultsPanel");
        layoutManager.addLayoutComponent(passenger, "passengerPanel");

        layoutManager.show(this, "menuPanel");
    }
}
