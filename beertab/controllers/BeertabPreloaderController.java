package beertab.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BeertabPreloaderController implements Initializable {

    public static Label progresslabel;

    @FXML
    private Label splashlabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.splashlabel.setText("Loading");
        progresslabel = splashlabel;
    }
}
