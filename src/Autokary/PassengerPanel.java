package Autokary;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Piotr on 05.06.2017.
 */
public class PassengerPanel extends JPanel implements ActionListener {
    protected MainPanel mainPanel;
    protected GridLayout contentManager;
    protected Connection conn;
    protected JTextField nameTextField;
    protected JTextField surnameTextField;
    protected JTextField emailTextField;
    protected JTextField dobTextField;
    protected String passengersName;
    protected String passengersSurname;
    protected String passengersEmail;
    protected String passengersDOB;
    protected Integer passengersID;
    protected Double price = 0.0;
    protected ResultSet rs;
    protected Statement stmt;
    private Double priceModifier = 0.0;

    private enum Actions {
        ret, name, surname, email, DOB, next
    }

    public PassengerPanel(MainPanel parentPanel, Connection conn){
        this.setLayout(new BorderLayout());
        mainPanel = parentPanel;
        this.conn = conn;

        contentManager = new GridLayout(5,2, 10, 10);

        JPanel titlePanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(contentManager);

        add(titlePanel,BorderLayout.PAGE_START);
        add(contentPanel, BorderLayout.CENTER);

        ImageIcon returnIcon = new ImageIcon("img/back.png","Wróć");
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);

        JLabel title = new JLabel("Wprowadź dane pasażera");
        title.setFont(new Font("Verdana", 1, 20));

        titlePanel.add(returnButton, BorderLayout.WEST);
        returnButton.setActionCommand(Actions.ret.name());
        returnButton.addActionListener(this);
        titlePanel.add(title,BorderLayout.EAST);

        JLabel name = new JLabel("Imię:");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel surname = new JLabel("Nazwisko:");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel email = new JLabel("Adres email:");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel DOB = new JLabel("Data urodzenia (w formacie rrrr-mm-dd):");
        title.setFont(new Font("Verdana", 1, 14));
        JLabel next = new JLabel("Dokonaj zakupu");
        title.setFont(new Font("Verdana", 1, 14));

        nameTextField = new JTextField(16);
        nameTextField.setActionCommand(Actions.name.name());
        nameTextField.addActionListener(this);

        surnameTextField = new JTextField(30);
        surnameTextField.setActionCommand(Actions.surname.name());
        surnameTextField.addActionListener(this);

        emailTextField = new JTextField();
        emailTextField.setActionCommand(Actions.email.name());
        emailTextField.addActionListener(this);

        dobTextField = new JTextField();
        dobTextField.setActionCommand(Actions.DOB.name());
        dobTextField.addActionListener(this);

        ImageIcon nextIcon = new ImageIcon("img/next.png","Zapłać");
        JButton nextButton = new JButton(nextIcon);
        nextButton.setActionCommand(Actions.next.name());
        nextButton.addActionListener(this);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);

        contentPanel.add(name);
        contentPanel.add(nameTextField);
        contentPanel.add(surname);
        contentPanel.add(surnameTextField);
        contentPanel.add(email);
        contentPanel.add(emailTextField);
        contentPanel.add(DOB);
        contentPanel.add(dobTextField);
        contentPanel.add(next);
        contentPanel.add(nextButton);

        contentManager.addLayoutComponent("nameLabel", name);
        contentManager.addLayoutComponent("nameField", nameTextField);
        contentManager.addLayoutComponent("surnameLabel", surname);
        contentManager.addLayoutComponent("surnameField", surnameTextField);
        contentManager.addLayoutComponent("emailLabel", email);
        contentManager.addLayoutComponent("emailField", emailTextField);
        contentManager.addLayoutComponent("dobLabel", DOB);
        contentManager.addLayoutComponent("dobField", dobTextField);
        contentManager.addLayoutComponent("nextLabel", next);
        contentManager.addLayoutComponent("nextButton", nextButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == Actions.ret.name()){
            mainPanel.layoutManager.show(mainPanel, "searchPanel");
        }
        if(e.getActionCommand() == Actions.name.name()){
            passengersName = nameTextField.getText();
        }
        if(e.getActionCommand() == Actions.surname.name()){
            passengersSurname = surnameTextField.getText();
        }
        if(e.getActionCommand() == Actions.email.name()){
            passengersEmail = emailTextField.getText();
        }
        if(e.getActionCommand() == Actions.DOB.name()){
            passengersDOB = dobTextField.getText();
        }
        if(e.getActionCommand() == Actions.next.name()){
            int n;
            int numberOfPassengers;
            passengersName = nameTextField.getText();
            passengersSurname = surnameTextField.getText();
            passengersEmail = emailTextField.getText();
            passengersDOB = dobTextField.getText();
            try {
                //Sprawdzenie czy istnieje pasażer o podanych danych.
                stmt = conn.createStatement();
                String sql = "SELECT COUNT(*) " +
                        "FROM Pasazerowie " +
                        "WHERE \"imię\" = '" + passengersName +
                        "' AND \"nazwisko\" ='" + passengersSurname +
                        "' AND \"data_urodzenia_pasażera\" = TO_DATE('" + passengersDOB + "', 'yyyy-mm-dd')" +
                        " AND \"email\"='" + passengersEmail +"'";
                rs = stmt.executeQuery(sql);
                if(rs.next()) {
                    numberOfPassengers = rs.getInt(1);
                    rs.close();
                    stmt.close();
                    //Jeśli nie istnieje, należy stworzyć nowy rekord na podstawie podanych danych.
                    if (numberOfPassengers == 0) {
                        stmt = conn.createStatement();
                        sql = "INSERT INTO Pasazerowie" +
                                "(\"id_pasażera\", \"imię\", \"nazwisko\", \"data_urodzenia_pasażera\", \"email\")" +
                                "VALUES (seq_pasażer.nextval, '" + passengersName + "','" + passengersSurname + "',TO_DATE('" + passengersDOB + "', 'yyyy-mm-dd'),'" + passengersEmail +"')";
                        n = stmt.executeUpdate(sql);
                        if (n == 0) {
                            JOptionPane.showMessageDialog(this, "Nie udało się dodać pasażera do bazy danych", "Błąd wstawiania", JOptionPane.ERROR_MESSAGE);
                        }
                        stmt.close();
                    }
                    //Zapytanie o id_pasażera o podanych danych.
                        stmt = conn.createStatement();
                        sql = "SELECT \"id_pasażera\" " +
                                "FROM Pasazerowie " +
                                "WHERE \"imię\" = '" + passengersName +
                                "' AND \"nazwisko\" ='" + passengersSurname +
                                "' AND \"data_urodzenia_pasażera\" = TO_DATE('" + passengersDOB + "', 'yyyy-mm-dd')" +
                                " AND \"email\" ='" + passengersEmail +"'";
                        rs = stmt.executeQuery(sql);
                        if(rs.next()) passengersID = rs.getInt(1);
                        rs.close();
                        stmt.close();
                }
                //Zapytanie o cenę odcinków wchodzących w skład wybranej trasy i modyfikator ceny wynikający z pozycji rozkładu jazdy.
                stmt = conn.createStatement();
                sql = "SELECT \"cena_odcinka\", \"modyfikator_ceny\" " +
                        "FROM ODCINKI FULL OUTER JOIN TRASY " +
                        "ON ODCINKI.\"id_trasy\" = TRASY.\"id_trasy\" " +
                        "FULL OUTER JOIN POZYCJE_ROZKLADU_JAZDY ON TRASY.\"id_trasy\" = POZYCJE_ROZKLADU_JAZDY.\"id_trasy\" " +
                        "WHERE ODCINKI.\"id_trasy\" =" + SearchPanel.routeData.lastElement();
                rs = stmt.executeQuery(sql);
                while (rs.next()){
                    price += rs.getDouble("cena_odcinka");
                    priceModifier = rs.getDouble("modyfikator_ceny");
                }
                price = price * priceModifier;
                rs.close();
                stmt.close();
                //Wstawienie do tabeli Rezerwacje rekordu o podanych danych i obliczonej cenie.
                stmt = conn.createStatement();
                sql = "INSERT INTO Rezerwacje " +
                        "(\"id_rezerwacji\",\"id_kursu\", \"id_pasażera\", \"cena_biletu\", \"zaplacone\") " +
                        "VALUES (seq_rezerwacja.nextval," + SearchPanel.selectedRouteID + "," + passengersID + "," + price + "," + " 1) ";
                n = stmt.executeUpdate(sql);
                if(n == 0){
                    JOptionPane.showMessageDialog(this, "Nie udało się dodać rezerwacji","Błąd wstawiania", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(this, "Rezerwacja dodana. Cena biletu:" + price , "", JOptionPane.INFORMATION_MESSAGE);
                }
                stmt.close();
                mainPanel.search.clearForm();
                clearForm();
                mainPanel.layoutManager.show(mainPanel, "menuPanel");
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public void clearForm(){
        nameTextField.setText(null);
        surnameTextField.setText(null);
        emailTextField.setText(null);
        dobTextField.setText(null);

        passengersName = null;
        passengersSurname = null;
        passengersEmail = null;
        passengersDOB = null;
        passengersID = null;
    }
}
