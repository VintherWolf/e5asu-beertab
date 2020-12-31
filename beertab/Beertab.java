package beertab;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Beertab extends Application {

    private static int splashCounter = 0;
    private static final int MAXCOUNT = 6;


    @Override
    public void init() throws Exception
    {
        System.out.printf("init() called on thread %s%n",
                Thread.currentThread());
        System.out.println("Hello from main init");

        // Initialize stuff here and splashscreen is enabled as long
        // as init is ongoing
        longStart();
        Thread.sleep(200);
        // TODO:
        // 1. Implement Database load
        // 2. Preloader image
    }

    private void longStart() {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                System.out.printf("Longstart called on thread %s%n",
                        Thread.currentThread());
                // Send event to PreLoader (SplashScreen)
                for (int i = 0; i < MAXCOUNT ; i++) {
                    int progress = ((100 * i) / MAXCOUNT) ;
                    notifyPreloader(new Preloader.ProgressNotification(progress));
                    Thread.sleep(200);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            System.out.printf("start() called on thread %s%n",
                    Thread.currentThread());
            System.out.println("Hello from main Start");

            Parent root = FXMLLoader.load(getClass().getResource("fxml/beertab.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Beverage Account");
            primaryStage.setScene(scene);

            // After the app is ready, show the stage
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop()
    {
        System.out.printf("stop() called on thread %s%n",
                Thread.currentThread());
        // TODO:
        // 1. Implement Database save
    }


}
