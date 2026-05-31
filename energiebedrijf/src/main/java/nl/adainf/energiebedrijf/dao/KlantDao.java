package nl.adainf.energiebedrijf.dao;

import nl.adainf.energiebedrijf.database.Database;
import nl.adainf.energiebedrijf.model.Klant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KlantDao {

    public void insert(Klant klant) {
        String sql = "INSERT INTO klant(klantnummer, voornaam, achternaam, jaarlijks_voorschot) VALUES(?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klant.getKlantnummer());
            ps.setString(2, klant.getVoornaam());
            ps.setString(3, klant.getAchternaam());
            ps.setDouble(4, klant.getJaarlijksVoorschot());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR insert klant: " + e.getMessage());
        }
    }

    public void update(Klant klant) {
        String sql = "UPDATE klant SET voornaam = ?, achternaam = ?, jaarlijks_voorschot = ? WHERE klantnummer = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, klant.getVoornaam());
            ps.setString(2, klant.getAchternaam());
            ps.setDouble(3, klant.getJaarlijksVoorschot());
            ps.setInt(4, klant.getKlantnummer());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR update klant: " + e.getMessage());
        }
    }

    public Klant getByKlantnummer(int klantnummer) {
        String sql = "SELECT * FROM klant WHERE klantnummer = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Klant(
                            rs.getInt("klantnummer"),
                            rs.getString("voornaam"),
                            rs.getString("achternaam"),
                            rs.getDouble("jaarlijks_voorschot")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR get klant: " + e.getMessage());
        }

        return null;
    }
}
