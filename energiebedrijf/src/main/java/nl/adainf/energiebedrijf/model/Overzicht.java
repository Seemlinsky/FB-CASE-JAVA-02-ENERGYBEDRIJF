package nl.adainf.energiebedrijf.model;

public class Overzicht {

    private final Klant klant;
    private final double totaalStroom;
    private final double totaalGas;
    private final double stroomTarief;
    private final double gasTarief;

    public Overzicht(Klant klant, double totaalStroom, double totaalGas, double stroomTarief, double gasTarief) {
        this.klant = klant;
        this.totaalStroom = totaalStroom;
        this.totaalGas = totaalGas;
        this.stroomTarief = stroomTarief;
        this.gasTarief = gasTarief;
    }

    public Klant getKlant() {
        return klant;
    }

    public double getTotaalStroom() {
        return totaalStroom;
    }

    public double getTotaalGas() {
        return totaalGas;
    }

    public double getStroomKosten() {
        return totaalStroom * stroomTarief;
    }

    public double getGasKosten() {
        return totaalGas * gasTarief;
    }

    public double getTotaalKosten() {
        return getStroomKosten() + getGasKosten();
    }

    public double getGemiddeldPerMaand() {
        return getTotaalKosten() / 12.0;
    }
}
