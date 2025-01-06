package model;
public class Kunde {

    // --------- Instanzvariablen ---------
    private int kundenNummer; 
    private String anrede; 
    private String vorname;
    private String nachname;
    private String geburtstag;
    private String email;
    private String telefonnummer;

    private String adresseStrasse;
    private String adresseHausnummer;
    private String adressePlz;
    private String adresseOrt;

    private String lieferadresseStrasse;
    private String lieferadresseHausnummer;
    private String lieferadressePlz;
    private String lieferadresseOrt;

    private String kundenkategorie;

    // --------- Konstruktoren ---------

   
    public Kunde(int kundenNummer, String anrede, String vorname, String nachname, String geburtstag,
                 String email, String telefonnummer, String adresseStrasse, String adresseHausnummer,
                 String adressePlz, String adresseOrt, String lieferadresseStrasse,
                 String lieferadresseHausnummer, String lieferadressePlz,
                 String lieferadresseOrt, String kundenkategorie) {
        this.kundenNummer = kundenNummer; 
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtstag = geburtstag;
        this.email = email;
        this.telefonnummer = telefonnummer;
        this.adresseStrasse = adresseStrasse;
        this.adresseHausnummer = adresseHausnummer;
        this.adressePlz = adressePlz;
        this.adresseOrt = adresseOrt;
        this.lieferadresseStrasse = lieferadresseStrasse;
        this.lieferadresseHausnummer = lieferadresseHausnummer;
        this.lieferadressePlz = lieferadressePlz;
        this.lieferadresseOrt = lieferadresseOrt;
        this.kundenkategorie = kundenkategorie;
    }
    
    public Kunde(int kundenNummer, String vorname, String nachname, String geburtstag) {
        this.kundenNummer = kundenNummer; 
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtstag = geburtstag;
    }
    
    public Kunde(String anrede, String vorname, String nachname, String geburtstag, String email,
                 String telefonnummer, String adresseStrasse, String adresseHausnummer,
                 String adressePlz, String adresseOrt, String lieferadresseStrasse,
                 String lieferadresseHausnummer, String lieferadressePlz, String lieferadresseOrt,
                 String kundenkategorie) {
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtstag = geburtstag;
        this.email = email;
        this.telefonnummer = telefonnummer;
        this.adresseStrasse = adresseStrasse;
        this.adresseHausnummer = adresseHausnummer;
        this.adressePlz = adressePlz;
        this.adresseOrt = adresseOrt;
        this.lieferadresseStrasse = lieferadresseStrasse;
        this.lieferadresseHausnummer = lieferadresseHausnummer;
        this.lieferadressePlz = lieferadressePlz;
        this.lieferadresseOrt = lieferadresseOrt;
        this.kundenkategorie = kundenkategorie;
    }
    
    
    // --------- Getter-Methoden ---------
    public int getKundenNummer() { return kundenNummer; }

    public String getAnrede() { return anrede; }

    public String getVorname() { return vorname; }

    public String getNachname() { return nachname; }

    public String getGeburtstag() { return geburtstag; }

    public String getEmail() { return email; }

    public String getTelefonnummer() { return telefonnummer; }

    public String getAdresseStrasse() { return adresseStrasse; }

    public String getAdresseHausnummer() { return adresseHausnummer; }

    public String getAdressePlz() { return adressePlz; }

    public String getAdresseOrt() { return adresseOrt; }

    public String getLieferadresseStrasse() { return lieferadresseStrasse; }

    public String getLieferadresseHausnummer() { return lieferadresseHausnummer; }

    public String getLieferadressePlz() { return lieferadressePlz; }

    public String getLieferadresseOrt() { return lieferadresseOrt; }

    public String getKundenkategorie() { return kundenkategorie; }

    // --------- Setter-Methoden ---------

    public void setAnrede(String anrede) { this.anrede = anrede; }
    
    public void setVorname(String vorname) { this.vorname = vorname; }

    public void setNachname(String nachname) { this.nachname = nachname; }

    public void setEmail(String email) { this.email = email; }

    public void setTelefonnummer(String telefonnummer) { this.telefonnummer = telefonnummer; }

    public void setAdresseStrasse(String adresseStrasse) { this.adresseStrasse = adresseStrasse; }

    public void setAdresseHausnummer(String adresseHausnummer) { this.adresseHausnummer = adresseHausnummer; }

    public void setAdressePlz(String adressePlz) { this.adressePlz = adressePlz; }

    public void setAdresseOrt(String adresseOrt) { this.adresseOrt = adresseOrt; }

    public void setLieferadresseStrasse(String lieferadresseStrasse) { this.lieferadresseStrasse = lieferadresseStrasse; }

    public void setLieferadresseHausnummer(String lieferadresseHausnummer) { this.lieferadresseHausnummer = lieferadresseHausnummer; }

    public void setLieferadressePlz(String lieferadressePlz) { this.lieferadressePlz = lieferadressePlz; }

    public void setLieferadresseOrt(String lieferadresseOrt) { this.lieferadresseOrt = lieferadresseOrt; }
}
