package Autokary;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 * Created by Piotr on 05.06.2017.
 */
public class SearchPanel extends JPanel {
    protected MainPanel mainPanel;
    protected GridLayout contentManager;

    public SearchPanel(MainPanel parentPanel){
        mainPanel = parentPanel;

        contentManager = new GridLayout(4,2);

        JPanel titlePanel = new JPanel();
        JPanel contentPanel = new JPanel(contentManager);

        add(titlePanel, BorderLayout.LINE_START);
        add(contentPanel, BorderLayout.CENTER);


        ImageIcon returnIcon = new ImageIcon("img/back.png","Wróć");
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);

        JLabel title = new JLabel("Wyszukaj połączenie");
        title.setFont(new Font("Verdana", 1, 20));

        titlePanel.add(returnButton, BorderLayout.LINE_START);
        titlePanel.add(title,BorderLayout.CENTER);

        JLabel from = new JLabel("Z:");
        JLabel to = new JLabel("Do:");
        JLabel date = new JLabel("Data wyjazdu:");
        JLabel search = new JLabel("Szukaj");

        JComboBox fromList = new JComboBox();
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
}
