package view;
import javax.swing.*;

import database.DBManager;
import model.Auftrag;
import model.Kunde;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;


public class AuftragErstellenPanel extends JPanel {

    // --------- Komponenten ---------
    private JTextField kundenNummerField;
    private JTextField preisField;
    private JTextField anzahlField;
    private JButton erstellenButton;
    private JLabel gehaeuseImageLabel;
    private JTextField nachnameField;
    private JButton suchenButton;

    // --------- Dropdown-Menüs ---------
    private JComboBox<String> cpuComboBox;
    private JComboBox<String> ramComboBox;
    private JComboBox<String> ssdComboBox;
    private JComboBox<String> hddComboBox;
    private JComboBox<String> gpuComboBox;
    private JComboBox<String> motherboardComboBox;
    private JComboBox<String> netzteilComboBox;
    private JComboBox<String> kuehlerComboBox;
    private JComboBox<String> laufwerkComboBox;
    private JComboBox<String> gehaeuseComboBox;

    // --------- Preis-Maps ---------
    private Map<String, Double> cpuPreise;
    private Map<String, Double> ramPreise;
    private Map<String, Double> ssdPreise;
    private Map<String, Double> hddPreise;
    private Map<String, Double> gpuPreise;
    private Map<String, Double> motherboardPreise;
    private Map<String, Double> netzteilPreise;
    private Map<String, Double> kuehlerPreise;
    private Map<String, Double> laufwerkPreise;
    private Map<String, Double> gehaeusePreise;

    private DBManager dbManager;

    // --------- Konstruktor ---------
    public AuftragErstellenPanel(DBManager dbManager) {
        this.dbManager = dbManager;
        setLayout(null);
        setBackground(Color.decode("#1E1E1E"));

        initializePrices();
        initializeComponents();
        addActionListeners();
    }

    // --------- Initialisierung ---------
    private void initializeComponents() {
        JLabel kundenNummerLabel = new JLabel("Kundennummer:");
        kundenNummerLabel.setForeground(Color.LIGHT_GRAY);
        kundenNummerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        kundenNummerLabel.setBounds(30, 20, 150, 30);
        add(kundenNummerLabel);

        kundenNummerField = createTextField(180, 20, 200, 30);
        add(kundenNummerField);

        JLabel komponentenLabel = new JLabel("Komponenten:");
        komponentenLabel.setForeground(Color.LIGHT_GRAY);
        komponentenLabel.setFont(new Font("Arial", Font.BOLD, 18));
        komponentenLabel.setBounds(30, 70, 200, 30);
        add(komponentenLabel);

        cpuComboBox = createComboBox("CPU:", 30, 120, cpuPreise.keySet().toArray(new String[0]));
        ramComboBox = createComboBox("RAM:", 30, 170, ramPreise.keySet().toArray(new String[0]));
        ssdComboBox = createComboBox("SSD:", 30, 220, ssdPreise.keySet().toArray(new String[0]));
        hddComboBox = createComboBox("HDD:", 30, 270, hddPreise.keySet().toArray(new String[0]));
        gpuComboBox = createComboBox("GPU:", 30, 320, gpuPreise.keySet().toArray(new String[0]));
        motherboardComboBox = createComboBox("Motherboard:", 30, 370, motherboardPreise.keySet().toArray(new String[0]));
        netzteilComboBox = createComboBox("Netzteil:", 30, 420, netzteilPreise.keySet().toArray(new String[0]));
        kuehlerComboBox = createComboBox("Kühler:", 30, 470, kuehlerPreise.keySet().toArray(new String[0]));
        laufwerkComboBox = createComboBox("Laufwerk:", 30, 520, laufwerkPreise.keySet().toArray(new String[0]));
        gehaeuseComboBox = createComboBox("Gehäuse:", 700, 120, gehaeusePreise.keySet().toArray(new String[0]));

        JLabel preisLabel = new JLabel("Preis:");
        preisLabel.setForeground(Color.LIGHT_GRAY);
        preisLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        preisLabel.setBounds(400, 620, 50, 30);
        add(preisLabel);

        preisField = createTextField(460, 620, 100, 30);
        preisField.setEditable(false);
        add(preisField);

        JLabel anzahlLabel = new JLabel("Anzahl:");
        anzahlLabel.setForeground(Color.LIGHT_GRAY);
        anzahlLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        anzahlLabel.setBounds(580, 620, 70, 30);
        add(anzahlLabel);

        anzahlField = createTextField(660, 620, 100, 30);
        anzahlField.setText("1");
        add(anzahlField);

        erstellenButton = createButton("Auftrag erstellen", 780, 620, 200, 40);
        add(erstellenButton);
        
        gehaeuseImageLabel = new JLabel();
        gehaeuseImageLabel.setBounds(700, 170, 400, 400); 
        gehaeuseImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(gehaeuseImageLabel);
        
        JLabel nachnameLabel = new JLabel("Nachname:");
        nachnameLabel.setForeground(Color.LIGHT_GRAY);
        nachnameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nachnameLabel.setBounds(450, 20, 150, 30);
        add(nachnameLabel);

        nachnameField = new JTextField();
        nachnameField.setBounds(550, 20, 200, 30);
        nachnameField.setBackground(Color.decode("#2E2E2E"));
        nachnameField.setForeground(Color.LIGHT_GRAY);
        nachnameField.setCaretColor(Color.WHITE);
        add(nachnameField);

        suchenButton = createButton("Suchen", 770, 20, 120, 30);
        add(suchenButton);
    }

    private void initializePrices() {
        cpuPreise = Map.of("Intel i5 - 2.3 GHz", 200.0, "Intel i7 - 3.1 GHz", 300.0);
        ramPreise = Map.of("8GB DDR4", 50.0, "16GB DDR4", 100.0);
        ssdPreise = Map.of("256GB", 70.0, "512GB", 120.0);
        hddPreise = Map.of("1TB", 50.0, "2TB", 80.0);
        gpuPreise = Map.of("NVIDIA GTX 1650", 150.0, "NVIDIA RTX 3070", 500.0);
        motherboardPreise = Map.of("ASUS Prime", 100.0, "MSI Pro", 150.0);
        netzteilPreise = Map.of("450W", 50.0, "600W", 75.0);
        kuehlerPreise = Map.of("Standard Kühler", 20.0, "Wasserkühlung", 120.0);
        laufwerkPreise = Map.of("DVD Laufwerk", 15.0, "Blu-ray Laufwerk", 50.0);
        gehaeusePreise = Map.of("Mini Tower", 30.0, "Midi Tower", 50.0);
    }

    // --------- ActionListener ---------
    private void addActionListeners() {
        ActionListener updatePriceListener = e -> updateTotalPrice();
        gehaeuseComboBox.addActionListener(e -> updateGehaeuseImage());

        cpuComboBox.addActionListener(updatePriceListener);
        ramComboBox.addActionListener(updatePriceListener);
        ssdComboBox.addActionListener(updatePriceListener);
        hddComboBox.addActionListener(updatePriceListener);
        gpuComboBox.addActionListener(updatePriceListener);
        motherboardComboBox.addActionListener(updatePriceListener);
        netzteilComboBox.addActionListener(updatePriceListener);
        kuehlerComboBox.addActionListener(updatePriceListener);
        laufwerkComboBox.addActionListener(updatePriceListener);
        gehaeuseComboBox.addActionListener(updatePriceListener);

        anzahlField.addActionListener(e -> updateTotalPrice());

        suchenButton.addActionListener(e -> {
            String nachname = nachnameField.getText().trim().toLowerCase();

            if (nachname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie einen Nachnamen ein.", "Fehler", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                List<Kunde> kundenListe = dbManager.searchKundeByNachname(nachname);

                if (kundenListe.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Kein Kunde mit diesem Nachnamen gefunden.", "Fehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (kundenListe.size() == 1) {
                    kundenNummerField.setText(String.valueOf(kundenListe.get(0).getKundenNummer()));
                    return;
                }

                String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Mehrere Kunden gefunden. Bitte wählen Sie einen aus:",
                    "Kunden auswählen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    kundenListe.stream()
                        .map(k -> k.getKundenNummer() + " - " + k.getVorname() + " " + k.getNachname())
                        .toArray(),
                    null
                );

                if (selected != null) {
                    kundenNummerField.setText(selected.split(" - ")[0]);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Suche: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        erstellenButton.addActionListener(e -> {
            try {
            	if(!(dbManager.searchKundeByNummer(Integer.parseInt(this.kundenNummerField.getText())).isEmpty())) {
            	} else {
            		throw new IllegalArgumentException("Kundennummer nicht gefunden!");
            	}

                int kundennummer = Integer.parseInt(kundenNummerField.getText());

                if (anzahlField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bitte geben Sie die Anzahl ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int anzahl = Integer.parseInt(anzahlField.getText());

                String preisText = preisField.getText().replace(",", ".").trim(); // Ersetze Komma durch Punkt
                if (preisText.isEmpty() || Double.parseDouble(preisText) <= 0) {
                    JOptionPane.showMessageDialog(this, "Der Preis darf nicht 0 oder leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double preis = Double.parseDouble(preisText);

                String cpu = (String) cpuComboBox.getSelectedItem();
                String ram = (String) ramComboBox.getSelectedItem();
                String ssd = (String) ssdComboBox.getSelectedItem();
                String hdd = (String) hddComboBox.getSelectedItem();
                String gpu = (String) gpuComboBox.getSelectedItem();
                String motherboard = (String) motherboardComboBox.getSelectedItem();
                String netzteil = (String) netzteilComboBox.getSelectedItem();
                String kuehler = (String) kuehlerComboBox.getSelectedItem();
                String laufwerk = (String) laufwerkComboBox.getSelectedItem();
                String gehaeuse = (String) gehaeuseComboBox.getSelectedItem();

                if (cpu == null || ram == null || ssd == null || hdd == null || gpu == null ||
                        motherboard == null || netzteil == null || kuehler == null || laufwerk == null || gehaeuse == null) {
                    JOptionPane.showMessageDialog(this, "Bitte wählen Sie alle Komponenten aus!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Auftrag auftrag = new Auftrag(
                        kundennummer, 
                        cpu, ram, ssd, hdd, gpu, motherboard, netzteil, kuehler, laufwerk,
                        gehaeuse, anzahl, preis, LocalDate.now(), "Eingegangen"
                );

                dbManager.insertAuftrag(auftrag);

                JOptionPane.showMessageDialog(this, "Auftrag erfolgreich erstellt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);

                resetFields();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie gültige numerische Werte für Kundennummer, Anzahl und Preis ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Erstellen des Auftrags: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // --------- Hilfsmethoden ---------

    private void updateTotalPrice() {
        double total = 0;

        total += cpuPreise.getOrDefault(cpuComboBox.getSelectedItem(), 0.0);
        total += ramPreise.getOrDefault(ramComboBox.getSelectedItem(), 0.0);
        total += ssdPreise.getOrDefault(ssdComboBox.getSelectedItem(), 0.0);
        total += hddPreise.getOrDefault(hddComboBox.getSelectedItem(), 0.0);
        total += gpuPreise.getOrDefault(gpuComboBox.getSelectedItem(), 0.0);
        total += motherboardPreise.getOrDefault(motherboardComboBox.getSelectedItem(), 0.0);
        total += netzteilPreise.getOrDefault(netzteilComboBox.getSelectedItem(), 0.0);
        total += kuehlerPreise.getOrDefault(kuehlerComboBox.getSelectedItem(), 0.0);
        total += laufwerkPreise.getOrDefault(laufwerkComboBox.getSelectedItem(), 0.0);
        total += gehaeusePreise.getOrDefault(gehaeuseComboBox.getSelectedItem(), 0.0);

        try {
            int anzahl = Integer.parseInt(anzahlField.getText().trim());
            if (anzahl <= 0) {
                JOptionPane.showMessageDialog(this, "Anzahl muss eine positive Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                anzahl = 1; 
                anzahlField.setText("1");
            }
            total *= anzahl;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Zahl für die Anzahl ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            anzahlField.setText("1"); 
            total = 0; 
        }

        preisField.setText(String.format("%.2f", total));
    }

    private void resetFields() {
        kundenNummerField.setText("");
        anzahlField.setText("1");
        preisField.setText("0.00");

        cpuComboBox.setSelectedIndex(0);
        ramComboBox.setSelectedIndex(0);
        ssdComboBox.setSelectedIndex(0);
        hddComboBox.setSelectedIndex(0);
        gpuComboBox.setSelectedIndex(0);
        motherboardComboBox.setSelectedIndex(0);
        netzteilComboBox.setSelectedIndex(0);
        kuehlerComboBox.setSelectedIndex(0);
        laufwerkComboBox.setSelectedIndex(0);
        gehaeuseComboBox.setSelectedIndex(0);
    }
    
    private void updateGehaeuseImage() {
        String selectedGehaeuse = (String) gehaeuseComboBox.getSelectedItem();
        if (selectedGehaeuse != null) {
            String imagePath = "./" + selectedGehaeuse.replace(" ", "_") + ".png"; 
            try {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                gehaeuseImageLabel.setIcon(new ImageIcon(
                       imageIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH) 
                ));
            } catch (Exception e) {
                gehaeuseImageLabel.setIcon(null); 
            }
        } else {
            gehaeuseImageLabel.setIcon(null); 
        }
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setBackground(Color.decode("#2E2E2E"));
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setCaretColor(Color.WHITE);
        return textField;
    }

    private JComboBox<String> createComboBox(String labelText, int x, int y, String[] items) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(x, y, 150, 30);
        add(label);

        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBounds(x + 160, y, 200, 30);
        add(comboBox);

        return comboBox;
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
}
