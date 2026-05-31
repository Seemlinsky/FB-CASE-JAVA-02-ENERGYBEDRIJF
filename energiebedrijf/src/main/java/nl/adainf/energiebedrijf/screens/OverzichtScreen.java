package nl.adainf.energiebedrijf.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import nl.adainf.energiebedrijf.dao.OverzichtDao;
import nl.adainf.energiebedrijf.model.Overzicht;
import nl.adainf.energiebedrijf.model.Tarief;
import nl.adainf.energiebedrijf.dao.TariefDao;

import java.util.ArrayList;

public class OverzichtScreen {

    private final Scene scene;
    private final TextArea overzichtText = new TextArea();
    private final Label msg = new Label();

    public OverzichtScreen(int klantnummer, Runnable onBack) {
        Label title = new Label("Overzicht verbruik + kosten");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnRefresh = new Button("Refresh overzicht");
        Button btnTerug = new Button("Terug naar verbruik");

        overzichtText.setEditable(false);
        overzichtText.setPrefSize(620, 330);

        btnRefresh.setOnAction(e -> laadOverzicht(klantnummer));
        btnTerug.setOnAction(e -> onBack.run());

        VBox root = new VBox(12, title, btnRefresh, overzichtText, msg, btnTerug);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 750, 600);
        laadOverzicht(klantnummer);
    }

    private void laadOverzicht(int klantnummer) {
        Overzicht overzicht = new OverzichtDao().getOverzichtVoorKlant(klantnummer);

        if (overzicht == null) {
            msg.setStyle("-fx-text-fill: red;");
            msg.setText("Geen overzicht gevonden.");
            return;
        }

        // Polymorfie voorbeeld: Tarief is abstract, maar de lijst bevat StroomTarief en GasTarief objecten.
        ArrayList<Tarief> tarieven = new TariefDao().getTarievenVoorKlant(klantnummer);
        StringBuilder tariefTekst = new StringBuilder();
        for (Tarief tarief : tarieven) {
            tariefTekst.append(tarief.getType())
                    .append(" tarief: ")
                    .append(String.format("%.2f", tarief.getTarief()))
                    .append("\n");
        }

        String tekst = "Klant: " + overzicht.getKlant().getKlantnummer() + " - " + overzicht.getKlant().getVolledigeNaam() + "\n" +
                "Jaarlijks voorschot: " + String.format("%.2f", overzicht.getKlant().getJaarlijksVoorschot()) + "\n" +
                "Maandelijks voorschot: " + String.format("%.2f", overzicht.getKlant().getJaarlijksVoorschot() / 12.0) + "\n\n" +
                tariefTekst + "\n" +
                "Totaal stroom (kWh): " + String.format("%.2f", overzicht.getTotaalStroom()) + "\n" +
                "Totaal gas (m3): " + String.format("%.2f", overzicht.getTotaalGas()) + "\n\n" +
                "Stroom kosten: " + String.format("%.2f", overzicht.getStroomKosten()) + "\n" +
                "Gas kosten: " + String.format("%.2f", overzicht.getGasKosten()) + "\n" +
                "TOTAAL kosten: " + String.format("%.2f", overzicht.getTotaalKosten()) + "\n\n" +
                "Gemiddeld per maand: " + String.format("%.2f", overzicht.getGemiddeldPerMaand());

        overzichtText.setText(tekst);

        if (overzicht.getGemiddeldPerMaand() > overzicht.getKlant().getJaarlijksVoorschot() / 12.0) {
            msg.setStyle("-fx-text-fill: red;");
            msg.setText("Let op: je zit boven je voorschot.");
        } else {
            msg.setStyle("-fx-text-fill: green;");
            msg.setText("OK: je zit onder je voorschot.");
        }
    }

    public Scene getScene() {
        return scene;
    }
}
