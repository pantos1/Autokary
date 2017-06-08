package Autokary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Piotr on 05.06.2017.
 */
public class MainFrame extends JFrame {
    protected Dimension d;
    protected Connection conn;
    protected boolean isConnected;

    protected String hostname = "localhost";
    protected int port = 1521;
    protected String SID = "orcl";
    protected String username = "c##Piotrek";
    protected String password = "Altanka1";

    public MainFrame(){
        super("Autokary - panel zarządzania");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        isConnected = connectToDB();
        if(!isConnected){
            JOptionPane.showMessageDialog(this, "Nie udało się nawiązać połączenia z bazą danych","Błąd połączenia", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        MainPanel mainPanel = new MainPanel(conn);
        addWindowListener(new WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(mainPanel);
        pack();
        setMinimumSize(getMinimumSize());
        setSize(400,500);
        d = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int)screenSize.getWidth()-d.width)/2, ((int)screenSize.getHeight()-d.height)/2);
    }

    protected boolean connectToDB(){

        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@"+hostname+":"+port+":"+SID, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
