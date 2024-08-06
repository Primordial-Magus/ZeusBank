module com.jmc.mazebank {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.jmc.zeusbank to javafx.fxml;
    exports com.jmc.zeusbank;
    exports com.jmc.zeusbank.Controllers;
    exports com.jmc.zeusbank.Controllers.Admin;
    exports com.jmc.zeusbank.Controllers.Client;
    exports com.jmc.zeusbank.Models;
    exports com.jmc.zeusbank.Views;
}