package org.example.klinika;

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

public class GeneratorPDF {
    public static void generatePDF(TextField imiePacjenta, TextField nazwiskoPacjenta, TextField ulicaPacjenta,
                                   TextField miastoPacjenta, TextField kodPocztowyPacjenta, DatePicker dataWizyty,
                                   ComboBox<String> godzinaWizyty, ComboBox<String> rodzajWizyty, ComboBox<String> lekarz) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType0Font.load(document, new File("TimesNewRoman.ttf")), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Imie: " + imiePacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Nazwisko: " + nazwiskoPacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("ul. : " + ulicaPacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Miasto: : " + miastoPacjenta.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Kod pocztowy: " + kodPocztowyPacjenta.getText());

                // Sprawdź czy data została wybrana przed próbą sformatowania
                LocalDate selectedDate = dataWizyty.getValue();
                if (selectedDate != null) {
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Data wizyty: " + selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                } else {
                    contentStream.newLineAtOffset(0, -20);
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

            document.save("terminWizyty.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
