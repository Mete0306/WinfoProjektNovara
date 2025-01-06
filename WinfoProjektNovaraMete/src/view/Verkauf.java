package view;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import database.DBManager;

import java.awt.*;

public class Verkauf extends JFrame {

    private DBManager dbManager;

    public Verkauf(DBManager dbManager) {
        this.dbManager = dbManager;

        setTitle("HighSpeed GmbH - Verkaufsabteilung");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#1E1E1E"));

        JLabel highSpeedLabel = new JLabel("HighSpeed GmbH");
        highSpeedLabel.setForeground(Color.WHITE);
        highSpeedLabel.setFont(new Font("Arial", Font.BOLD, 28));
        highSpeedLabel.setBounds(20, 20, highSpeedLabel.getPreferredSize().width, highSpeedLabel.getPreferredSize().height);
        add(highSpeedLabel);

        JLabel madeByLabel = new JLabel("- Made by Novara -");
        madeByLabel.setForeground(Color.LIGHT_GRAY);
        madeByLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        madeByLabel.setBounds(40, 60, 200, 20);
        add(madeByLabel);

        JLabel titleLabel = new JLabel("Verkaufs Abteilung", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBounds(500, 20, 400, 50);
        add(titleLabel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 100, 1300, 700);
        tabbedPane.setBackground(Color.decode("#2E2E2E"));
        tabbedPane.setForeground(Color.LIGHT_GRAY);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 18));
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            }
        });

        SuchenPanel suchenPanel = new SuchenPanel(dbManager);

        KundeErfassenPanel kundeErfassenPanel = new KundeErfassenPanel(dbManager, suchenPanel);
        tabbedPane.addTab("Kunde erfassen", kundeErfassenPanel);

        AuftragErstellenPanel auftragErstellenPanel = new AuftragErstellenPanel(dbManager);
        tabbedPane.addTab("Auftrag erstellen", auftragErstellenPanel);

        tabbedPane.addTab("Suchen", suchenPanel);

        add(tabbedPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
