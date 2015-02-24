/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

import java.sql.Connection;
import java.sql.DriverManager;
import cims.Property;

/**
 *
 * @author Joris
 */
public class DatabaseManager {
    public static Connection connection;

    /**
     * Opens the connection to the database
     *
     * @return Returns true if succesfull, else returns false
     */
    private static boolean openConnection() {
        boolean result;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Property.DBADDRESS.getProperty() + ":"+ Property.DBPORT.getProperty()+ "/"+Property.DBNAME.getProperty(), Property.DBUSERNAME.getProperty(), Property.DBPASSWORD.getProperty());
            result = true;
        } catch (Exception e) {
            connection = null;
            System.out.println(e.getMessage());
            System.out.println("Connection failed");
            result = false;
        }
        return result;
    }
}
