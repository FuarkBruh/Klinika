package org.example.klinika;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;

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
        private Label brakDanychWiadomosc;
        @FXML
        protected void initialize() {
                // Ustawiamy minimalną datę na dzień dzisiejszy
                dataWizyty.setConverter(new LocalDateStringConverter());
                dataWizyty.setDayCellFactory(picker -> new DateCell() {
                        public void updateItem(LocalDate date, boolean empty) {
                                super.updateItem(date, empty);
                                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
                        }
                });

                rodzajWizyty.setOnAction(event -> specjalizacjaLekarza(rodzajWizyty.getValue()));
        }


        protected void specjalizacjaLekarza(String rodzajWizyty) {
                ObservableList<String> items = lekarz.getItems();

                // Wyczyszczenie listy i dodanie odpowiednich elementów w zależności od rodzaju wizyty
                // Wiem, że bez sensu trochę to wygląda, ale działa i działać będzie :)
                items.clear();
                switch (rodzajWizyty) {
                        case "Analiza wyników badań":
                                items.add("Jakub Nienormalny");
                                items.add("Jan Jędrzejczyk");
                                break;
                        case "Wizyta ogólna dorośli":
                                items.add("Jan Jędrzejczyk");
                                break;
                        case "Pediatria":
                                items.add("Jakub Nienormalny");
                                break;
                        case "Przedłużenie recepty":
                                items.add("Janina Kowalska");
                                if (!items.contains("Jakub Nienormalny")) {
                                        items.add("Jakub Nienormalny");
                                }
                                if (!items.contains("Jan Jędrzejczyk")) {
                                        items.add("Jan Jędrzejczyk");
                                }
                                break;
                }
        }
        @FXML
        protected void wyswietlWiadomoscBrakDanych() {
                brakDanychWiadomosc.setVisible(true);
        }

        protected void usunWiadomoscBrakDanych() {
                brakDanychWiadomosc.setVisible(false);
        }

        @FXML
        protected void onGenerujButtonClick(){
                if(imiePacjenta.getText().isEmpty() || nazwiskoPacjenta.getText().isEmpty() || ulicaPacjenta.getText().isEmpty() ||
                        miastoPacjenta.getText().isEmpty() || kodPocztowyPacjenta.getText().isEmpty() || dataWizyty.getValue() == null ||
                        godzinaWizyty.getValue() == null || rodzajWizyty.getValue() == null || lekarz.getValue() == null) {
                        wyswietlWiadomoscBrakDanych();
                }
                else {
                        GeneratorPDF.generatePDF(imiePacjenta, nazwiskoPacjenta, ulicaPacjenta,
                                miastoPacjenta, kodPocztowyPacjenta,
                                dataWizyty, godzinaWizyty, rodzajWizyty, lekarz);
                        usunWiadomoscBrakDanych();
                }
        }
    }