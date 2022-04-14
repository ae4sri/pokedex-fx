module pokedex.app.pokedex {
    requires javafx.controls;
    requires javafx.fxml;

    opens pokedex.app to javafx.fxml;
    exports pokedex.app;
}