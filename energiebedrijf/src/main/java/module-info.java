module nl.adainf.energiebedrijf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens nl.adainf.energiebedrijf to javafx.fxml;
    exports nl.adainf.energiebedrijf;
}