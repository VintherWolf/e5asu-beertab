package beertab;

import beertab.controllers.BeertabPreloaderController;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class BeertabPreloader extends Preloader {

    private Stage preloaderstage;

    @Override
    public void init() throws Exception
    {
        System.out.println("Hello from preloader Init");

    }

    @Override
    public void start (Stage stage) throws Exception
    {
        System.out.println("Hello from preloader Start");
        this.preloaderstage = stage;

        BorderPane parent = (BorderPane) FXMLLoader.load(getClass().getResource("fxml/preloader.fxml"));
        Scene scene = new Scene(parent);


        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setTitle("Preloader");
        stage.show();

        Thread.sleep(500);

    }


    @Override
    public void handleApplicationNotification(Preloader.PreloaderNotification info)
    {
        if (info instanceof ProgressNotification) {
            double progressValue = ((ProgressNotification) info).getProgress();

            if (progressValue > 0 && progressValue <= 100) {
                BeertabPreloaderController.progbar.setProgress(progressValue/100);
            }

            if ((int) progressValue >= Main.conDbStart && (int) progressValue < Main.conDbSuccess) {

                switch((int)progressValue) {

                    case (int) Main.conDbStart:
                        BeertabPreloaderController.progresslabel.setText("Loading Tables from Database");
                        break;
                    case (int) Main.conDbFailUnknownHost:
                        BeertabPreloaderController.progresslabel.setText("Failed to Connect to DB (Unknown Host)");
                        break;
                    case (int) Main.conDbFailIO:
                        BeertabPreloaderController.progresslabel.setText("Failed to Connect to DB (IO Error)");
                        break;
                    case (int) Main.conDbFailUnknownError:
                        BeertabPreloaderController.progresslabel.setText("Failed to Connect to DB (Unknown Error)");
                        break;
                    default:
                        BeertabPreloaderController.progresslabel.setText("Unexpected Error Occurred!");
                        break;
                }

            }

            if (progressValue == Main.conDbSuccess) {
                BeertabPreloaderController.progresslabel.setText("Success!");
            }

            if (progressValue == 100) {
                BeertabPreloaderController.progresslabel.setText("All Done! Starting Application");
            }


        }

    }

    @Override
    public void handleProgressNotification(ProgressNotification pn)
    {
        System.out.println("Progress: " + pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info)
    {

        StateChangeNotification.Type type = info.getType();
        switch (type) {

            case BEFORE_INIT:
                break;

            case BEFORE_START:
                System.out.println("Before Start");
                System.out.println("Hiding preloader");
                this.preloaderstage.hide();
                break;

            case BEFORE_LOAD:
                break;
        }
    }

}
