package nl.adainf.energiebedrijf.model;

import java.time.LocalDate;

public class GasTarief extends Tarief {

    public GasTarief(int id, int klantnummer, double tarief, LocalDate datumVanaf, LocalDate datumTot) {
        super(id, klantnummer, tarief, datumVanaf, datumTot);
    }

    @Override
    public String getType() {
        return "Gas";
    }
}
