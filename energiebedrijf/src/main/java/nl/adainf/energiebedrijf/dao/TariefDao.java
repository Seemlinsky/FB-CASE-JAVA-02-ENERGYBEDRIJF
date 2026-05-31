package nl.adainf.energiebedrijf.dao;

import nl.adainf.energiebedrijf.database.Database;
import nl.adainf.energiebedrijf.model.GasTarief;
import nl.adainf.energiebedrijf.model.StroomTarief;
import nl.adainf.energiebedrijf.model.Tarief;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TariefDao {

    public void insertStroom(int klantnummer, double tariefKwh, LocalDate vanaf, LocalDate tot) {
        String sql = "INSERT INTO stroomtarief(klantnummer, tarief_kwh, datum_vanaf, datum_tot) VALUES(?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);
            ps.setDouble(2, tariefKwh);
            ps.setDate(3, Date.valueOf(vanaf));
            ps.setDate(4, Date.valueOf(tot));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR insert stroomtarief: " + e.getMessage());
        }
    }

    public void insertGas(int klantnummer, double tariefM3, LocalDate vanaf, LocalDate tot) {
        String sql = "INSERT INTO gastarief(klantnummer, tarief_m3, datum_vanaf, datum_tot) VALUES(?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);
            ps.setDouble(2, tariefM3);
            ps.setDate(3, Date.valueOf(vanaf));
            ps.setDate(4, Date.valueOf(tot));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR insert gastarief: " + e.getMessage());
        }
    }

    public StroomTarief getLaatsteStroomTarief(int klantnummer) {
        String sql = "SELECT * FROM stroomtarief WHERE klantnummer = ? ORDER BY datum_vanaf DESC LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StroomTarief(
                            rs.getInt("id"),
                            rs.getInt("klantnummer"),
                            rs.getDouble("tarief_kwh"),
                            rs.getDate("datum_vanaf").toLocalDate(),
                            rs.getDate("datum_tot").toLocalDate()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR get stroomtarief: " + e.getMessage());
        }

        return null;
    }

    public GasTarief getLaatsteGasTarief(int klantnummer) {
        String sql = "SELECT * FROM gastarief WHERE klantnummer = ? ORDER BY datum_vanaf DESC LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GasTarief(
                            rs.getInt("id"),
                            rs.getInt("klantnummer"),
                            rs.getDouble("tarief_m3"),
                            rs.getDate("datum_vanaf").toLocalDate(),
                            rs.getDate("datum_tot").toLocalDate()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR get gastarief: " + e.getMessage());
        }

        return null;
    }

    public ArrayList<Tarief> getTarievenVoorKlant(int klantnummer) {
        ArrayList<Tarief> tarieven = new ArrayList<>();
        StroomTarief stroomTarief = getLaatsteStroomTarief(klantnummer);
        GasTarief gasTarief = getLaatsteGasTarief(klantnummer);

        if (stroomTarief != null) {
            tarieven.add(stroomTarief);
        }
        if (gasTarief != null) {
            tarieven.add(gasTarief);
        }

        return tarieven;
    }
}
