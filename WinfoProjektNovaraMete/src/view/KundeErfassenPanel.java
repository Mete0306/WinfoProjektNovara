package view;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import database.DBManager;
import model.Kunde;

import java.awt.*;
import java.awt.event.ActionListener;

public class KundeErfassenPanel extends JPanel {

    // --------- Komponenten für persönliche Daten ---------
	private ButtonGroup anredeGroup;
    private JRadioButton herrRadioButton;
    private JRadioButton frauRadioButton;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JTextField geburtstagTagField;
    private JTextField geburtstagMonatField;
    private JTextField geburtstagJahrField;

    // --------- Komponenten für Kontaktdaten ---------
    private JTextField emailField;
    private JTextField telefonnummerField;

    // --------- Komponenten für Adressen ---------
    private JTextField adresseStrasseField;
    private JTextField adresseHausnummerField;
    private JTextField adressePlzField;
    private JTextField adresseOrtField;
    private JTextField lieferadresseStrasseField;
    private JTextField lieferadresseHausnummerField;
    private JTextField lieferadressePlzField;
    private JTextField lieferadresseOrtField;
    private JCheckBox gleicheAdresseCheckbox;

    // --------- Komponenten für Kundenkategorie ---------
    private ButtonGroup kategorieGroup;;
    private JRadioButton privatkundeRadioButton;
    private JRadioButton geschaeftskundeRadioButton;

    // --------- Sonstige Komponenten ---------
    private JButton speichernButton;
    private SuchenPanel suchenPanel;
    private DBManager dbManager;

    // --------- Konstruktor ---------
    public KundeErfassenPanel(DBManager dbManager, SuchenPanel suchenPanel) {
        this.dbManager = dbManager;
        this.suchenPanel = suchenPanel;

        setLayout(null);
        setBackground(Color.decode("#1E1E1E"));

        initializePanels();

        addCheckboxListener();
        addSpeichernButtonListener();
    }

    // --------- Initialisierung der Panels ---------
    private void initializePanels() {
        JPanel personalPanel = createSectionPanel("Persönliche Daten", 30, 20, 620, 200);
        herrRadioButton = new JRadioButton("Herr");
        frauRadioButton = new JRadioButton("Frau");

        anredeGroup = new ButtonGroup();
        anredeGroup.add(herrRadioButton);
        anredeGroup.add(frauRadioButton);

        addLabeledRadioButtons(personalPanel, herrRadioButton, frauRadioButton, 30, 20);
        vornameField = addLabeledTextField(personalPanel, "Vorname:", 40, 60);
        nachnameField = addLabeledTextField(personalPanel, "Nachname:", 40, 100);
        geburtstagTagField = new JTextField("TT");
        geburtstagMonatField = new JTextField("MM");
        geburtstagJahrField = new JTextField("YYYY");
        addBirthdayFields(personalPanel, 30);
        add(personalPanel);

        JPanel kontaktPanel = createSectionPanel("Kontaktdaten", 30, 240, 620, 150);
        emailField = addLabeledTextField(kontaktPanel, "E-Mail:", 40, 30);
        telefonnummerField = addLabeledTextField(kontaktPanel, "Telefonnummer:", 40, 70);
        add(kontaktPanel);

        JPanel kategoriePanel = createSectionPanel("Kundenkategorie", 30, 420, 620, 100);
        privatkundeRadioButton = new JRadioButton("Privatkunde");
        geschaeftskundeRadioButton = new JRadioButton("Geschäftskunde");

        kategorieGroup = new ButtonGroup();
        kategorieGroup.add(privatkundeRadioButton);
        kategorieGroup.add(geschaeftskundeRadioButton);

        addLabeledRadioButtons(kategoriePanel, privatkundeRadioButton, geschaeftskundeRadioButton, 30, 20);
        add(kategoriePanel);

        JPanel adressePanel = createSectionPanel("Adresse", 700, 20, 600, 200);
        adresseStrasseField = addLabeledTextField(adressePanel, "Straße:", 40, 30);
        adresseHausnummerField = addLabeledTextField(adressePanel, "Hausnummer:", 40, 70);
        adressePlzField = addLabeledTextField(adressePanel, "PLZ:", 40, 110);
        adresseOrtField = addLabeledTextField(adressePanel, "Ort:", 40, 150);
        add(adressePanel);

        JPanel lieferadressePanel = createSectionPanel("Lieferadresse", 700, 240, 600, 240);
        gleicheAdresseCheckbox = new JCheckBox("Lieferadresse entspricht der Standardadresse");
        gleicheAdresseCheckbox.setBackground(Color.decode("#1E1E1E"));
        gleicheAdresseCheckbox.setForeground(Color.LIGHT_GRAY);
        gleicheAdresseCheckbox.setFont(new Font("Arial", Font.PLAIN, 16));
        gleicheAdresseCheckbox.setBounds(10, 20, 400, 30);
        lieferadressePanel.add(gleicheAdresseCheckbox);
        lieferadresseStrasseField = addLabeledTextField(lieferadressePanel, "Straße:", 40, 70);
        lieferadresseHausnummerField = addLabeledTextField(lieferadressePanel, "Hausnummer:", 40, 110);
        lieferadressePlzField = addLabeledTextField(lieferadressePanel, "PLZ:", 40, 150);
        lieferadresseOrtField = addLabeledTextField(lieferadressePanel, "Ort:", 40, 190);
        add(lieferadressePanel);

        speichernButton = new JButton("Speichern");
        speichernButton.setBounds(700, 500, 150, 40);
        speichernButton.setBackground(Color.RED);
        speichernButton.setForeground(Color.WHITE);
        speichernButton.setFocusPainted(false);
        speichernButton.setOpaque(true);
        speichernButton.setBorderPainted(false);
        add(speichernButton);
    }

    //------- Actionlistener für die Checkbox und den Speichern Button ----------
    
    private void addCheckboxListener() {
        gleicheAdresseCheckbox.addActionListener(e -> {
            boolean selected = gleicheAdresseCheckbox.isSelected();
            if (selected) {
                copyAddressFields();
                setDeliveryAddressEditable(false);
            } else {
                setDeliveryAddressEditable(true);
                clearDeliveryAddressFields();
            }
        });
    }

    private void addSpeichernButtonListener() {
        speichernButton.addActionListener(e -> speichernKunde());
    }

    //-------- Hilfsmethoden für das Layout und das Hinzufügen von Komponenten ---------------
    private JPanel createSectionPanel(String title, int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#1E1E1E"));
        panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.LIGHT_GRAY));
        panel.setBounds(x, y, width, height);
        panel.setLayout(null);
        return panel;
    }

    private JTextField addLabeledTextField(JPanel panel, String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(x, y, 130, 30);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBackground(Color.decode("#2E2E2E"));
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setCaretColor(Color.WHITE);
        textField.setBounds(x + 145, y, 200, 30);
        panel.add(textField);
        return textField;
    }

    private void addLabeledRadioButtons(JPanel panel, JRadioButton button1, JRadioButton button2, int x, int y) {
        button1.setForeground(Color.LIGHT_GRAY);
        button1.setBackground(Color.decode("#1E1E1E"));
        button1.setFont(new Font("Arial", Font.PLAIN, 18));
        button1.setBounds(x, y, 150, 30);

        button2.setForeground(Color.LIGHT_GRAY);
        button2.setBackground(Color.decode("#1E1E1E"));
        button2.setFont(new Font("Arial", Font.PLAIN, 18));
        button2.setBounds(x + 170, y, 170, 30);

        ButtonGroup group = new ButtonGroup();
        group.add(button1);
        group.add(button2);

        panel.add(button1);
        panel.add(button2);
    }

    private void addBirthdayFields(JPanel panel, int xOffset) {
        JLabel geburtstagLabel = new JLabel("Geburtstag:");
        geburtstagLabel.setForeground(Color.LIGHT_GRAY);
        geburtstagLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        geburtstagLabel.setBounds(xOffset + 10, 140, 100, 30);
        panel.add(geburtstagLabel);

        geburtstagTagField.setBounds(xOffset + 160, 140, 50, 30);
        geburtstagMonatField.setBounds(xOffset + 220, 140, 50, 30);
        geburtstagJahrField.setBounds(xOffset + 280, 140, 70, 30);

        geburtstagTagField.setBackground(Color.decode("#2E2E2E"));
        geburtstagMonatField.setBackground(Color.decode("#2E2E2E"));
        geburtstagJahrField.setBackground(Color.decode("#2E2E2E"));

        geburtstagTagField.setForeground(Color.LIGHT_GRAY);
        geburtstagMonatField.setForeground(Color.LIGHT_GRAY);
        geburtstagJahrField.setForeground(Color.LIGHT_GRAY);

        panel.add(geburtstagTagField);
        panel.add(geburtstagMonatField);
        panel.add(geburtstagJahrField);
    }

    // ------- Logik zur Adressübernahme und Validierung -------

    private void copyAddressFields() {
        lieferadresseStrasseField.setText(adresseStrasseField.getText());
        lieferadresseHausnummerField.setText(adresseHausnummerField.getText());
        lieferadressePlzField.setText(adressePlzField.getText());
        lieferadresseOrtField.setText(adresseOrtField.getText());
    }

    private void setDeliveryAddressEditable(boolean editable) {
        lieferadresseStrasseField.setEditable(editable);
        lieferadresseHausnummerField.setEditable(editable);
        lieferadressePlzField.setEditable(editable);
        lieferadresseOrtField.setEditable(editable);
    }

    private void clearDeliveryAddressFields() {
        lieferadresseStrasseField.setText("");
        lieferadresseHausnummerField.setText("");
        lieferadressePlzField.setText("");
        lieferadresseOrtField.setText("");
    }

    private boolean areFieldsEmpty() {
        return getVorname().isEmpty() ||
               getNachname().isEmpty() ||
               getGeburtstag().isEmpty() ||
               getEmail().isEmpty() ||
               getTelefonnummer().isEmpty() ||
               getAdresseStrasse().isEmpty() ||
               getAdresseHausnummer().isEmpty() ||
               getAdressePlz().isEmpty() ||
               getAdresseOrt().isEmpty() ||
               getLieferadresseStrasse().isEmpty() ||
               getLieferadresseHausnummer().isEmpty() ||
               getLieferadressePlz().isEmpty() ||
               getLieferadresseOrt().isEmpty() ||
               getKundenkategorie().isEmpty();
    }

    private Kunde createKundeFromPanel() {
        return new Kunde(
            getAnrede(),
            getVorname(),
            getNachname(),
            getGeburtstag(),
            getEmail(),
            getTelefonnummer(),
            getAdresseStrasse(),
            getAdresseHausnummer(),
            getAdressePlz(),
            getAdresseOrt(),
            getLieferadresseStrasse(),
            getLieferadresseHausnummer(),
            getLieferadressePlz(),
            getLieferadresseOrt(),
            getKundenkategorie()
        );
    }

    // ------- Kunde speichern ---------

    private void speichernKunde() {
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehlende Informationen", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validierungen

        if (!isValidStrasse(getAdresseStrasse())) {
            JOptionPane.showMessageDialog(this, "Der Straßenname der Hauptadresse darf keine Zahlen enthalten.", "Ungültiger Straßenname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!getAdressePlz().matches("\\d{5}")) {
            JOptionPane.showMessageDialog(this, "Die PLZ der Hauptadresse muss aus genau 5 Ziffern bestehen.", "Ungültige PLZ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidHausnummer(getAdresseHausnummer())) {
            JOptionPane.showMessageDialog(this, "Die Hausnummer der Hauptadresse ist ungültig. Sie sollte aus Zahlen bestehen, optional gefolgt von Buchstaben (z.B. '12A').", "Ungültige Hausnummer", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidOrt(getAdresseOrt())) {
            JOptionPane.showMessageDialog(this, "Der Ortsname der Hauptadresse darf keine Zahlen enthalten.", "Ungültiger Ortsname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!getTelefonnummer().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Die Telefonnummer darf nur Ziffern enthalten.", "Ungültige Telefonnummer", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidEmail(getEmail())) {
            JOptionPane.showMessageDialog(this, "Die E-Mail-Adresse ist ungültig.", "Ungültige E-Mail-Adresse", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isValidDate(getGeburtstag())) {
            JOptionPane.showMessageDialog(this, "Das Geburtsdatum ist ungültig oder liegt in der Zukunft.", "Ungültiges Geburtsdatum", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isValidName(getVorname())) {
            JOptionPane.showMessageDialog(this, "Der Vorname darf keine Zahlen enthalten und muss gültig sein.", "Ungültiger Vorname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidName(getNachname())) {
            JOptionPane.showMessageDialog(this, "Der Nachname darf keine Zahlen enthalten und muss gültig sein.", "Ungültiger Nachname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!gleicheAdresseCheckbox.isSelected()) {
            if (!isValidStrasse(getLieferadresseStrasse())) {
                JOptionPane.showMessageDialog(this, "Der Straßenname der Lieferadresse darf keine Zahlen enthalten.", "Ungültiger Straßenname", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!getLieferadressePlz().matches("\\d{5}")) {
                JOptionPane.showMessageDialog(this, "Die PLZ der Lieferadresse muss aus genau 5 Ziffern bestehen.", "Ungültige PLZ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidHausnummer(getLieferadresseHausnummer())) {
                JOptionPane.showMessageDialog(this, "Die Hausnummer der Lieferadresse ist ungültig. Sie sollte aus Zahlen bestehen, optional gefolgt von Buchstaben (z.B. '12A').", "Ungültige Hausnummer", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidOrt(getLieferadresseOrt())) {
                JOptionPane.showMessageDialog(this, "Der Ortsname der Lieferadresse darf keine Zahlen enthalten.", "Ungültiger Ortsname", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Kunde kunde = createKundeFromPanel();
        dbManager.insertKunde(kunde);

        JOptionPane.showMessageDialog(this, "Kunde erfolgreich gespeichert!");

        if (suchenPanel != null) {
            suchenPanel.loadAllCustomers();
        }

        resetFields();
    }

    private void resetFields() {
        this.setVorname("");
        this.setNachname("");
        this.setGeburtstag("TT.MM.YYYY");
        this.setEmail("");
        this.setTelefonnummer("");
        this.setAdresseStrasse("");
        this.setAdresseHausnummer("");
        this.setAdressePlz("");
        this.setAdresseOrt("");
        this.setLieferadresseStrasse("");
        this.setLieferadresseHausnummer("");
        this.setLieferadressePlz("");
        this.setLieferadresseOrt("");

        this.gleicheAdresseCheckbox.setSelected(false);

        this.anredeGroup.clearSelection();
        this.kategorieGroup.clearSelection();
    }
    
    // --------- Getter für die Daten ---------
    
    public String getAnrede() { return herrRadioButton.isSelected() ? "Herr" : "Frau"; }
    public String getVorname() { return vornameField.getText(); }
    public String getNachname() { return nachnameField.getText(); }
    public String getGeburtstag() { return geburtstagTagField.getText() + "." + geburtstagMonatField.getText() + "." + geburtstagJahrField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public String getTelefonnummer() { return telefonnummerField.getText(); }
    public String getAdresseStrasse() { return adresseStrasseField.getText(); }
    public String getAdresseHausnummer() { return adresseHausnummerField.getText(); }
    public String getAdressePlz() { return adressePlzField.getText(); }
    public String getAdresseOrt() { return adresseOrtField.getText(); }
    public String getLieferadresseStrasse() { return lieferadresseStrasseField.getText(); }
    public String getLieferadresseHausnummer() { return lieferadresseHausnummerField.getText(); }
    public String getLieferadressePlz() { return lieferadressePlzField.getText(); }
    public String getLieferadresseOrt() { return lieferadresseOrtField.getText(); }
    public String getKundenkategorie() { return privatkundeRadioButton.isSelected() ? "Privatkunde" : "Geschäftskunde"; }

    // --------- Setter für Datenbefüllung ---------
    
    public void setVorname(String vorname) { vornameField.setText(vorname); }
    public void setNachname(String nachname) { nachnameField.setText(nachname); }
    public void setGeburtstag(String geburtstag) {
        String[] parts = geburtstag.split("\\.");
        geburtstagTagField.setText(parts[0]);
        geburtstagMonatField.setText(parts[1]);
        geburtstagJahrField.setText(parts[2]);
    }
    public void setEmail(String email) { emailField.setText(email); }
    public void setTelefonnummer(String telefonnummer) { telefonnummerField.setText(telefonnummer); }
    public void setAdresseStrasse(String strasse) { adresseStrasseField.setText(strasse); }
    public void setAdresseHausnummer(String hausnummer) { adresseHausnummerField.setText(hausnummer); }
    public void setAdressePlz(String plz) { adressePlzField.setText(plz); }
    public void setAdresseOrt(String ort) { adresseOrtField.setText(ort); }
    public void setLieferadresseStrasse(String strasse) { lieferadresseStrasseField.setText(strasse); }
    public void setLieferadresseHausnummer(String hausnummer) { lieferadresseHausnummerField.setText(hausnummer); }
    public void setLieferadressePlz(String plz) { lieferadressePlzField.setText(plz); }
    public void setLieferadresseOrt(String ort) { lieferadresseOrtField.setText(ort); }
    public void setKundenkategorie(String kategorie) {
        if (kategorie.equals("Privatkunde")) {
            privatkundeRadioButton.setSelected(true);
        } else {
            geschaeftskundeRadioButton.setSelected(true);
        }
    }

    // ----------- Daten validieren ----------------
    
    private boolean isValidName(String name) {
        String nameRegex = "^[a-zA-ZäöüÄÖÜß\\s\\-'´]+$";
        return name.matches(nameRegex);
    }
    
    private boolean isValidDate(String date) {
        try {
            String[] parts = date.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            java.time.LocalDate geburtsdatum = java.time.LocalDate.of(year, month, day);
            java.time.LocalDate today = java.time.LocalDate.now();

            return !geburtsdatum.isAfter(today);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isValidOrt(String ort) {
        String ortRegex = "^[a-zA-ZäöüÄÖÜß\\s\\-]+$";
        return ort.matches(ortRegex);
    }
    
    private boolean isValidStrasse(String strasse) {
        String strasseRegex = "^[a-zA-ZäöüÄÖÜß\\s\\-]+$";
        return strasse.matches(strasseRegex);
    }

    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    private boolean isValidHausnummer(String hausnummer) {
        String hausnummerRegex = "^\\d+([a-zA-Z]|[-/\\d])*?$";
        return hausnummer.matches(hausnummerRegex);
    } 
    
}
