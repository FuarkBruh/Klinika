package org.example.klinika;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControllerKlinika {
        @FXML
        private TextField imiePacjenta;
        @FXML
        private TextField nazwiskoPacjenta;
        @FXML
        private TextField ulicaPacjenta;
        @FXML
        private TextField miastoPacjenta;
        @FXML
        private TextField kodPocztowyPacjenta;
        @FXML
        private DatePicker dataWizyty;
        @FXML
        private ComboBox<String> godzinaWizyty;
        @FXML
        private ComboBox<String> rodzajWizyty;
        @FXML
        private ComboBox<String> lekarz;

        @FXML
        protected void onGenerujButtonClick() throws IOException {
            GeneratorPDF.generatePDF(imiePacjenta, nazwiskoPacjenta, ulicaPacjenta, miastoPacjenta, kodPocztowyPacjenta,
                    dataWizyty, godzinaWizyty, rodzajWizyty, lekarz);
        }
    }