package beertab;

import beertab.controllers.BeertabController;
import beertab.entities.CustomerTable;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Beertab extends Application {

    private static final int MAXCOUNT = 6;
    private int check = 1;

    public List<String> retData = new ArrayList<String>();


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
                System.out.println("Received from server: " + inputLine);

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
