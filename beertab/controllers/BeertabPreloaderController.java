package beertab.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public class BeertabPreloaderController implements Initializable {

    public static Label progresslabel;
    public static ProgressBar progbar;

    @FXML
    private Label splashlabel;

    @FXML
    private ProgressBar pbar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.splashlabel.setText("Loading");
        progresslabel = splashlabel;

        this.pbar.setProgress(0.0);
        progbar = pbar;

    }
}
