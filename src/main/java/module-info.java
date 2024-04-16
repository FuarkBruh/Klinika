module org.example.klinika {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.apache.pdfbox;

    opens org.example.klinika to javafx.fxml;
    exports org.example.klinika;
}