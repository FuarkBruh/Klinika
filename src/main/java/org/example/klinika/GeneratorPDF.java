package org.example.klinika;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class GeneratorPDF {
    private static void informacjaSukces() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText("Zapisano do pliku PDF informacje o wizycie!");
        alert.showAndWait();
    }
    public static void generatePDF(TextField imiePacjenta, TextField nazwiskoPacjenta, TextField ulicaPacjenta, TextField nrBudynku,
                                   TextField nrMieszkania, TextField miastoPacjenta, TextField kodPocztowyPacjenta, DatePicker dataWizyty,
                                   ComboBox<String> godzinaWizyty, ComboBox<String> rodzajWizyty, ComboBox<String> lekarz) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                //Plik ttf do czcionki musi być dodany ręcznie z powodu błędów z tą biblioteką, bo niby ma swoje
                //wewnętrzne czcionki, ale trudno się do nich dostać
                contentStream.setFont(PDType0Font.load(document, new File("TimesNewRoman.ttf")), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Imie: " + imiePacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Nazwisko: " + nazwiskoPacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                if(Objects.equals(nrMieszkania.getText(), "brak")) {
                    contentStream.showText("ul. " + ulicaPacjenta.getText() + " " + nrBudynku.getText());
                }
                else {
                    contentStream.showText("ul. " + ulicaPacjenta.getText() + " " + nrBudynku.getText() + "/" + nrMieszkania.getText());
                }
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Miasto: " + miastoPacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Kod pocztowy: " + kodPocztowyPacjenta.getText());

                // Sprawdź czy data została wybrana przed próbą sformatowania
                LocalDate selectedDate = dataWizyty.getValue();
                contentStream.newLineAtOffset(0, -20);
                if (selectedDate != null) {
                    contentStream.showText("Data wizyty: " + selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                } else {
                    contentStream.showText("Data wizyty: [Nie wybrano]");
                }

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Godzina wizyty: " + godzinaWizyty.getValue());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Rodzaj wizyty: " + rodzajWizyty.getValue());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Lekarz: " + lekarz.getValue());

                contentStream.endText();
            }

            LocalDate data = dataWizyty.getValue();
            String dataString = data.toString();
            String nazwaPliku = imiePacjenta.getText() + "_" + nazwiskoPacjenta.getText() + dataString;
            String sciezkaDoZapisu = "wygenerowanePDFy\\";

            document.save(new File( sciezkaDoZapisu + nazwaPliku + ".pdf"));


            informacjaSukces();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
