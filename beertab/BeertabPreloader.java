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
            int progressValue = (int) ((ProgressNotification) info).getProgress();
            System.out.println(progressValue);
            BeertabPreloaderController.progresslabel.setText("Loading "+ ((progressValue)));
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
