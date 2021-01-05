package beertab;

import beertab.controllers.BeertabController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Beertab extends Application {

    private static final int MAXCOUNT = 6;
    private int check = 1;

    public static List<String> retData = new ArrayList<String>();


    @Override
    public void init() throws Exception
    {
        System.out.printf("init() called on thread %s%n",
                Thread.currentThread());
        System.out.println("Hello from main init");

        // Initialize stuff here and splashscreen is enabled as long
        // as init is ongoing
        //longStart();
        Thread.sleep(500);

        // Load CustomerTable from Database
        notifyPreloader(new Preloader.ProgressNotification(Main.conDbStart));
        Thread.sleep(2000);
        check = connectDatabase("127.0.0.1", 8182);
        if (check != 0) {

          switch (check) {
                case -1:
                    notifyPreloader(new Preloader.ProgressNotification(Main.conDbFailUnknownHost));
                    break;
                case -2:
                    notifyPreloader(new Preloader.ProgressNotification(Main.conDbFailIO));
                    break;
                default:
                    notifyPreloader(new Preloader.ProgressNotification(Main.conDbFailUnknownError));
                    break;
            }
        }
        else {
            notifyPreloader(new Preloader.ProgressNotification(Main.conDbSuccess));
        }
        Thread.sleep(2000);

        // All done
        notifyPreloader(new Preloader.ProgressNotification(100));
        Thread.sleep(2000);
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

    private int connectDatabase(String host, int port) throws Exception {

        try (
                Socket dbsocket = new Socket(host, port);
                PrintWriter out =
                        new PrintWriter(dbsocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(dbsocket.getInputStream()));
        ) {
            // Send to DatabaseServerListener
            out.println("retrieve data\r\n");
            Thread.sleep(100);

            // Received table rows from database
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                if (inputLine.equals("Bye"))
                {
                    break;
                }
                retData.add(inputLine);
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    host);
            return -2;
        }
        return 0;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            System.out.printf("start() called on thread %s%n",
                    Thread.currentThread());
            System.out.println("Hello from main Start");

            Circle ball = new Circle(20, Color.LIGHTYELLOW);

            //Setting the x, y position of the ball
            ball.setCenterX(20);
            ball.setCenterY(20);

            //Setting the stroke width of the circle
            ball.setStrokeWidth(20);

            Parent root = FXMLLoader.load(getClass().getResource("fxml/beertab.fxml"));
            Scene scene = new Scene(root);
            BeertabController.ballpane.getChildren().add(ball);
            primaryStage.setTitle("Beverage Account");
            primaryStage.setScene(scene);

            // After the app is ready, show the stage
            primaryStage.show();

            // Generate Beer pingpong Ball
            Bounds bounds = BeertabController.ballpane.getBoundsInParent();

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                    new EventHandler<ActionEvent>() {

                double dx = (double)  (Math.random() * 3) + 1; //Step on x or velocity
                int ydirection = 1;
                int xdirection = 1;

                @Override
                public void handle(ActionEvent t) {


                    double dy = (double)  (Math.random() * 4) + 0; //Step on y

                    boolean atBottomBorder = (ball.getLayoutY() + ball.getRadius()) >= 340;
                    boolean atTopBorder = (ball.getLayoutY() + ball.getRadius()) < 20;

                    if (atBottomBorder) {
                        ydirection = -1;
                    }

                    if (atTopBorder) {
                        ydirection = 1;
                    }

                    //If the ball reaches the left or right border make the step negative
                    boolean atRightBorder = (ball.getLayoutX() + ball.getRadius()) >= 510;
                    boolean atLeftBorder = (ball.getLayoutX() + ball.getRadius()) < 20;

                    if (atRightBorder) {
                        xdirection = -1;
                    }

                    if (atLeftBorder) {
                        xdirection = 1;
                    }

                    //move the ball
                    ball.setLayoutX(ball.getLayoutX() + dx * xdirection);
                    ball.setLayoutY(ball.getLayoutY() + dy * ydirection);

                }
            }));

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();


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
