package view;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import database.DBManager;
import model.Auftrag;
import model.Kunde;

import java.awt.*;
import java.util.List;


public class KundeBearbeitenFrame extends JFrame {

    // --------- GUI-Komponenten ---------
    private JRadioButton herrRadioButton, frauRadioButton;
    private JTextField vornameField, nachnameField, emailField, telefonnummerField;
    private JTextField adresseStrasseField, adresseHausnummerField, adressePlzField, adresseOrtField;
    private JTextField lieferadresseStrasseField, lieferadresseHausnummerField, lieferadressePlzField, lieferadresseOrtField;
    private JCheckBox gleicheAdresseCheckbox;
    private JButton speichernButton;
    private JButton detailsButton;
    private JTable auftragsTable;
    private DefaultTableModel auftragsTableModel;

    // --------- Daten ---------
    private Kunde kunde;
    private DBManager dbManager;

    // --------- Konstruktor ---------
    public KundeBearbeitenFrame(DBManager dbManager, Kunde kunde) {
        this.dbManager = dbManager;
        this.kunde = kunde;

        setTitle("Kunde Bearbeiten");
        setSize(1400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#1E1E1E"));

        initializePanels();

        initializeAuftragsTable();

        populateFields();

        gleicheAdresseCheckbox.addActionListener(e -> handleCheckbox());

        speichernButton.addActionListener(e -> speichernKunde());
        detailsButton.addActionListener(e -> showOrderDetails());

        setLocationRelativeTo(null); 
        setVisible(true); 
    }
    
    // --------- Initialisierungsmethoden ---------
    

    private void initializePanels() {
        JPanel personalPanel = createSectionPanel("Persönliche Daten", 30, 20, 620, 200);
        herrRadioButton = new JRadioButton("Herr");
        frauRadioButton = new JRadioButton("Frau");
        ButtonGroup anredeGroup = new ButtonGroup();
        anredeGroup.add(herrRadioButton);
        anredeGroup.add(frauRadioButton);
        addLabeledRadioButtons(personalPanel, herrRadioButton, frauRadioButton, 30, 20);
        vornameField = addLabeledTextField(personalPanel, "Vorname:", 40, 60);
        nachnameField = addLabeledTextField(personalPanel, "Nachname:", 40, 100);
        add(personalPanel);

        JPanel kontaktPanel = createSectionPanel("Kontaktdaten", 30, 240, 620, 150);
        emailField = addLabeledTextField(kontaktPanel, "E-Mail:", 40, 30);
        telefonnummerField = addLabeledTextField(kontaktPanel, "Telefonnummer:", 40, 70);
        add(kontaktPanel);

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
        speichernButton.setBounds(900, 500, 150, 40);
        speichernButton.setBackground(Color.RED);
        speichernButton.setForeground(Color.WHITE);
        speichernButton.setFocusPainted(false);
        speichernButton.setOpaque(true);
        speichernButton.setBorderPainted(false);
        add(speichernButton);

        detailsButton = new JButton("Details");
        detailsButton.setBounds(700, 500, 150, 40); 
        detailsButton.setBackground(Color.RED);
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.setOpaque(true);
        detailsButton.setBorderPainted(false);
        add(detailsButton);
    }

    private void initializeAuftragsTable() {
        String[] columnNames = {"Auftragsnummer", "Eingangsdatum", "Status"};
        auftragsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        auftragsTable = new JTable(auftragsTableModel);
        auftragsTable.setBackground(Color.decode("#1E1E1E"));
        auftragsTable.setForeground(Color.LIGHT_GRAY);
        auftragsTable.setSelectionBackground(Color.decode("#555555"));
        auftragsTable.setSelectionForeground(Color.WHITE);
        auftragsTable.setRowHeight(30);
        auftragsTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JTableHeader header = auftragsTable.getTableHeader();
        header.setBackground(Color.decode("#333333"));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        auftragsTable.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(auftragsTable);
        scrollPane.setBounds(30, 400, 600, 200);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));
        add(scrollPane);

        loadKundenspezifischeAuftraege();
    }
    
    // --------- Event-Handler ---------

    private void handleCheckbox() {
        boolean selected = gleicheAdresseCheckbox.isSelected();
        lieferadresseStrasseField.setEditable(!selected);
        lieferadresseHausnummerField.setEditable(!selected);
        lieferadressePlzField.setEditable(!selected);
        lieferadresseOrtField.setEditable(!selected);
        if (selected) {
            lieferadresseStrasseField.setText(adresseStrasseField.getText());
            lieferadresseHausnummerField.setText(adresseHausnummerField.getText());
            lieferadressePlzField.setText(adressePlzField.getText());
            lieferadresseOrtField.setText(adresseOrtField.getText());
        }
    }
    
    private void speichernKunde() {
        if (vornameField.getText().trim().isEmpty() || nachnameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() || telefonnummerField.getText().trim().isEmpty() ||
            adresseStrasseField.getText().trim().isEmpty() || adresseHausnummerField.getText().trim().isEmpty() ||
            adressePlzField.getText().trim().isEmpty() || adresseOrtField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder der Hauptadresse aus.", "Fehlende Informationen", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String geschlecht = herrRadioButton.isSelected() ? "Herr" : "Frau";

        if (!isValidName(vornameField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Der Vorname darf keine Zahlen enthalten und muss gültig sein.", "Ungültiger Vorname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidName(nachnameField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Der Nachname darf keine Zahlen enthalten und muss gültig sein.", "Ungültiger Nachname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidOrt(adresseOrtField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Der Ortsname der Hauptadresse darf keine Zahlen enthalten.", "Ungültiger Ortsname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidStrasse(adresseStrasseField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Der Straßenname der Hauptadresse darf keine Zahlen enthalten.", "Ungültiger Straßenname", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!adressePlzField.getText().matches("\\d{5}")) {
            JOptionPane.showMessageDialog(this, "Die PLZ der Hauptadresse muss aus genau 5 Ziffern bestehen.", "Ungültige PLZ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidHausnummer(adresseHausnummerField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Die Hausnummer der Hauptadresse ist ungültig. Sie sollte aus Zahlen bestehen, optional gefolgt von Buchstaben (z.B. '12A').", "Ungültige Hausnummer", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!telefonnummerField.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Die Telefonnummer darf nur Ziffern enthalten.", "Ungültige Telefonnummer", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidEmail(emailField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Die E-Mail-Adresse ist ungültig.", "Ungültige E-Mail-Adresse", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!gleicheAdresseCheckbox.isSelected()) {
            if (lieferadresseStrasseField.getText().trim().isEmpty() || lieferadresseHausnummerField.getText().trim().isEmpty() ||
                lieferadressePlzField.getText().trim().isEmpty() || lieferadresseOrtField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder der Lieferadresse aus.", "Fehlende Informationen", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidStrasse(lieferadresseStrasseField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Der Straßenname der Lieferadresse darf keine Zahlen enthalten.", "Ungültiger Straßenname", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!lieferadressePlzField.getText().matches("\\d{5}")) {
                JOptionPane.showMessageDialog(this, "Die PLZ der Lieferadresse muss aus genau 5 Ziffern bestehen.", "Ungültige PLZ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidHausnummer(lieferadresseHausnummerField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Die Hausnummer der Lieferadresse ist ungültig.", "Ungültige Hausnummer", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidOrt(lieferadresseOrtField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Der Ortsname der Lieferadresse darf keine Zahlen enthalten.", "Ungültiger Ortsname", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        kunde.setAnrede(geschlecht); 
        kunde.setVorname(vornameField.getText().trim());
        kunde.setNachname(nachnameField.getText().trim());
        kunde.setEmail(emailField.getText().trim());
        kunde.setTelefonnummer(telefonnummerField.getText().trim());
        kunde.setAdresseStrasse(adresseStrasseField.getText().trim());
        kunde.setAdresseHausnummer(adresseHausnummerField.getText().trim());
        kunde.setAdressePlz(adressePlzField.getText().trim());
        kunde.setAdresseOrt(adresseOrtField.getText().trim());
        kunde.setLieferadresseStrasse(lieferadresseStrasseField.getText().trim());
        kunde.setLieferadresseHausnummer(lieferadresseHausnummerField.getText().trim());
        kunde.setLieferadressePlz(lieferadressePlzField.getText().trim());
        kunde.setLieferadresseOrt(lieferadresseOrtField.getText().trim());

        dbManager.updateKunde(kunde);
        JOptionPane.showMessageDialog(this, "Kunde erfolgreich aktualisiert!");
        dispose();
    }

    private boolean isValidName(String name) {
        String nameRegex = "^[a-zA-ZäöüÄÖÜß\\s\\-'´]+$";
        return name.matches(nameRegex);
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
        String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[a-zA-Z]{2,3}$";
        return email.matches(emailRegex);
    }


    private boolean isValidHausnummer(String hausnummer) {
        String hausnummerRegex = "^\\d+([a-zA-Z]|[-/\\d])*?$";
        return hausnummer.matches(hausnummerRegex);
    }


    private void showOrderDetails() {
        int selectedRow = auftragsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte einen Auftrag auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int auftragsnummer = (int) auftragsTableModel.getValueAt(selectedRow, 0);
        List<Auftrag> auftragsDetails = dbManager.searchAuftragByNummer(auftragsnummer);

        if (!auftragsDetails.isEmpty()) {
            Auftrag auftrag = auftragsDetails.get(0);

            JDialog detailsDialog = new JDialog(this, "Auftragsdetails", true);
            detailsDialog.setSize(600, 500);
            detailsDialog.setLayout(new BorderLayout());
            detailsDialog.setLocationRelativeTo(this);

            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBackground(Color.decode("#1E1E1E"));
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            detailsPanel.add(createDetailLabel("Auftragsnummer: " + auftrag.getAuftragsnummer()));
            detailsPanel.add(createDetailLabel("Kundennummer: " + auftrag.getKundennummer()));
            detailsPanel.add(createDetailLabel("Datum: " + auftrag.getDatum()));
            detailsPanel.add(createDetailLabel("Status: " + auftrag.getStatus()));
            detailsPanel.add(createDetailLabel("CPU: " + auftrag.getCpu()));
            detailsPanel.add(createDetailLabel("RAM: " + auftrag.getRam()));
            detailsPanel.add(createDetailLabel("SSD: " + auftrag.getSsd()));
            detailsPanel.add(createDetailLabel("HDD: " + auftrag.getHdd()));
            detailsPanel.add(createDetailLabel("GPU: " + auftrag.getGpu()));
            detailsPanel.add(createDetailLabel("Motherboard: " + auftrag.getMotherboard()));
            detailsPanel.add(createDetailLabel("Netzteil: " + auftrag.getNetzteil()));
            detailsPanel.add(createDetailLabel("Kühler: " + auftrag.getKuehler()));
            detailsPanel.add(createDetailLabel("Laufwerk: " + auftrag.getLaufwerk()));
            detailsPanel.add(createDetailLabel("Gehäuse: " + auftrag.getGehaeuse()));
            detailsPanel.add(createDetailLabel("Anzahl: " + auftrag.getAnzahl()));
            detailsPanel.add(createDetailLabel(String.format("Preis: %.2f EUR", auftrag.getPreis())));

            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(Color.decode("#1E1E1E"));
            detailsDialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("Schließen");
            closeButton.setBackground(Color.RED);
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Arial", Font.BOLD, 16));
            closeButton.setFocusPainted(false);
            closeButton.setOpaque(true);
            closeButton.setBorderPainted(false);
            closeButton.addActionListener(e -> detailsDialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.decode("#1E1E1E"));
            buttonPanel.add(closeButton);

            detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

            detailsDialog.setVisible(true);
        }
    }
    
    // --------- GUI-Hilfsmethoden ---------
    
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return label;
    }
    
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

    // --------- Datenhandling ---------

    private void populateFields() {
        if (kunde.getAnrede().equals("Herr")) {
            herrRadioButton.setSelected(true);
        } else {
            frauRadioButton.setSelected(true);
        }
        vornameField.setText(kunde.getVorname());
        nachnameField.setText(kunde.getNachname());
        emailField.setText(kunde.getEmail());
        telefonnummerField.setText(kunde.getTelefonnummer());
        adresseStrasseField.setText(kunde.getAdresseStrasse());
        adresseHausnummerField.setText(kunde.getAdresseHausnummer());
        adressePlzField.setText(kunde.getAdressePlz());
        adresseOrtField.setText(kunde.getAdresseOrt());
        lieferadresseStrasseField.setText(kunde.getLieferadresseStrasse());
        lieferadresseHausnummerField.setText(kunde.getLieferadresseHausnummer());
        lieferadressePlzField.setText(kunde.getLieferadressePlz());
        lieferadresseOrtField.setText(kunde.getLieferadresseOrt());
    }

    private void loadKundenspezifischeAuftraege() {
        auftragsTableModel.setRowCount(0);
        List<Auftrag> auftraege = dbManager.getAuftraegeByKunde(kunde.getKundenNummer());
        for (Auftrag auftrag : auftraege) {
            auftragsTableModel.addRow(new Object[]{auftrag.getAuftragsnummer(), auftrag.getDatum(), auftrag.getStatus()});
        }
    }

    // --------- Renderer ---------

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = (String) value;
            switch (status) {
                case "Eingegangen":
                    cell.setBackground(Color.RED);
                    break;
                case "In Bearbeitung":
                    cell.setBackground(Color.ORANGE);
                    break;
                case "Abgeschlossen":
                    cell.setBackground(Color.GREEN);
                    break;
                default:
                    cell.setBackground(table.getBackground());
                    break;
            }

            cell.setForeground(Color.WHITE);

            if (isSelected) {
                cell.setBackground(cell.getBackground().darker());
            }

            return cell;
        }
    }
}
