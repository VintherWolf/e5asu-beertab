package beertab;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Beertab extends Application {

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
        connectDatabase("127.0.0.1", 4444);
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

    private void connectDatabase(String host, int port) throws Exception {

        try (
            Socket echoSocket = new Socket(host, port);
            PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in))
        ) {
            out.println("Hello from echo Client!");
            System.out.println("echo: " + in.readLine());
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                host);
            System.exit(1);
        }
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
