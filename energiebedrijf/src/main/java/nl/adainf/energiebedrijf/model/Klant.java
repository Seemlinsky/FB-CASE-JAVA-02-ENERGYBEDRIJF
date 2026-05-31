package nl.adainf.energiebedrijf.model;

public class Klant {

    private final int klantnummer;
    private final String voornaam;
    private final String achternaam;
    private final double jaarlijksVoorschot;

    public Klant(int klantnummer, String voornaam, String achternaam, double jaarlijksVoorschot) {
        this.klantnummer = klantnummer;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.jaarlijksVoorschot = jaarlijksVoorschot;
    }

    public int getKlantnummer() {
        return klantnummer;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public double getJaarlijksVoorschot() {
        return jaarlijksVoorschot;
    }

    public String getVolledigeNaam() {
        return voornaam + " " + achternaam;
    }
}
