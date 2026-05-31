package nl.adainf.energiebedrijf.model;

import java.time.LocalDate;

public abstract class Tarief {

    private final int id;
    private final int klantnummer;
    private final double tarief;
    private final LocalDate datumVanaf;
    private final LocalDate datumTot;

    public Tarief(int id, int klantnummer, double tarief, LocalDate datumVanaf, LocalDate datumTot) {
        this.id = id;
        this.klantnummer = klantnummer;
        this.tarief = tarief;
        this.datumVanaf = datumVanaf;
        this.datumTot = datumTot;
    }

    public int getId() {
        return id;
    }

    public int getKlantnummer() {
        return klantnummer;
    }

    public double getTarief() {
        return tarief;
    }

    public LocalDate getDatumVanaf() {
        return datumVanaf;
    }

    public LocalDate getDatumTot() {
        return datumTot;
    }

    // Overerving/polymorfie: StroomTarief en GasTarief geven allebei hun eigen type terug.
    public abstract String getType();
}
