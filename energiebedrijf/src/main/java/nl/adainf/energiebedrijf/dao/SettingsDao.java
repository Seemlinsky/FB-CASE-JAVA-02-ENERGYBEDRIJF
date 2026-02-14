package nl.adainf.energiebedrijf.dao;

import nl.adainf.energiebedrijf.database.Database;
import nl.adainf.energiebedrijf.model.Settings;

import java.sql.*;

public class SettingsDao {

    public int insertSettings(Settings settings) {
        String sql = "INSERT INTO settings(yearly_advance, gas_price, electricity_price) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, settings.getYearlyAdvance());
            stmt.setDouble(2, settings.getGasPrice());
            stmt.setDouble(3, settings.getElectricityPrice());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // nieuwe id
            }

        } catch (SQLException e) {
            System.out.println("ERROR insertSettings: " + e.getMessage());
        }

        return -1;
    }

    public Settings getLatestSettings() {
        String sql = "SELECT * FROM settings ORDER BY id DESC LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                double yearly = rs.getDouble("yearly_advance");
                double gas = rs.getDouble("gas_price");
                double elec = rs.getDouble("electricity_price");

                return new Settings(yearly, gas, elec);
            }

        } catch (SQLException e) {
            System.out.println("ERROR getLatestSettings: " + e.getMessage());
        }

        return null;
    }
}