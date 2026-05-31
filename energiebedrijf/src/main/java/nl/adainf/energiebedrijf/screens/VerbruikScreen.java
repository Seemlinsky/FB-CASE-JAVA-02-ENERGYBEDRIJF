package nl.adainf.energiebedrijf.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.adainf.energiebedrijf.dao.VerbruikDao;
import nl.adainf.energiebedrijf.model.Verbruik;

import java.time.LocalDate;

public class VerbruikScreen {

    private final Scene scene;
    private final VerbruikDao verbruikDao = new VerbruikDao();
    private final ListView<Verbruik> listView = new ListView<>();

    public VerbruikScreen(int klantnummer, Runnable onOverview, Runnable onBack) {
        Label title = new Label("Wekelijks verbruik invoeren");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField tfStroom = new TextField();
        tfStroom.setPromptText("Stroom in kWh, bijvoorbeeld 30");

        TextField tfGas = new TextField();
        tfGas.setPromptText("Gas in m3, bijvoorbeeld 10");

        DatePicker dpStart = new DatePicker(LocalDate.now().minusDays(7));
        DatePicker dpEind = new DatePicker(LocalDate.now());

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: red;");

        Button btnOpslaan = new Button("Opslaan verbruik");
        Button btnWijzigen = new Button("Wijzig geselecteerde");
        Button btnVerwijderen = new Button("Verwijder geselecteerde");
        Button btnOverzicht = new Button("Naar overzicht");
        Button btnTerug = new Button("Terug naar start");

        btnOpslaan.setOnAction(e -> {
            Verbruik nieuwVerbruik = maakVerbruikVanInput(0, klantnummer, tfStroom, tfGas, dpStart, dpEind, msg);

            if (nieuwVerbruik == null) {
                return;
            }

            int nieuwId = verbruikDao.insert(nieuwVerbruik);
            refreshList(klantnummer);

            msg.setStyle("-fx-text-fill: green;");
            if (nieuwId > 0) {
                msg.setText("Verbruik opgeslagen.");
            } else {
                msg.setText("Verbruik opgeslagen, maar id kon niet opgehaald worden.");
            }
        });

        btnWijzigen.setOnAction(e -> {
            Verbruik selected = listView.getSelectionModel().getSelectedItem();

            if (selected == null) {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Selecteer eerst een verbruiksregel.");
                return;
            }

            Verbruik gewijzigd = maakVerbruikVanInput(selected.getId(), klantnummer, tfStroom, tfGas, dpStart, dpEind, msg);

            if (gewijzigd == null) {
                return;
            }

            int rowsChanged = verbruikDao.update(gewijzigd);
            refreshList(klantnummer);

            if (rowsChanged > 0) {
                // Na refresh opnieuw de gewijzigde regel selecteren.
                for (Verbruik verbruik : listView.getItems()) {
                    if (verbruik.getId() == gewijzigd.getId()) {
                        listView.getSelectionModel().select(verbruik);
                        break;
                    }
                }

                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Verbruik gewijzigd.");
            } else {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Wijzigen mislukt. Er is geen rij aangepast.");
            }
        });

        btnVerwijderen.setOnAction(e -> {
            Verbruik selected = listView.getSelectionModel().getSelectedItem();

            if (selected == null) {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Selecteer eerst een verbruiksregel.");
                return;
            }

            int rowsDeleted = verbruikDao.delete(selected.getId());
            refreshList(klantnummer);

            if (rowsDeleted > 0) {
                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Verbruik verwijderd.");
            } else {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Verwijderen mislukt. Er is geen rij verwijderd.");
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                tfStroom.setText(String.valueOf(selected.getStroomKwh()));
                tfGas.setText(String.valueOf(selected.getGasM3()));
                dpStart.setValue(selected.getDatumStart());
                dpEind.setValue(selected.getDatumEind());
            }
        });

        btnOverzicht.setOnAction(e -> onOverview.run());
        btnTerug.setOnAction(e -> onBack.run());

        HBox buttons = new HBox(10, btnOpslaan, btnWijzigen, btnVerwijderen, btnOverzicht, btnTerug);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(10,
                title,
                new Label("Stroomverbruik"), tfStroom,
                new Label("Gasverbruik"), tfGas,
                new Label("Startperiode"), dpStart,
                new Label("Eindperiode"), dpEind,
                buttons,
                msg,
                new Label("Opgeslagen verbruik"),
                listView
        );

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        listView.setPrefHeight(180);

        scene = new Scene(root, 850, 700);
        refreshList(klantnummer);
    }

    private Verbruik maakVerbruikVanInput(int id, int klantnummer, TextField tfStroom, TextField tfGas,
                                          DatePicker dpStart, DatePicker dpEind, Label msg) {
        msg.setStyle("-fx-text-fill: red;");
        msg.setText("");

        if (tfStroom.getText().isBlank() || tfGas.getText().isBlank()
                || dpStart.getValue() == null || dpEind.getValue() == null) {
            msg.setText("Vul alle velden in.");
            return null;
        }

        try {
            double stroom = Double.parseDouble(tfStroom.getText().replace(",", "."));
            double gas = Double.parseDouble(tfGas.getText().replace(",", "."));

            if (stroom < 0 || gas < 0) {
                msg.setText("Verbruik mag niet negatief zijn.");
                return null;
            }

            if (dpEind.getValue().isBefore(dpStart.getValue())) {
                msg.setText("Einddatum moet na startdatum liggen.");
                return null;
            }

            return new Verbruik(id, klantnummer, stroom, gas, dpStart.getValue(), dpEind.getValue());

        } catch (NumberFormatException ex) {
            msg.setText("Stroom en gas moeten getallen zijn.");
            return null;
        }
    }

    private void refreshList(int klantnummer) {
        listView.getItems().setAll(verbruikDao.getAllForKlant(klantnummer));
    }

    public Scene getScene() {
        return scene;
    }
}