package nl.adainf.energiebedrijf.model;

public class Settings {

    private int id;
    private double yearlyAdvance;
    private double gasPrice;
    private double electricityPrice;

    public Settings(double yearlyAdvance, double gasPrice, double electricityPrice) {
        this.yearlyAdvance = yearlyAdvance;
        this.gasPrice = gasPrice;
        this.electricityPrice = electricityPrice;
    }

    public double getYearlyAdvance() {
        return yearlyAdvance;
    }

    public double getGasPrice() {
        return gasPrice;
    }

    public double getElectricityPrice() {
        return electricityPrice;
    }
}