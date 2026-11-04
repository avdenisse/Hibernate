module org.example.practicahibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;




    opens org.example.practicahibernate to javafx.fxml;
    exports org.example.practicahibernate;
}