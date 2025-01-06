package main;
import javax.swing.*;

import database.DBConnection;
import database.DBManager;
import view.Login;
import view.Montage;
import view.Verkauf;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        
        try {
            connection = dbConnection.connect(); 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Datenbankverbindung fehlgeschlagen.");
            return; 
        }//Met
        DBManager dbManager = new DBManager(connection);
        
        Login loginFrame = new Login();

        loginFrame.getLoginButton().addActionListener(e -> {
            String username = loginFrame.getUsernameField().getText();
            String password = new String(loginFrame.getPasswordField().getPassword());

            String department = dbManager.getDepartmentByUsernameAndPassword(username, password);

            if (department != null) {
                JOptionPane.showMessageDialog(loginFrame, "Login erfolgreich! Abteilung: " + department);
                loginFrame.dispose();

                if (department.equals("Verkauf")) {
                    new Verkauf(dbManager);
                } else if (department.equals("Montage")) {
                    new Montage(dbManager);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Unbekannte Abteilung.");
                }
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Login fehlgeschlagen. Bitte überprüfen Sie Ihre Zugangsdaten.");
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(dbConnection::close));
    }
}
