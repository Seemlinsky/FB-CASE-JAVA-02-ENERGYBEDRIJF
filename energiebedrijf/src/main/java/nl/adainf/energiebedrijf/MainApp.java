package nl.adainf.energiebedrijf;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.adainf.energiebedrijf.screens.OverzichtScreen;
import nl.adainf.energiebedrijf.screens.StartScreen;
import nl.adainf.energiebedrijf.screens.VerbruikScreen;

public class MainApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Energiebedrijf Current");
        showStartScreen();
        stage.show();
    }

    private void showStartScreen() {
        StartScreen startScreen = new StartScreen(klantnummer -> showVerbruikScreen(klantnummer));
        stage.setScene(startScreen.getScene());
    }

    private void showVerbruikScreen(int klantnummer) {
        VerbruikScreen verbruikScreen = new VerbruikScreen(
                klantnummer,
                () -> showOverzichtScreen(klantnummer),
                this::showStartScreen
        );
        stage.setScene(verbruikScreen.getScene());
    }

    private void showOverzichtScreen(int klantnummer) {
        OverzichtScreen overzichtScreen = new OverzichtScreen(
                klantnummer,
                () -> showVerbruikScreen(klantnummer)
        );
        stage.setScene(overzichtScreen.getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
