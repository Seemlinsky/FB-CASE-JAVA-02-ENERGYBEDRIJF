package nl.adainf.energiebedrijf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.adainf.energiebedrijf.database.Database;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // 🔥 DATABASE TEST
        try {
            System.out.println("Database test...");
            Connection conn = Database.getConnection();
            System.out.println("Connected to database!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Energiebedrijf Current");
        stage.setScene(scene);
        stage.show();
    }
}