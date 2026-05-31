package nl.adainf.energiebedrijf.model;

import java.time.LocalDate;

public class StroomTarief extends Tarief {

    public StroomTarief(int id, int klantnummer, double tarief, LocalDate datumVanaf, LocalDate datumTot) {
        super(id, klantnummer, tarief, datumVanaf, datumTot);
    }

    @Override
    public String getType() {
        return "Stroom";
    }
}
