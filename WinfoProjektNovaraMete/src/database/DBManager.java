package database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.Auftrag;
import model.Kunde;

public class DBManager {

    private Connection connection;

    // --------- Konstruktor ---------
    public DBManager(Connection connection) {
        this.connection = connection;
        try {
            initializeTables(); 
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Initialisieren der Tabellen.");
        }
    }
    
    // --------- Methode für Tabelleninitialisierung ---------
    private void initializeTables() throws SQLException {
        String createMitarbeiterTableSQL = "CREATE TABLE IF NOT EXISTS Mitarbeiter (" +
                                           "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                           "username VARCHAR(50) NOT NULL UNIQUE, " +
                                           "password VARCHAR(50) NOT NULL, " +
                                           "department VARCHAR(50) NOT NULL)";

        String createKundenTableSQL = "CREATE TABLE IF NOT EXISTS Kunden (" +
                                      "kundennummer INT AUTO_INCREMENT PRIMARY KEY, " +
                                      "anrede VARCHAR(10), " +
                                      "vorname VARCHAR(50), " +
                                      "nachname VARCHAR(50), " +
                                      "geburtstag VARCHAR(10), " +
                                      "email VARCHAR(50), " +
                                      "telefonnummer VARCHAR(20), " +
                                      "adresse_strasse VARCHAR(100), " +
                                      "adresse_hausnummer VARCHAR(10), " +
                                      "adresse_plz VARCHAR(10), " +
                                      "adresse_ort VARCHAR(50), " +
                                      "lieferadresse_strasse VARCHAR(100), " +
                                      "lieferadresse_hausnummer VARCHAR(10), " +
                                      "lieferadresse_plz VARCHAR(10), " +
                                      "lieferadresse_ort VARCHAR(50), " +
                                      "kundenkategorie VARCHAR(20))";

        String createAuftragTableSQL = "CREATE TABLE IF NOT EXISTS Auftrag (" +
                                       "auftragsnummer INT AUTO_INCREMENT PRIMARY KEY, " +
                                       "kundennummer INT, " +
                                       "cpu VARCHAR(50), " +
                                       "ram VARCHAR(50), " +
                                       "ssd VARCHAR(50), " +
                                       "hdd VARCHAR(50), " +
                                       "gpu VARCHAR(50), " +
                                       "motherboard VARCHAR(50), " +
                                       "netzteil VARCHAR(50), " +
                                       "kuehler VARCHAR(50), " +
                                       "laufwerk VARCHAR(50), " +
                                       "gehaeuse VARCHAR(50), " +
                                       "anzahl INT, " +
                                       "preis DECIMAL(10, 2), " +
                                       "datum DATE DEFAULT (CURRENT_DATE), " +
                                       "status VARCHAR(20) DEFAULT 'Eingegangen', " +
                                       "FOREIGN KEY (kundennummer) REFERENCES Kunden(kundennummer) ON DELETE CASCADE)";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createMitarbeiterTableSQL);
            stmt.executeUpdate(createKundenTableSQL);
            stmt.executeUpdate(createAuftragTableSQL);
        }
    }
    
    // --------- Methoden für Mitarbeiterverwaltung ---------
    
    public void addMitarbeiter(String username, String password, String department) {
        String insertMitarbeiterSQL = "INSERT INTO Mitarbeiter (username, password, department) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertMitarbeiterSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, department);
            stmt.executeUpdate();
            System.out.println("Mitarbeiter erfolgreich hinzugefügt.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Hinzufügen des Mitarbeiters.");
        }
    }

    public void removeMitarbeiterByUsername(String username) {
        String deleteMitarbeiterSQL = "DELETE FROM Mitarbeiter WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteMitarbeiterSQL)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Mitarbeiter erfolgreich entfernt.");
            } else {
                System.out.println("Kein Mitarbeiter mit diesem Benutzernamen gefunden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Entfernen des Mitarbeiters.");
        }
    }

    public void printAllMitarbeiter() {
        String query = "SELECT * FROM Mitarbeiter";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("Mitarbeiterliste:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Username: " + rs.getString("username") +
                                   ", Department: " + rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Abrufen der Mitarbeiterliste.");
        }
    }

    public String getDepartmentByUsernameAndPassword(String username, String password) {
        String department = null;
        String sql = "SELECT department FROM Mitarbeiter WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                department = rs.getString("department");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }

    
    
    //----------- Methoden für Kundenverwaltung ---------------
    
    public void insertKunde(Kunde kunde) {
        String sql = "INSERT INTO Kunden (anrede, vorname, nachname, geburtstag, email, telefonnummer, " +
                     "adresse_strasse, adresse_hausnummer, adresse_plz, adresse_ort, " +
                     "lieferadresse_strasse, lieferadresse_hausnummer, lieferadresse_plz, lieferadresse_ort, kundenkategorie) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, kunde.getAnrede());
            pstmt.setString(2, kunde.getVorname());
            pstmt.setString(3, kunde.getNachname());
            pstmt.setString(4, kunde.getGeburtstag());
            pstmt.setString(5, kunde.getEmail());
            pstmt.setString(6, kunde.getTelefonnummer());
            pstmt.setString(7, kunde.getAdresseStrasse());
            pstmt.setString(8, kunde.getAdresseHausnummer());
            pstmt.setString(9, kunde.getAdressePlz());
            pstmt.setString(10, kunde.getAdresseOrt());
            pstmt.setString(11, kunde.getLieferadresseStrasse());
            pstmt.setString(12, kunde.getLieferadresseHausnummer());
            pstmt.setString(13, kunde.getLieferadressePlz());
            pstmt.setString(14, kunde.getLieferadresseOrt());
            pstmt.setString(15, kunde.getKundenkategorie());
            pstmt.executeUpdate();
            System.out.println("Kunde erfolgreich hinzugefügt: " + kunde.getVorname() + " " + kunde.getNachname());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Hinzufügen des Kunden.");
        }
    }

    public void updateKunde(Kunde kunde) {
        String sql = "UPDATE Kunden SET anrede = ?, vorname = ?, nachname = ?, email = ?, telefonnummer = ?, " +
                     "adresse_strasse = ?, adresse_hausnummer = ?, adresse_plz = ?, adresse_ort = ?, " +
                     "lieferadresse_strasse = ?, lieferadresse_hausnummer = ?, lieferadresse_plz = ?, " +
                     "lieferadresse_ort = ? WHERE kundennummer = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, kunde.getAnrede());
            pstmt.setString(2, kunde.getVorname());
            pstmt.setString(3, kunde.getNachname());
            pstmt.setString(4, kunde.getEmail());
            pstmt.setString(5, kunde.getTelefonnummer());
            pstmt.setString(6, kunde.getAdresseStrasse());
            pstmt.setString(7, kunde.getAdresseHausnummer());
            pstmt.setString(8, kunde.getAdressePlz());
            pstmt.setString(9, kunde.getAdresseOrt());
            pstmt.setString(10, kunde.getLieferadresseStrasse());
            pstmt.setString(11, kunde.getLieferadresseHausnummer());
            pstmt.setString(12, kunde.getLieferadressePlz());
            pstmt.setString(13, kunde.getLieferadresseOrt());
            pstmt.setInt(14, kunde.getKundenNummer()); // Hier Kundennummer am Ende
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Kunde erfolgreich aktualisiert: " + kunde.getVorname() + " " + kunde.getNachname());
            } else {
                System.out.println("Kein Kunde mit der Kundennummer gefunden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Aktualisieren des Kunden.");
        }
    }

    
    public Kunde getKundeByNummer(int kundennummer) {
        String sql = "SELECT * FROM Kunden WHERE kundennummer = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, kundennummer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Kunde(
                    rs.getInt("kundennummer"),             
                    rs.getString("anrede"),
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    rs.getString("geburtstag"),
                    rs.getString("email"),
                    rs.getString("telefonnummer"),
                    rs.getString("adresse_strasse"),
                    rs.getString("adresse_hausnummer"),
                    rs.getString("adresse_plz"),
                    rs.getString("adresse_ort"),
                    rs.getString("lieferadresse_strasse"),
                    rs.getString("lieferadresse_hausnummer"),
                    rs.getString("lieferadresse_plz"),
                    rs.getString("lieferadresse_ort"),
                    rs.getString("kundenkategorie")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Abrufen des Kunden mit Kundennummer: " + kundennummer);
        }
        return null;
    }
    
    public void deleteKundeByNummer(int kundennummer) {
        String sql = "DELETE FROM Kunden WHERE kundennummer = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, kundennummer);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Kunde erfolgreich gelöscht: Kundennummer " + kundennummer);
            } else {
                System.out.println("Kein Kunde mit der Kundennummer gefunden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Löschen des Kunden.");
        }
    }
    
    public List<Kunde> getAllKunden() {
        List<Kunde> kundenListe = new ArrayList<>();
        String sql = "SELECT * FROM Kunden";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                kundenListe.add(new Kunde(
                    rs.getInt("kundennummer"), 
                    rs.getString("anrede"),
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    rs.getString("geburtstag"),
                    rs.getString("email"),
                    rs.getString("telefonnummer"),
                    rs.getString("adresse_strasse"),
                    rs.getString("adresse_hausnummer"),
                    rs.getString("adresse_plz"),
                    rs.getString("adresse_ort"),
                    rs.getString("lieferadresse_strasse"),
                    rs.getString("lieferadresse_hausnummer"),
                    rs.getString("lieferadresse_plz"),
                    rs.getString("lieferadresse_ort"),
                    rs.getString("kundenkategorie")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Abrufen der Kundenliste.");
        }
        return kundenListe;
    }

    // --------- Suche-Methoden für Kunden ---------

    public List<Kunde> searchKundeByNummer(int kundennummer) {
        List<Kunde> kundenListe = new ArrayList<>();
        String query = "SELECT * FROM Kunden WHERE kundennummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, kundennummer); 
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                kundenListe.add(new Kunde(
                    rs.getInt("kundennummer"), 
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    rs.getString("geburtstag")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kundenListe;
    }


    public List<Kunde> searchKundeByNachname(String nachname) {
        List<Kunde> kundenListe = new ArrayList<>();
        String query = "SELECT * FROM Kunden WHERE nachname = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nachname); 
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                kundenListe.add(new Kunde(
                    rs.getInt("kundennummer"), 
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    rs.getString("geburtstag")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kundenListe;
    }

    public List<Kunde> searchKundeByNummerAndNachname(int kundennummer, String nachname) {
        List<Kunde> kundenListe = new ArrayList<>();
        String query = "SELECT * FROM Kunden WHERE kundennummer = ? AND nachname = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, kundennummer); 
            stmt.setString(2, nachname);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                kundenListe.add(new Kunde(
                    rs.getInt("kundennummer"), 
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    rs.getString("geburtstag")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kundenListe;
    }

    //------------ Methoden für Auftragsverwaltung ----------------
    
    public void insertAuftrag(Auftrag auftrag) {
        String insertAuftragSQL = "INSERT INTO Auftrag (kundennummer, cpu, ram, ssd, hdd, gpu, " +
                "motherboard, netzteil, kuehler, laufwerk, gehaeuse, anzahl, preis, datum, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertAuftragSQL)) {
            stmt.setInt(1, auftrag.getKundennummer());
            stmt.setString(2, auftrag.getCpu());
            stmt.setString(3, auftrag.getRam());
            stmt.setString(4, auftrag.getSsd());
            stmt.setString(5, auftrag.getHdd());
            stmt.setString(6, auftrag.getGpu());
            stmt.setString(7, auftrag.getMotherboard());
            stmt.setString(8, auftrag.getNetzteil());
            stmt.setString(9, auftrag.getKuehler());
            stmt.setString(10, auftrag.getLaufwerk());
            stmt.setString(11, auftrag.getGehaeuse());
            stmt.setInt(12, auftrag.getAnzahl());
            stmt.setDouble(13, auftrag.getPreis());
            stmt.setDate(14, java.sql.Date.valueOf(auftrag.getDatum()));
            stmt.setString(15, auftrag.getStatus());
            stmt.executeUpdate();
            System.out.println("Auftrag erfolgreich in der Datenbank gespeichert.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Speichern des Auftrags.");
        }
    }

    public void deleteAuftragByNummer(int auftragsnummer) {
        String deleteSQL = "DELETE FROM Auftrag WHERE auftragsnummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.setInt(1, auftragsnummer);
            stmt.executeUpdate();
            System.out.println("Auftrag mit Auftragsnummer " + auftragsnummer + " wurde gelöscht.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Löschen des Auftrags.");
        }
    }
  
    public List<Auftrag> getAllAuftraege() {
        List<Auftrag> auftragsListe = new ArrayList<>();
        String query = "SELECT auftragsnummer, kundennummer, datum, status FROM Auftrag";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                auftragsListe.add(new Auftrag(
                        rs.getInt("auftragsnummer"),
                        rs.getInt("kundennummer"),
                        rs.getDate("datum").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auftragsListe;
    }
    
    // --------- Suche-Methoden für Auftraege ---------
    
    public List<Auftrag> searchAuftragByNummer(int auftragsnummer) {
        List<Auftrag> auftragsListe = new ArrayList<>();
        String query = "SELECT * FROM Auftrag WHERE auftragsnummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, auftragsnummer);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Auftrag auftrag = new Auftrag(
                    rs.getInt("kundennummer"),
                    rs.getString("cpu"),
                    rs.getString("ram"),
                    rs.getString("ssd"),
                    rs.getString("hdd"),
                    rs.getString("gpu"),
                    rs.getString("motherboard"),
                    rs.getString("netzteil"),
                    rs.getString("kuehler"),
                    rs.getString("laufwerk"),
                    rs.getString("gehaeuse"),
                    rs.getInt("anzahl"),
                    rs.getDouble("preis"),
                    rs.getDate("datum").toLocalDate(),
                    rs.getString("status")
                );
                // Manuell die Auftragsnummer setzen
                auftrag.setAuftragsnummer(rs.getInt("auftragsnummer"));
                auftragsListe.add(auftrag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auftragsListe;
    }

    public List<Auftrag> getAuftraegeByStatus(String status) {
        List<Auftrag> auftragsListe = new ArrayList<>();
        String query = "SELECT * FROM Auftrag WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Auftrag auftrag = new Auftrag(
                    rs.getInt("kundennummer"),
                    rs.getString("cpu"),
                    rs.getString("ram"),
                    rs.getString("ssd"),
                    rs.getString("hdd"),
                    rs.getString("gpu"),
                    rs.getString("motherboard"),
                    rs.getString("netzteil"),
                    rs.getString("kuehler"),
                    rs.getString("laufwerk"),
                    rs.getString("gehaeuse"),
                    rs.getInt("anzahl"),
                    rs.getDouble("preis"),
                    rs.getDate("datum").toLocalDate(),
                    rs.getString("status")
                );
                // Manuell die Auftragsnummer setzen
                auftrag.setAuftragsnummer(rs.getInt("auftragsnummer"));
                auftragsListe.add(auftrag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auftragsListe;
    }

    
    // --------- Methode zum Aktualisieren des Auftragsstatus ---------
    public void updateAuftragStatus(int auftragsnummer, String neuerStatus) {
        String query = "UPDATE Auftrag SET status = ? WHERE auftragsnummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, neuerStatus);
            stmt.setInt(2, auftragsnummer);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Auftrag> getAuftraegeByKunde(int kundenNummer) {
        List<Auftrag> auftragsListe = new ArrayList<>();
        String query = "SELECT auftragsnummer, datum, status FROM Auftrag WHERE kundennummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, kundenNummer);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                auftragsListe.add(new Auftrag(
                    rs.getInt("auftragsnummer"),
                    kundenNummer,
                    rs.getDate("datum").toLocalDate(),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Abrufen der Aufträge für Kunden: " + kundenNummer);
        }
        return auftragsListe;
    }
}
