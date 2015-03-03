/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

//import authentication.UserBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + Property.DBADDRESS.getProperty() + ":" + Property.DBPORT.getProperty() + "/" + Property.DBNAME.getProperty(),
                    Property.DBUSERNAME.getProperty(),
                    Property.DBPASSWORD.getProperty());
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
     * @param user The UserBean who's credentials to check
     * @return Returns a boolean if the password and username match or not
     */
    /**public static UserBean authenticateUser(UserBean user) {
        UserBean result = null;
        //Open connection
        if (openConnection()) {
            try {
                //Try to execute sql statment
                //Prepared statement van gemaakt voor de sql parameters
                PreparedStatement pStmnt = connection.prepareStatement("SELECT username, approved FROM user WHERE username = ? AND password = ?");
                pStmnt.setString(1, user.getUsername());
                pStmnt.setString(2, user.getPassword());

                ResultSet rs = pStmnt.executeQuery();
                //Check if password and username match
                if (rs.next()) {
                    String username = rs.getString("username");
                    if (username.equals(user.getUsername()) && rs.getInt("approved") == 1) {
                        user.setValid(true);
                        result = user;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            closeConnection();
        }
        return result;
    }*/

    /**
     * Creates a new user with the given variables
     *
     * @param username Username for the user
     * @param password Password for the user
     * @param email
     * @return Boolean. True if succesfull, false if not
     */
    public static boolean addUser(String username, String email, String password) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !username.trim().isEmpty() && !password.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("INSERT INTO user (username, email, password) VALUES(?, ?, ?);");
                pStmnt.setString(1, username);
                pStmnt.setString(2, email);
                pStmnt.setString(3, password);

                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        //Close connection
        closeConnection();
        return result;
    }
}
