module org.example.gestaodehorario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.gestaodehorario to javafx.fxml;
    exports org.example.gestaodehorario;
    exports org.example.gestaodehorario.Controller;
    opens org.example.gestaodehorario.Controller to javafx.fxml;
    exports org.example.gestaodehorario.util;
    opens org.example.gestaodehorario.util to javafx.fxml;
    opens org.example.gestaodehorario.model to javafx.base;
}