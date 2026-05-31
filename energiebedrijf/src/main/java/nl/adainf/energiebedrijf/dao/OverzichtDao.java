package nl.adainf.energiebedrijf.dao;

import nl.adainf.energiebedrijf.database.Database;
import nl.adainf.energiebedrijf.model.Klant;
import nl.adainf.energiebedrijf.model.Overzicht;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OverzichtDao {

    public Overzicht getOverzichtVoorKlant(int klantnummer) {
        // JOIN query: klant wordt gekoppeld aan verbruik en tarieven.
        String sql = """
                SELECT k.klantnummer, k.voornaam, k.achternaam, k.jaarlijks_voorschot,
                       COALESCE(v.totaal_stroom, 0) AS totaal_stroom,
                       COALESCE(v.totaal_gas, 0) AS totaal_gas,
                       COALESCE(st.tarief_kwh, 0) AS tarief_kwh,
                       COALESCE(gt.tarief_m3, 0) AS tarief_m3
                FROM klant k
                LEFT JOIN (
                    SELECT klantnummer, SUM(stroom_kwh) AS totaal_stroom, SUM(gas_m3) AS totaal_gas
                    FROM verbruik
                    GROUP BY klantnummer
                ) v ON v.klantnummer = k.klantnummer
                LEFT JOIN stroomtarief st ON st.id = (
                    SELECT id FROM stroomtarief WHERE klantnummer = k.klantnummer ORDER BY datum_vanaf DESC LIMIT 1
                )
                LEFT JOIN gastarief gt ON gt.id = (
                    SELECT id FROM gastarief WHERE klantnummer = k.klantnummer ORDER BY datum_vanaf DESC LIMIT 1
                )
                WHERE k.klantnummer = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Klant klant = new Klant(
                            rs.getInt("klantnummer"),
                            rs.getString("voornaam"),
                            rs.getString("achternaam"),
                            rs.getDouble("jaarlijks_voorschot")
                    );

                    return new Overzicht(
                            klant,
                            rs.getDouble("totaal_stroom"),
                            rs.getDouble("totaal_gas"),
                            rs.getDouble("tarief_kwh"),
                            rs.getDouble("tarief_m3")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR overzicht: " + e.getMessage());
        }

        return null;
    }
}
