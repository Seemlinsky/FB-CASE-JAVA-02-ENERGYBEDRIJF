package nl.adainf.energiebedrijf.dao;

import nl.adainf.energiebedrijf.database.Database;
import nl.adainf.energiebedrijf.model.Verbruik;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VerbruikDao {

    public int insert(Verbruik verbruik) {
        String sql = "INSERT INTO verbruik(klantnummer, stroom_kwh, gas_m3, datum_start, datum_eind) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, verbruik.getKlantnummer());
            ps.setDouble(2, verbruik.getStroomKwh());
            ps.setDouble(3, verbruik.getGasM3());
            ps.setDate(4, Date.valueOf(verbruik.getDatumStart()));
            ps.setDate(5, Date.valueOf(verbruik.getDatumEind()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR insert verbruik: " + e.getMessage());
        }

        return -1;
    }

    public int update(Verbruik verbruik) {
        String sql = "UPDATE verbruik " +
                "SET stroom_kwh = ?, gas_m3 = ?, datum_start = ?, datum_eind = ? " +
                "WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, verbruik.getStroomKwh());
            ps.setDouble(2, verbruik.getGasM3());
            ps.setDate(3, Date.valueOf(verbruik.getDatumStart()));
            ps.setDate(4, Date.valueOf(verbruik.getDatumEind()));
            ps.setInt(5, verbruik.getId());

            int rowsChanged = ps.executeUpdate();
            System.out.println("UPDATE verbruik id=" + verbruik.getId() + ", rowsChanged=" + rowsChanged);

            return rowsChanged;

        } catch (SQLException e) {
            System.out.println("ERROR update verbruik: " + e.getMessage());
        }

        return 0;
    }

    public int delete(int id) {
        String sql = "DELETE FROM verbruik WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            System.out.println("DELETE verbruik id=" + id + ", rowsDeleted=" + rowsDeleted);

            return rowsDeleted;

        } catch (SQLException e) {
            System.out.println("ERROR delete verbruik: " + e.getMessage());
        }

        return 0;
    }

    public ArrayList<Verbruik> getAllForKlant(int klantnummer) {
        ArrayList<Verbruik> list = new ArrayList<>();

        String sql = "SELECT * FROM verbruik WHERE klantnummer = ? ORDER BY datum_start ASC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, klantnummer);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Verbruik(
                            rs.getInt("id"),
                            rs.getInt("klantnummer"),
                            rs.getDouble("stroom_kwh"),
                            rs.getDouble("gas_m3"),
                            rs.getDate("datum_start").toLocalDate(),
                            rs.getDate("datum_eind").toLocalDate()
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR get verbruik: " + e.getMessage());
        }

        return list;
    }
}