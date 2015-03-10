/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

//import authentication.UserBean;
import incident.Incident;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    
    public static List<String> getUnApprovedUsers()
    {
        List<String> users = new ArrayList<>();
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT username FROM user WHERE approved = 0");
                ResultSet rs = pStmnt.executeQuery();
                while (rs.next()) {
                    users.add(rs.getString("username"));
                }
            }
            catch(Exception e)
            {
                return null;
            }
            finally {closeConnection();}
        }
        return users;
    }

    /**
     * Approves a user with the given username
     *
     * @param username Username for the user
     * @return Boolean. True if succesfull, false if not
     */
    public static boolean authUser(String username) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !username.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("UPDATE user SET approved = 1 WHERE username = ?;");
                pStmnt.setString(1, username);

                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally{closeConnection();}
        }
        return result;
    }
    
     public static boolean authIncident(String type, String locatie ) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !type.trim().isEmpty() && !locatie.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("UPDATE incident SET approved = 1 WHERE type = ? AND location = ?;");
                pStmnt.setString(1, type);
                pStmnt.setString(2, locatie);

                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally{closeConnection();}
        }
        return result;
    }
     
      public static boolean denyIncident(String type , String locatie) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !type.trim().isEmpty() && !locatie.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("DELETE incident WHERE type = ? AND location = ?;;");
                pStmnt.setString(1, type);
                pStmnt.setString(2, locatie);
                
                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally{closeConnection();}
        }
        return result;
    }
    
    /**
     * Denies a user with the given username
     *
     * @param username Username for the user
     * @return Boolean. True if succesfull, false if not
     */
    public static boolean denyUser(String username) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !username.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("UPDATE user SET approved = -1 WHERE username = ?;");
                pStmnt.setString(1, username);

                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally{closeConnection();}
        }
        return result;
    }
    
    public static List<Incident> getIncidents()
    {
        List<Incident> unapprovedIncidents = new ArrayList<>();
        if(openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT type, location, submitter, description, date FROM incident WHERE approved = 0;");
                ResultSet results = pStmnt.executeQuery();
                while (results.next()) {
                    Incident incident = new Incident(results.getString("location"), results.getString("submitter"), results.getString("type"), results.getString("description"), results.getString("date"));
                    unapprovedIncidents.add(incident);
                }
            }
            catch(Exception ex)
            {
                System.out.println("Database exception: " + ex.getMessage());
            }
            finally
            {
                closeConnection();
            }
        }
        return unapprovedIncidents;
    }
}
