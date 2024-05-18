package org.example.klinika;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ControllerKlinika {
        @FXML
        private TextField imiePacjenta;
        @FXML
        private TextField nazwiskoPacjenta;
        @FXML
        private TextField ulicaPacjenta;
        @FXML
        private TextField nrBudynku;
        @FXML
        private TextField nrMieszkania;
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
        private Map<String, Map<LocalDate, Map<String, Boolean>>> wizyty = new HashMap<>();


        @FXML
        protected void initialize() {
                // Poprawione ustawienie minimalnej daty
                dataWizyty.setConverter(new LocalDateStringConverter());
                dataWizyty.setDayCellFactory(picker -> new DateCell() {
                        public void updateItem(LocalDate date, boolean empty) {
                                super.updateItem(date, empty);
                                setDisable(empty || date.isBefore(LocalDate.now()));
                        }
                });

                // Inicjalizacja ComboBoxa rodzajWizyty
                rodzajWizyty.setItems(FXCollections.observableArrayList(
                        rodzajWizyty.getItems()));
                rodzajWizyty.setOnAction(event -> specjalizacjaLekarza(rodzajWizyty.getValue()));

                // Ustawienie DatePicker jako nieaktywny na początku
                dataWizyty.setDisable(true);

                // Listener do ComboBoxa lekarz, aby włączyć DatePicker po wybraniu lekarza
                lekarz.valueProperty().addListener((observable, oldValue, newValue) -> {
                        dataWizyty.setDisable(newValue == null);
                });

                initializeWizyty();
        }

        private void initializeWizyty() {
                ObservableList<String> godziny = godzinaWizyty.getItems();

                // Lista lekarzy
                ObservableList<String> lekarze = FXCollections.observableArrayList(lekarz.getItems());
                lekarz.setItems(lekarze);

                for (String lek : lekarze) {
                        Map<LocalDate, Map<String, Boolean>> terminy = new HashMap<>();
                        for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusMonths(1)); date = date.plusDays(1)) {
                                Map<String, Boolean> godzinyMap = new HashMap<>();
                                for (String godz : godziny) {
                                        godzinyMap.put(godz, false);
                                }
                                terminy.put(date, godzinyMap);
                        }
                        wizyty.put(lek, terminy);
                }
        }

        protected void specjalizacjaLekarza(String rodzajWizyty) {
                ObservableList<String> items = lekarz.getItems();
                // Wyczyszczenie listy i dodanie odpowiednich elementów w zależności od rodzaju wizyty
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
                // Resetowanie wyboru lekarza po zmianie rodzaju wizyty
                lekarz.setValue(null);
        }

        @FXML
        protected void wyswietlWiadomoscBrakDanych() {
                brakDanychWiadomosc.setVisible(true);
        }

        protected void usunWiadomoscBrakDanych() {
                brakDanychWiadomosc.setVisible(false);
        }

        @FXML
        protected void onGenerujButtonClick() {
                if (imiePacjenta.getText().isEmpty() || nazwiskoPacjenta.getText().isEmpty() || ulicaPacjenta.getText().isEmpty() ||
                        nrBudynku.getText().isEmpty() || miastoPacjenta.getText().isEmpty() ||
                        kodPocztowyPacjenta.getText().isEmpty() || dataWizyty.getValue() == null || godzinaWizyty.getValue() == null ||
                        rodzajWizyty.getValue() == null || lekarz.getValue() == null) {
                        wyswietlWiadomoscBrakDanych();
                } else {
                        if (nrMieszkania.getText().isEmpty()) {
                                nrMieszkania.setText("brak");
                        }
                        GeneratorPDF.generatePDF(imiePacjenta, nazwiskoPacjenta, ulicaPacjenta, nrBudynku, nrMieszkania,
                                miastoPacjenta, kodPocztowyPacjenta,
                                dataWizyty, godzinaWizyty, rodzajWizyty, lekarz);

                        // Usunięcie danych z wszystkich pól tekstowych
                        imiePacjenta.clear();
                        nazwiskoPacjenta.clear();
                        ulicaPacjenta.clear();
                        nrBudynku.clear();
                        nrMieszkania.clear();
                        miastoPacjenta.clear();
                        kodPocztowyPacjenta.clear();
                        dataWizyty.setValue(null);
                        godzinaWizyty.setValue(null);
                        rodzajWizyty.setValue(null);
                        lekarz.setValue(null);
                        usunWiadomoscBrakDanych();
                        dataWizyty.setDisable(true); // Resetowanie stanu DatePicker po usunięciu danych
                }
        }
}
