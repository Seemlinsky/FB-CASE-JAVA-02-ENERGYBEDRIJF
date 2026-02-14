package nl.adainf.energiebedrijf;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.adainf.energiebedrijf.screens.SettingsScreen;

public class MainApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        SettingsScreen screen1 = new SettingsScreen(() -> {
            System.out.println("Settings saved -> next screen later");
            // straks: stage.setScene(new UsageScreen(...).getScene());
        });

        stage.setTitle("Energiebedrijf Current");
        stage.setScene(screen1.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}