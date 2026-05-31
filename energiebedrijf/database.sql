CREATE DATABASE IF NOT EXISTS energiebedrijf_current;
USE energiebedrijf_current;

DROP TABLE IF EXISTS verbruik;
DROP TABLE IF EXISTS stroomtarief;
DROP TABLE IF EXISTS gastarief;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS klant;

CREATE TABLE klant (
    klantnummer INT PRIMARY KEY,
    voornaam VARCHAR(100) NOT NULL,
    achternaam VARCHAR(100) NOT NULL,
    jaarlijks_voorschot DOUBLE NOT NULL
);

CREATE TABLE stroomtarief (
    id INT AUTO_INCREMENT PRIMARY KEY,
    klantnummer INT NOT NULL,
    tarief_kwh DOUBLE NOT NULL,
    datum_vanaf DATE NOT NULL,
    datum_tot DATE NOT NULL,
    FOREIGN KEY (klantnummer) REFERENCES klant(klantnummer)
);

CREATE TABLE gastarief (
    id INT AUTO_INCREMENT PRIMARY KEY,
    klantnummer INT NOT NULL,
    tarief_m3 DOUBLE NOT NULL,
    datum_vanaf DATE NOT NULL,
    datum_tot DATE NOT NULL,
    FOREIGN KEY (klantnummer) REFERENCES klant(klantnummer)
);

CREATE TABLE verbruik (
    id INT AUTO_INCREMENT PRIMARY KEY,
    klantnummer INT NOT NULL,
    stroom_kwh DOUBLE NOT NULL,
    gas_m3 DOUBLE NOT NULL,
    datum_start DATE NOT NULL,
    datum_eind DATE NOT NULL,
    FOREIGN KEY (klantnummer) REFERENCES klant(klantnummer)
);
