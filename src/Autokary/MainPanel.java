package Autokary;

        import javax.swing.*;
        import java.awt.*;
        import java.sql.Connection;

/**
 * Created by Piotr on 05.06.2017.
 */
public class MainPanel extends JPanel {

    protected CardLayout layoutManager;
    MenuPanel menu;
    SearchPanel search;
    PassengerPanel passenger;
    PlannerPanel planner;
    AddingPanel adding;

    public MainPanel(Connection conn){
        layoutManager = new CardLayout();
        setLayout(layoutManager);

        menu = new MenuPanel(this);
        search = new SearchPanel(this, conn);
        passenger = new PassengerPanel(this, conn);
        planner = new PlannerPanel(this, conn);
        adding = new AddingPanel(this, conn);

        add(menu);
        add(search);
        add(passenger);
        add(planner);
        add(adding);

        layoutManager.addLayoutComponent(menu, "menuPanel");
        layoutManager.addLayoutComponent(search, "searchPanel");
        layoutManager.addLayoutComponent(passenger, "passengerPanel");
        layoutManager.addLayoutComponent(planner, "plannerPanel");
        layoutManager.addLayoutComponent(adding, "addingPanel");

        layoutManager.show(this, "menuPanel");
    }
}