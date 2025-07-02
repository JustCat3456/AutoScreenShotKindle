module kindlecapture {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires com.github.oshi;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    
    exports com.kindlecapture;
    exports com.kindlecapture.controller;
    exports com.kindlecapture.service;
    exports com.kindlecapture.util;
    
    opens com.kindlecapture to javafx.fxml;
    opens com.kindlecapture.controller to javafx.fxml;
}
