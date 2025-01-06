package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://3.69.96.96:80/db1"; 
    private static final String USER = "db1"; 
    private static final String PASSWORD = "!db1.wip24?WS1";

    private Connection connection;

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Verbindung zur MySQL-Datenbank erfolgreich hergestellt.");
        }
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Fehler beim Schließen der Datenbankverbindung.");
            }
        }
    }
}
