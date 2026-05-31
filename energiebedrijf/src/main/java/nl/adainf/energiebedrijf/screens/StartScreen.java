package nl.adainf.energiebedrijf.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.adainf.energiebedrijf.dao.KlantDao;
import nl.adainf.energiebedrijf.dao.TariefDao;
import nl.adainf.energiebedrijf.model.Klant;

import java.time.LocalDate;
import java.util.function.IntConsumer;

public class StartScreen {

    private final Scene scene;

    public StartScreen(IntConsumer onSavedGoNext) {
        Label title = new Label("Energiebedrijf Current - Start");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField tfKlantnummer = new TextField();
        tfKlantnummer.setPromptText("Klantnummer, bijvoorbeeld 1");

        TextField tfVoornaam = new TextField();
        tfVoornaam.setPromptText("Voornaam");

        TextField tfAchternaam = new TextField();
        tfAchternaam.setPromptText("Achternaam");

        TextField tfVoorschot = new TextField();
        tfVoorschot.setPromptText("Jaarlijks voorschot, bijvoorbeeld 1800");

        TextField tfStroom = new TextField();
        tfStroom.setPromptText("Stroomtarief per kWh, bijvoorbeeld 0.40");

        TextField tfGas = new TextField();
        tfGas.setPromptText("Gastarief per m3, bijvoorbeeld 1.30");

        DatePicker dpVanaf = new DatePicker(LocalDate.now());
        DatePicker dpTot = new DatePicker(LocalDate.now().plusYears(1));

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: red;");

        Button btnSave = new Button("Klant en tarieven opslaan");

        btnSave.setOnAction(e -> {
            msg.setStyle("-fx-text-fill: red;");
            msg.setText("");

            if (tfKlantnummer.getText().isBlank()
                    || tfVoornaam.getText().isBlank()
                    || tfAchternaam.getText().isBlank()
                    || tfVoorschot.getText().isBlank()
                    || tfStroom.getText().isBlank()
                    || tfGas.getText().isBlank()
                    || dpVanaf.getValue() == null
                    || dpTot.getValue() == null) {
                msg.setText("Vul alle velden in.");
                return;
            }

            try {
                int klantnummer = Integer.parseInt(tfKlantnummer.getText().trim());
                double voorschot = Double.parseDouble(tfVoorschot.getText().replace(",", "."));
                double stroomTarief = Double.parseDouble(tfStroom.getText().replace(",", "."));
                double gasTarief = Double.parseDouble(tfGas.getText().replace(",", "."));

                if (voorschot <= 0 || stroomTarief <= 0 || gasTarief <= 0) {
                    msg.setText("Voorschot en tarieven moeten groter zijn dan 0.");
                    return;
                }

                if (dpTot.getValue().isBefore(dpVanaf.getValue())) {
                    msg.setText("Datum tot moet na datum vanaf liggen.");
                    return;
                }

                Klant klant = new Klant(
                        klantnummer,
                        tfVoornaam.getText().trim(),
                        tfAchternaam.getText().trim(),
                        voorschot
                );

                KlantDao klantDao = new KlantDao();
                if (klantDao.getByKlantnummer(klantnummer) == null) {
                    klantDao.insert(klant);
                } else {
                    klantDao.update(klant);
                }

                TariefDao tariefDao = new TariefDao();
                tariefDao.insertStroom(klantnummer, stroomTarief, dpVanaf.getValue(), dpTot.getValue());
                tariefDao.insertGas(klantnummer, gasTarief, dpVanaf.getValue(), dpTot.getValue());

                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Klant en tarieven opgeslagen.");
                onSavedGoNext.accept(klantnummer);

            } catch (NumberFormatException ex) {
                msg.setText("Klantnummer, voorschot en tarieven moeten getallen zijn.");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Klantnummer"), 0, 0);
        grid.add(tfKlantnummer, 1, 0);
        grid.add(new Label("Voornaam"), 0, 1);
        grid.add(tfVoornaam, 1, 1);
        grid.add(new Label("Achternaam"), 0, 2);
        grid.add(tfAchternaam, 1, 2);
        grid.add(new Label("Jaarlijks voorschot"), 0, 3);
        grid.add(tfVoorschot, 1, 3);
        grid.add(new Label("Stroomtarief"), 0, 4);
        grid.add(tfStroom, 1, 4);
        grid.add(new Label("Gastarief"), 0, 5);
        grid.add(tfGas, 1, 5);
        grid.add(new Label("Datum vanaf"), 0, 6);
        grid.add(dpVanaf, 1, 6);
        grid.add(new Label("Datum tot"), 0, 7);
        grid.add(dpTot, 1, 7);

        VBox root = new VBox(15, title, grid, btnSave, msg);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 750, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
