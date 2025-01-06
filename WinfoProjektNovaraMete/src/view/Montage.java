package view;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import database.DBManager;
import model.Auftrag;

import java.awt.*;
import java.util.List;

public class Montage extends JFrame {

    private DBManager dbManager;
    private DefaultTableModel model;
    private JTable table;
    private JComboBox<String> statusComboBox;
    private JButton statusAendernButton;
    private JButton detailsButton; 

    // ------- Konstruktor --------
    public Montage(DBManager dbManager) {
        this.dbManager = dbManager;

        setTitle("Montageabteilung - HighSpeed GmbH");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#1E1E1E"));

        initializeComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ------- Initialisierungsmethoden ----------
    
    private void initializeComponents() {
        JLabel highSpeedLabel = new JLabel("HighSpeed GmbH");
        highSpeedLabel.setForeground(Color.WHITE);
        highSpeedLabel.setFont(new Font("Arial", Font.BOLD, 28));
        highSpeedLabel.setBounds(20, 20, highSpeedLabel.getPreferredSize().width, highSpeedLabel.getPreferredSize().height);
        add(highSpeedLabel);

        JLabel madeByLabel = new JLabel("- Made by Novara -");
        madeByLabel.setForeground(Color.LIGHT_GRAY);
        madeByLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        madeByLabel.setBounds(20, 60, 200, 20);
        add(madeByLabel);

        JLabel titleLabel = new JLabel("Montage Abteilung", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBounds(400, 20, 400, 50);
        add(titleLabel);

        initializeSearchPanel();
        initializeTable();
        initializeFilterPanel();
        initializeDetailsButton(); 

    }

    private void initializeSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#1E1E1E"));
        searchPanel.setBounds(50, 100, 1100, 40);
        searchPanel.setLayout(null);
        add(searchPanel);

        JLabel searchLabel = new JLabel("Auftragsnummer:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        searchLabel.setBounds(0, 0, 200, 30);
        searchPanel.add(searchLabel);

        JTextField searchField = new JTextField(20);
        searchField.setBackground(Color.decode("#2E2E2E"));
        searchField.setForeground(Color.LIGHT_GRAY);
        searchField.setCaretColor(Color.WHITE);
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField.setBounds(200, 0, 200, 30);
        searchPanel.add(searchField);

        JButton searchButton = createButton("Suchen", 410, 0, 120, 30);
        searchButton.addActionListener(e -> searchOrder(searchField.getText()));
        searchPanel.add(searchButton);

        JButton resetButton = createButton("Zurücksetzen", 540, 0, 160, 30);
        resetButton.addActionListener(e -> {
            searchField.setText("");
            loadAllOrders();
        });
        searchPanel.add(resetButton);

        statusComboBox = new JComboBox<>(new String[]{"Eingegangen", "In Bearbeitung", "Abgeschlossen"});
        statusComboBox.setBounds(710, 0, 160, 30);
        searchPanel.add(statusComboBox);

        statusAendernButton = createButton("Status ändern", 880, 0, 180, 30);
        statusAendernButton.addActionListener(e -> changeOrderStatus());
        searchPanel.add(statusAendernButton);
    }

    private void initializeTable() {
        String[] columnNames = {"Auftragsnummer", "Eingangsdatum", "Status"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(model);
        table.setBackground(Color.decode("#1E1E1E"));
        table.setForeground(Color.LIGHT_GRAY);
        table.setSelectionBackground(Color.decode("#555555"));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 20));
        table.setGridColor(Color.decode("#333333"));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#333333"));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 22));

        table.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 150, 1100, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));
        add(scrollPane);

        loadAllOrders();
    }


    private void initializeFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(Color.decode("#1E1E1E"));
        filterPanel.setBounds(50, 670, 800, 40);
        filterPanel.setLayout(null);
        add(filterPanel);

        JLabel filterLabel = new JLabel("Filtern nach:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        filterLabel.setBounds(0, 5, 150, 30);
        filterPanel.add(filterLabel);

        JButton eingegangeneAuftraegeButton = createButton("Eingegangene Aufträge", 160, 5, 250, 30);
        eingegangeneAuftraegeButton.addActionListener(e -> filterOrders("Eingegangen"));
        filterPanel.add(eingegangeneAuftraegeButton);

        JButton auftraegeInBearbeitungButton = createButton("Aufträge in Bearbeitung", 420, 5, 240, 30);
        auftraegeInBearbeitungButton.addActionListener(e -> filterOrders("In Bearbeitung"));
        filterPanel.add(auftraegeInBearbeitungButton);
    }
    
    private void initializeDetailsButton() {
        detailsButton = createButton("Details", 900, 670, 150, 40);
        detailsButton.addActionListener(e -> showOrderDetails());
        add(detailsButton);
    }

    // ------------ Detail-Anzeige und Hilfsmethoden ---------------
    private void showOrderDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte einen Auftrag auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int auftragsnummer = (int) model.getValueAt(selectedRow, 0);
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

            JLabel auftragsnummerLabel = createDetailLabel("Auftragsnummer: " + auftrag.getAuftragsnummer());
            JLabel kundennummerLabel = createDetailLabel("Kundennummer: " + auftrag.getKundennummer());
            JLabel datumLabel = createDetailLabel("Datum: " + auftrag.getDatum());
            JLabel statusLabel = createDetailLabel("Status: " + auftrag.getStatus());
            JLabel cpuLabel = createDetailLabel("CPU: " + auftrag.getCpu());
            JLabel ramLabel = createDetailLabel("RAM: " + auftrag.getRam());
            JLabel ssdLabel = createDetailLabel("SSD: " + auftrag.getSsd());
            JLabel hddLabel = createDetailLabel("HDD: " + auftrag.getHdd());
            JLabel gpuLabel = createDetailLabel("GPU: " + auftrag.getGpu());
            JLabel motherboardLabel = createDetailLabel("Motherboard: " + auftrag.getMotherboard());
            JLabel netzteilLabel = createDetailLabel("Netzteil: " + auftrag.getNetzteil());
            JLabel kuehlerLabel = createDetailLabel("Kühler: " + auftrag.getKuehler());
            JLabel laufwerkLabel = createDetailLabel("Laufwerk: " + auftrag.getLaufwerk());
            JLabel gehaeuseLabel = createDetailLabel("Gehäuse: " + auftrag.getGehaeuse());
            JLabel anzahlLabel = createDetailLabel("Anzahl: " + auftrag.getAnzahl());
            JLabel preisLabel = createDetailLabel(String.format("Preis: %.2f EUR", auftrag.getPreis()));

            detailsPanel.add(auftragsnummerLabel);
            detailsPanel.add(kundennummerLabel);
            detailsPanel.add(datumLabel);
            detailsPanel.add(statusLabel);
            detailsPanel.add(cpuLabel);
            detailsPanel.add(ramLabel);
            detailsPanel.add(ssdLabel);
            detailsPanel.add(hddLabel);
            detailsPanel.add(gpuLabel);
            detailsPanel.add(motherboardLabel);
            detailsPanel.add(netzteilLabel);
            detailsPanel.add(kuehlerLabel);
            detailsPanel.add(laufwerkLabel);
            detailsPanel.add(gehaeuseLabel);
            detailsPanel.add(anzahlLabel);
            detailsPanel.add(preisLabel);

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

    // --------- Aufträge suchen, filtern und Status ändern ---------
    
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return label;
    }


    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.RED);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void loadAllOrders() {
        model.setRowCount(0);
        List<Auftrag> auftragsListe = dbManager.getAllAuftraege();
        for (Auftrag auftrag : auftragsListe) {
            model.addRow(new Object[]{auftrag.getAuftragsnummer(), auftrag.getDatum(), auftrag.getStatus()});
        }
    }

    private void searchOrder(String auftragsnummer) {
        model.setRowCount(0);
        try {
            int nummer = Integer.parseInt(auftragsnummer.trim());
            List<Auftrag> auftragsListe = dbManager.searchAuftragByNummer(nummer);
            for (Auftrag auftrag : auftragsListe) {
                model.addRow(new Object[]{auftrag.getAuftragsnummer(), auftrag.getDatum(), auftrag.getStatus()});
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte eine gültige Auftragsnummer eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void filterOrders(String status) {
        model.setRowCount(0);
        List<Auftrag> auftragsListe = dbManager.getAuftraegeByStatus(status);
        for (Auftrag auftrag : auftragsListe) {
            model.addRow(new Object[]{auftrag.getAuftragsnummer(), auftrag.getDatum(), auftrag.getStatus()});
        }
    }

    private void changeOrderStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte einen Auftrag auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int auftragsnummer = (int) model.getValueAt(selectedRow, 0);
        String neuerStatus = (String) statusComboBox.getSelectedItem();

        if (neuerStatus == null || neuerStatus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte einen gültigen Status auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dbManager.updateAuftragStatus(auftragsnummer, neuerStatus);

        if ("Abgeschlossen".equalsIgnoreCase(neuerStatus)) {
            dbManager.deleteAuftragByNummer(auftragsnummer);

            model.removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "Auftrag wurde abgeschlossen und aus der Tabelle entfernt.");
        } else {
            model.setValueAt(neuerStatus, selectedRow, 2);
        }
    }

    // ------------- Status Renderer -----------------
    
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
