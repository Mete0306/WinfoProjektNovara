package model;
import java.time.LocalDate;

public class Auftrag {

    // --------- Auftragsdaten ---------
    private int auftragsnummer; 
    private int kundennummer;
    private String cpu;
    private String ram;
    private String ssd;
    private String hdd;
    private String gpu;
    private String motherboard;
    private String netzteil;
    private String kuehler;
    private String laufwerk;
    private String gehaeuse;
    private int anzahl;
    private double preis;
    private LocalDate datum;
    private String status; 

    // --------- Konstruktor ---------
    public Auftrag(int kundennummer, String cpu, String ram, String ssd, String hdd, String gpu,
                   String motherboard, String netzteil, String kuehler, String laufwerk, String gehaeuse,
                   int anzahl, double preis, LocalDate datum, String status) {
        this.kundennummer = kundennummer;
        this.cpu = cpu;
        this.ram = ram;
        this.ssd = ssd;
        this.hdd = hdd;
        this.gpu = gpu;
        this.motherboard = motherboard;
        this.netzteil = netzteil;
        this.kuehler = kuehler;
        this.laufwerk = laufwerk;
        this.gehaeuse = gehaeuse;
        this.anzahl = anzahl;
        this.preis = preis;
        this.datum = datum;
        this.status = status; 
    }

    public Auftrag(int auftragsnummer, int kundennummer, LocalDate datum, String status) {
        this.auftragsnummer = auftragsnummer;
        this.kundennummer = kundennummer;
        this.datum = datum;
        this.status = status;
    }

    // --------- Getter- und Setter-Methoden ---------
    public int getAuftragsnummer() { return auftragsnummer; }

    public void setAuftragsnummer(int auftragsnummer) { this.auftragsnummer = auftragsnummer; }

    public int getKundennummer() { return kundennummer; }

    public void setKundennummer(int kundennummer) { this.kundennummer = kundennummer; }

    public String getCpu() { return cpu; }

    public void setCpu(String cpu) { this.cpu = cpu; }

    public String getRam() { return ram; }

    public void setRam(String ram) { this.ram = ram; }

    public String getSsd() { return ssd; }

    public void setSsd(String ssd) { this.ssd = ssd; }

    public String getHdd() { return hdd; }

    public void setHdd(String hdd) { this.hdd = hdd; }

    public String getGpu() { return gpu; }

    public void setGpu(String gpu) { this.gpu = gpu; }

    public String getMotherboard() { return motherboard; }

    public void setMotherboard(String motherboard) { this.motherboard = motherboard; }

    public String getNetzteil() { return netzteil; }

    public void setNetzteil(String netzteil) { this.netzteil = netzteil; }

    public String getKuehler() { return kuehler; }

    public void setKuehler(String kuehler) { this.kuehler = kuehler; }

    public String getLaufwerk() { return laufwerk; }

    public void setLaufwerk(String laufwerk) { this.laufwerk = laufwerk; }

    public String getGehaeuse() { return gehaeuse; }

    public void setGehaeuse(String gehaeuse) { this.gehaeuse = gehaeuse; }

    public int getAnzahl() { return anzahl; }

    public void setAnzahl(int anzahl) { this.anzahl = anzahl; }

    public double getPreis() { return preis; }

    public void setPreis(double preis) { this.preis = preis; }

    public LocalDate getDatum() { return datum; }

    public void setDatum(LocalDate datum) { this.datum = datum; }

    public String getStatus() { return status; } 

    public void setStatus(String status) { this.status = status; } 
}
