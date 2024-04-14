package org.example.klinika;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerKlinika {
    // W kontrolerze, należy dodać tę metodę
    public String getFactory(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

}