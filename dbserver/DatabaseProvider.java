package dbserver;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;

public class DatabaseProvider {

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

            Logger lgr = Logger.getLogger(DatabaseProvider.class.getName());
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

                System.out.println(customerId + "  " +
                        table + "  " +
                        customer +  "  " +
                        beverage + "  " +
                        quantity + "  " +
                        cost);
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DatabaseProvider.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static void main(String[] args) {

        /*
            1. Create database in psql
                # CREATE DATABASE beertab;
                # \l
            2. Create Table if not exists in beertab (psql# \connect beertab)
                # CREATE TABLE IF NOT EXISTS customertable (
                    customerId serial PRIMARY KEY,
                    ctable INT,
                    Customer VARCHAR(10),
                    Beverage VARCHAR(10),
                    quantity INT,
                    cost INT
                );
             3. Insert data into table
                # INSERT INTO customertable(
                    ctable, Customer, Beverage, quantity, cost)
                # VALUES(1, 'John Doe', 'IPA', 3, 50);

             Expected to return: INSERT 0 1

             test: # SELECT * FROM customertable;

         */



        if (password == null) {
            System.out.format("POSTGRES_PASS env var not set!%n");
            System.exit(-1);
        }



        // Insert test data in customertable
        insertData();

        // Retrieve data from customertable
        retrieveData();

    }
}
