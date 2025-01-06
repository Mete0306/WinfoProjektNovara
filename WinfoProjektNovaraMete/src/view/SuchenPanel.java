package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import database.DBManager;
import model.Kunde;

import java.awt.*;
import java.util.List;

public class SuchenPanel extends JPanel {

    private JTextField searchField;
    private JTextField lastnameField;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JButton editButton;
    private JTable table;
    private DefaultTableModel model;
    private DBManager dbManager;

    // ---------- Konstruktor ------------
    
    public SuchenPanel(DBManager dbManager) {
        this.dbManager = dbManager;
        setLayout(null);
        setBackground(Color.decode("#1E1E1E"));

        initializeComponents();

        loadAllCustomers();

        addListeners();
    }

    // ----------- Initialisierungsmethode ---------------
    private void initializeComponents() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.decode("#1E1E1E"));
        actionPanel.setBounds(20, 15, 1100, 50);

        JLabel searchLabel = new JLabel("Kundennummer:");
        searchLabel.setForeground(Color.LIGHT_GRAY);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        actionPanel.add(searchLabel);

        searchField = new JTextField(10);
        styleTextField(searchField);
        actionPanel.add(searchField);

        JLabel lastnameLabel = new JLabel("Nachname:");
        lastnameLabel.setForeground(Color.LIGHT_GRAY);
        lastnameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        actionPanel.add(lastnameLabel);

        lastnameField = new JTextField(10);
        styleTextField(lastnameField);
        actionPanel.add(lastnameField);

        searchButton = createButton("Suchen");
        actionPanel.add(searchButton);

        deleteButton = createButton("Löschen");
        actionPanel.add(deleteButton);

        resetButton = createButton("Zurücksetzen");
        actionPanel.add(resetButton);

        editButton = createButton("Bearbeiten");
        actionPanel.add(editButton);

        add(actionPanel);

        String[] columnNames = {"Kundennummer", "Vorname", "Nachname", "Geburtsdatum"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setBackground(Color.decode("#2E2E2E"));
        table.setForeground(Color.LIGHT_GRAY);
        table.setSelectionBackground(Color.decode("#555555"));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setGridColor(Color.decode("#444444"));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#333333"));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 80, 1060, 480);
        scrollPane.setBackground(Color.decode("#1E1E1E"));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));
        add(scrollPane);
    }

    // ---------- Methoden zum Styling und Erstellen von Buttons ----------------
    
    private void styleTextField(JTextField field) {
        field.setBackground(Color.decode("#333333"));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#FF0000"));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    // ---------- Laden aller Kunden in die Tabelle -----------------
    
    public void loadAllCustomers() {
        model.setRowCount(0);
        try {
            List<Kunde> kundenListe = dbManager.getAllKunden();
            for (Kunde kunde : kundenListe) {
                model.addRow(new Object[]{
                    kunde.getKundenNummer(), 
                    kunde.getVorname(),
                    kunde.getNachname(),
                    kunde.getGeburtstag()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Kunden: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --------- Hinzufügen von Event-Listenern für Buttons ---------
    
    private void addListeners() {
    	searchButton.addActionListener(e -> {
            String kundennummer = searchField.getText().trim();
            String nachname = lastnameField.getText().trim();

            if (!kundennummer.isEmpty() && !kundennummer.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Kundennummer darf nur Zahlen enthalten.", "Fehler", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                model.setRowCount(0);
                List<Kunde> results;
                if (!kundennummer.isEmpty() && !nachname.isEmpty()) {
                    results = dbManager.searchKundeByNummerAndNachname(Integer.parseInt(kundennummer), nachname);
                } else if (!kundennummer.isEmpty()) {
                    results = dbManager.searchKundeByNummer(Integer.parseInt(kundennummer));
                } else if (!nachname.isEmpty()) {
                    results = dbManager.searchKundeByNachname(nachname);
                } else {
                    JOptionPane.showMessageDialog(this, "Bitte geben Sie eine Kundennummer oder einen Nachnamen ein.", "Fehlende Eingaben", JOptionPane.WARNING_MESSAGE);
                    loadAllCustomers();
                    return;
                }

                for (Kunde kunde : results) {
                    model.addRow(new Object[]{
                        kunde.getKundenNummer(),
                        kunde.getVorname(),
                        kunde.getNachname(),
                        kunde.getGeburtstag()
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Suche: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

    	deleteButton.addActionListener(e -> {
    	    int selectedRow = table.getSelectedRow();
    	    if (selectedRow == -1) {
    	        JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Kunden aus der Tabelle aus.", "Fehler", JOptionPane.WARNING_MESSAGE);
    	        return;
    	    }
    	    try {
    	        int kundennummer = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
    	        dbManager.deleteKundeByNummer(kundennummer);
    	        model.removeRow(selectedRow);
    	    } catch (Exception ex) {
    	        JOptionPane.showMessageDialog(this, "Fehler beim Löschen: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
    	    }
    	});


        resetButton.addActionListener(e -> {
            searchField.setText("");
            lastnameField.setText("");
            loadAllCustomers();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Kunden aus der Tabelle aus.", "Fehler", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int kundennummer = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                Kunde kunde = dbManager.getKundeByNummer(kundennummer);
                if (kunde != null) {
                    new KundeBearbeitenFrame(dbManager, kunde);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Bearbeiten: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
