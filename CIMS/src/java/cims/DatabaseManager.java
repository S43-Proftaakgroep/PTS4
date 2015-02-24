/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

import java.sql.Connection;
import java.sql.DriverManager;
import cims.Property;
import java.sql.ResultSet;
import java.sql.Statement;

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
    
     /**
     * Closes the database connection
     */
    private static void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
     /**
     * Checks if the username and password the user entered are correct.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return Returns a boolean if the password and username match or not
     */
    public static boolean authenticateUser(String username, String password) {
        boolean result = false;
        //Open connection
        if (openConnection()) {
            try {
                //Try to execute sql statment
                Statement stmnt = connection.createStatement();
                String SQL = "SELECT username, password FROM user WHERE username = '";
                SQL += username + "'" + " AND password = " + "'" + password + "'" + ";";
                ResultSet rs = stmnt.executeQuery(SQL);
                //Check if password and username match
                if (rs.next()) {
                    result = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            closeConnection();
        }
        return result;
    }
    
    public static void main(String[] args) {
        DatabaseManager dm = new DatabaseManager();
        dm.openConnection();
        System.out.println(dm.authenticateUser("test", "test")); 
    }
}
