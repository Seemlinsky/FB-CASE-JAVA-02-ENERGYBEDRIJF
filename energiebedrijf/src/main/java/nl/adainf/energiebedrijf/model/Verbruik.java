package nl.adainf.energiebedrijf.model;

import java.time.LocalDate;

public class Verbruik {

    private final int id;
    private final int klantnummer;
    private final double stroomKwh;
    private final double gasM3;
    private final LocalDate datumStart;
    private final LocalDate datumEind;

    public Verbruik(int id, int klantnummer, double stroomKwh, double gasM3, LocalDate datumStart, LocalDate datumEind) {
        this.id = id;
        this.klantnummer = klantnummer;
        this.stroomKwh = stroomKwh;
        this.gasM3 = gasM3;
        this.datumStart = datumStart;
        this.datumEind = datumEind;
    }

    public int getId() {
        return id;
    }

    public int getKlantnummer() {
        return klantnummer;
    }

    public double getStroomKwh() {
        return stroomKwh;
    }

    public double getGasM3() {
        return gasM3;
    }

    public LocalDate getDatumStart() {
        return datumStart;
    }

    public LocalDate getDatumEind() {
        return datumEind;
    }

    @Override
    public String toString() {
        return datumStart + " t/m " + datumEind + " | stroom: " + stroomKwh + " kWh | gas: " + gasM3 + " m3";
    }
}
