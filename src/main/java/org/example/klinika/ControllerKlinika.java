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
        private final Map<String, Map<LocalDate, Map<String, Boolean>>> wizyty = new HashMap<>();

        @FXML
        protected void initialize() {
                // Ustawienie minimalnej daty w DatePicker
                dataWizyty.setConverter(new LocalDateStringConverter());
                dataWizyty.setDayCellFactory(picker -> new DateCell() {
                        public void updateItem(LocalDate date, boolean empty) {
                                super.updateItem(date, empty);
                                setDisable(empty || date.isBefore(LocalDate.now()));
                        }
                });

                // Inicjalizacja ComboBoxa rodzajWizyty
                rodzajWizyty.setItems(FXCollections.observableArrayList(rodzajWizyty.getItems()));
                rodzajWizyty.setOnAction(event -> specjalizacjaLekarza(rodzajWizyty.getValue()));

                // Ustawienie DatePicker jako nieaktywny na początku
                dataWizyty.setDisable(true);
                godzinaWizyty.setDisable(true);

                // Listener do ComboBoxa lekarz, aby włączyć DatePicker po wybraniu lekarza
                lekarz.valueProperty().addListener((observable, oldValue, newValue) -> {
                        godzinaWizyty.setDisable(newValue == null);
                        dataWizyty.setDisable(newValue == null);
                        if (newValue != null) {
                                aktualizujDostepneGodziny();
                        }
                });

                // Listener do DatePicker, aby zaktualizować dostępne godziny po wyborze daty
                dataWizyty.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && lekarz.getValue() != null) {
                                aktualizujDostepneGodziny();
                        }
                });

                // Inicjalizacja listy godzin dla wizyt
                ObservableList<String> godziny = FXCollections.observableArrayList(godzinaWizyty.getItems());
                godzinaWizyty.setItems(godziny);

                // Inicjalizacja mapy dla utworzenia kalendarza dla danego lekarza
                initializeWizyty();
        }

        private void initializeWizyty() {
                ObservableList<String> godziny = godzinaWizyty.getItems();

                // Lista lekarzy
                ObservableList<String> lekarze = FXCollections.observableArrayList(lekarz.getItems());
                lekarz.setItems(lekarze);

                for (String lek : lekarze) {
                        Map<LocalDate, Map<String, Boolean>> terminy = new HashMap<>();
                        LocalDate startDate = LocalDate.now();
                        for (int i = 0; i < 30; i++) {
                                LocalDate date = startDate.plusDays(i);
                                Map<String, Boolean> godzinyMap = new HashMap<>();
                                for (String godz : godziny) {
                                        godzinyMap.put(godz, false);
                                }
                                terminy.put(date, godzinyMap);
                        }
                        wizyty.put(lek, terminy);
                }
        }

        private void aktualizujDostepneGodziny() {
                if (lekarz.getValue() != null && dataWizyty.getValue() != null) {
                        Map<String, Boolean> godzinyMap = wizyty.get(lekarz.getValue()).get(dataWizyty.getValue());
                        if (godzinyMap != null) {
                                ObservableList<String> dostepneGodziny = FXCollections.observableArrayList();
                                for (Map.Entry<String, Boolean> entry : godzinyMap.entrySet()) {
                                        if (!entry.getValue()) {
                                                dostepneGodziny.add(entry.getKey());
                                        }
                                }
                                FXCollections.sort(dostepneGodziny); // Sortowanie godzin
                                godzinaWizyty.setItems(dostepneGodziny);
                        }
                }
        }

        private void obslugaTerminowWizyt(String lekarz, LocalDate data, String godzina) {
                Map<LocalDate, Map<String, Boolean>> terminyLekarza = wizyty.get(lekarz);
                if (terminyLekarza != null) {
                        Map<String, Boolean> godziny = terminyLekarza.get(data);
                        if (godziny != null) {
                                godziny.put(godzina, true);
                        }
                }
        }

        protected void specjalizacjaLekarza(String rodzajWizyty) {
                ObservableList<String> items = FXCollections.observableArrayList();
                if (rodzajWizyty != null) {
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
                lekarz.setItems(items);
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
                                miastoPacjenta, kodPocztowyPacjenta, dataWizyty, godzinaWizyty, rodzajWizyty, lekarz);

                        // Zapisz wizytę w mapie
                        obslugaTerminowWizyt(lekarz.getValue(), dataWizyty.getValue(), godzinaWizyty.getValue());

                        // Aktualizacja dostępnych godzin
                        aktualizujDostepneGodziny();

                        // Usunięcie danych z wszystkich pól tekstowych
                        wyczyszczeniePol();
                        usunWiadomoscBrakDanych();
                        dataWizyty.setDisable(true); // Resetowanie stanu DatePicker po usunięciu danych
                }
        }

        private void wyczyszczeniePol() {
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
        }
}
