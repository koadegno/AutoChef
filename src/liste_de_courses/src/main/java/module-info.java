module com.example.liste_de_courses {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.liste_de_courses to javafx.fxml;
    exports com.example.liste_de_courses;
}