module com.jmc.kronosbank {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.jmc.kronosbank to javafx.fxml;
    exports com.jmc.kronosbank;
    exports com.jmc.kronosbank.Controllers;
    exports com.jmc.kronosbank.Controllers.Admin;
    exports com.jmc.kronosbank.Controllers.Client;
    exports com.jmc.kronosbank.Models;
    exports com.jmc.kronosbank.Views;
}