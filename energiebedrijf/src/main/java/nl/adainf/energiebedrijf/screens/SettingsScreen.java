package nl.adainf.energiebedrijf.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import nl.adainf.energiebedrijf.dao.SettingsDao;
import nl.adainf.energiebedrijf.model.Settings;

public class SettingsScreen {

    private final Scene scene;

    public SettingsScreen(Runnable onSavedGoNext) {

        Label title = new Label("Energiebedrijf Current - Start");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField tfYearly = new TextField();
        tfYearly.setPromptText("Yearly advance (bijv 1800)");

        TextField tfGas = new TextField();
        tfGas.setPromptText("Gas price per m3 (bijv 1.45)");

        TextField tfElec = new TextField();
        tfElec.setPromptText("Electricity price per kWh (bijv 0.40)");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: red;");

        Button btnSave = new Button("Save settings");

        btnSave.setOnAction(e -> {
            msg.setText("");

            // 1) check leeg
            if (tfYearly.getText().isBlank() || tfGas.getText().isBlank() || tfElec.getText().isBlank()) {
                msg.setText("Fill in all fields!");
                return;
            }

            try {
                double yearly = Double.parseDouble(tfYearly.getText().replace(",", "."));
                double gas = Double.parseDouble(tfGas.getText().replace(",", "."));
                double elec = Double.parseDouble(tfElec.getText().replace(",", "."));

                // 2) opslaan
                Settings settings = new Settings(yearly, gas, elec);
                SettingsDao dao = new SettingsDao();
                int newId = dao.insertSettings(settings);

                if (newId > 0) {
                    msg.setStyle("-fx-text-fill: green;");
                    msg.setText("Saved! (id=" + newId + ")");
                    onSavedGoNext.run(); // ga naar volgende screen
                } else {
                    msg.setStyle("-fx-text-fill: red;");
                    msg.setText("Saving failed...");
                }

            } catch (NumberFormatException ex) {
                msg.setText("Only numbers please (example: 1800 / 1.45 / 0.40)");
            }
        });

        VBox root = new VBox(10, title, tfYearly, tfGas, tfElec, btnSave, msg);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(500);

        this.scene = new Scene(root, 700, 450);
    }

    public Scene getScene() {
        return scene;
    }
}