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
        PlannerPanel planner = new PlannerPanel(this, conn);
        AddingPanel adding = new AddingPanel(this, conn);

        add(menu);
        add(search);
//        add(results);
        add(passenger);
        add(planner);
        add(adding);

        layoutManager.addLayoutComponent(menu, "menuPanel");
        layoutManager.addLayoutComponent(search, "searchPanel");
//        layoutManager.addLayoutComponent(results, "resultsPanel");
        layoutManager.addLayoutComponent(passenger, "passengerPanel");
        layoutManager.addLayoutComponent(planner, "plannerPanel");
        layoutManager.addLayoutComponent(adding, "addingPanel");

        layoutManager.show(this, "menuPanel");
    }
}