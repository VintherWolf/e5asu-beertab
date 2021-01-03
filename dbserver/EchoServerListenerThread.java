

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;




public class EchoServerListenerThread extends Thread {

    private Socket socket;

    public EchoServerListenerThread(Socket socket) {
        super("EchoServerListenerThread");
        this.socket = socket;
    }

    public static List<String> retData = new ArrayList<String>();

    private static String url = "jdbc:postgresql://localhost:8181/beertab";
    private static String user = "javameister";

    //private password = System.getenv("POSTGRES_PASS");
    private static String password = "example";

    public static void insertData () {

        String query = "INSERT INTO customertable(" +
                "ctable, customer, beverage, quantity, cost)" +
                "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT VERSION()")) {

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

            PreparedStatement pst = con.prepareStatement(query);

            String customer = "Bob";
            String beverage = "Ale";

            pst.setInt(1, 7);
            pst.setString(2, customer);
            pst.setString(3, beverage);
            pst.setInt(4, 6);
            pst.setInt(5, 60);
            pst.executeUpdate();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(EchoServerListenerThread.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        System.out.format("Done sending data%n");
    }

    public static void retrieveData() {

        try (Connection con = DriverManager.getConnection(url, user, password);

             PreparedStatement pst = con.prepareStatement("SELECT * FROM customertable");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                int customerId =  rs.getInt(1);
                int table = rs.getInt(2);
                String customer = rs.getString(3);
                String beverage = rs.getString(4);
                int quantity = rs.getInt(5);
                int cost = rs.getInt(6);

                JSONObject jsonobj = new JSONObject();
                StringWriter out = new StringWriter();

                jsonobj.put("customerId", customerId);
                jsonobj.put("table", table);
                jsonobj.put("customer", customer);
                jsonobj.put("beverage", beverage);
                jsonobj.put("quantity", quantity);
                jsonobj.put("cost", cost);

                jsonobj.writeJSONString(out);
                String jsonText = out.toString();

                retData.add(jsonText);
            }


        } catch (SQLException | IOException ex) {

            Logger lgr = Logger.getLogger(EchoServerListenerThread.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        System.out.println("Ret Data: " + retData);

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

                    retrieveData();


                    if(retData != null)
                    {
                        System.out.println("Retrieving data from db!");

                        StringBuilder strbul=new StringBuilder();

                        for(String str : retData)
                        {
                            strbul.append(str);
                            //for adding comma between elements
                            strbul.append(",");
                            out.println(str);
                        }



                        outputLine = strbul.toString();

                        //out.println(outputLine);

                    }
                    else
                    {
                        out.println("No data in database\r\n");
                    }

                }
                else {
                    outputLine = "Bye";
                    out.println(outputLine);
                }
                //System.out.println("Sending: " + outputLine);

                if (outputLine.equals("Bye"))
                    break;
            }
            System.out.println("Closing connection!");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
