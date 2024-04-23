package org.example.klinika;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
//W sumie tutaj nic nie robiłem, tylko plik fxml wskazałem
public class StarterKlinika extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StarterKlinika.class.getResource("uiKlinika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        // Poprawiona ścieżka do pliku obrazu
        String imageUrl = StarterKlinika.class.getResource("/org/example/klinika/eskulap.jpg").toExternalForm();
        stage.getIcons().add(new Image(imageUrl));

        stage.setTitle("Aplikacja Klinika");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
