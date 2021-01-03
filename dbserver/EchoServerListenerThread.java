import dbserver.DatabaseProvider;

import java.net.*;
import java.io.*;

public class EchoServerListenerThread extends Thread {
    private Socket socket;

    public EchoServerListenerThread(Socket socket) {
        super("EchoServerListenerThread");
        this.socket = socket;
    }

    public void run() {

        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()))
        ) {
            String inputLine, outputLine;
            System.out.println("Received connection!");
            outputLine = "test";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (inputLine.equals("retrieve data")) {
                    System.out.println("Received OK: " + inputLine);
                    out.println(outputLine);
                    //out.println(DatabaseProvider.retrieveData());
                }
                //System.out.println("Sending: " + outputLine);

                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
