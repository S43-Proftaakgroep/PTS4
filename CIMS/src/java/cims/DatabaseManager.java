/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

import authentication.UserBean;
import incident.Incident;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

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
    private static boolean openConnection()
    {
        boolean result;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + Property.DBADDRESS.getProperty() + ":" + Property.DBPORT.getProperty() + "/" + Property.DBNAME.getProperty(),
                    Property.DBUSERNAME.getProperty(),
                    Property.DBPASSWORD.getProperty());
            result = true;
        }
        catch (Exception e)
        {
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
    private static void closeConnection()
    {
        try
        {
            connection.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // check if a user with this username exists
    public static boolean checkUsername(String name)
    {
        if (openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT username, approved FROM user WHERE username = ?");
                pStmnt.setString(1, name);

                ResultSet rs = pStmnt.executeQuery();
                //User with this name exists
                if (rs.next())
                {
                    return true;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return false;
    }

    // check if a username with this email exists
    public static boolean checkEmail(String email)
    {
        if (openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT username, approved FROM user WHERE email = ?");
                pStmnt.setString(1, email);

                ResultSet rs = pStmnt.executeQuery();
                //Username with this email exists
                if (rs.next())
                {
                    return true;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return false;
    }

    /**
     * Checks if the username and password the user entered are correct.
     *
     * @param user The UserBean who's credentials to check
     * @return Returns a boolean if the password and username match or not
     */
    public static UserBean authenticateUser(UserBean user)
    {
        UserBean result = null;
        //Open connection
        if (openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT salt FROM user WHERE username = ?");
                pStmnt.setString(1, user.getUsername());
                ResultSet rs = pStmnt.executeQuery();
                String salt = "";
                if (rs.next())
                {
                    salt = rs.getString("salt");
                }
                else
                {
                    return null;
                }
                String encryptedString = encryptPassword(salt + user.getPassword() + salt);
                //Try to execute sql statment
                //Prepared statement van gemaakt voor de sql parameters
                pStmnt = connection.prepareStatement("SELECT username, approved FROM user WHERE username = ? AND password = ?");
                pStmnt.setString(1, user.getUsername());
                pStmnt.setString(2, encryptedString);

                rs = pStmnt.executeQuery();
                //Check if password and username match
                if (rs.next())
                {
                    String username = rs.getString("username");
                    if (username.equals(user.getUsername()) && rs.getInt("approved") == 1)
                    {
                        user.setValid(true);
                        result = user;
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return result;
    }

    /**
     * Creates a new user with the given variables
     *
     * @param username Username for the user
     * @param password Password for the user
     * @param email
     * @return Boolean. True if succesfull, false if not
     */
    public static boolean addUser(String username, String email, String password)
    {
        boolean result = false;
        //Open the connection
        if (openConnection() && !username.trim().isEmpty() && !password.trim().isEmpty())
        {
            try
            {
                String salt = "";
                Random r = new Random();
                for (int i = 1; i < 16; i++)
                {
                    salt += (char) (r.nextInt(30) + 33);
                }
                password = salt + password + salt;
                String encryptedString = encryptPassword(password);
                PreparedStatement pStmnt = connection.prepareStatement("INSERT INTO user (username, email, password, salt) VALUES(?, ?, ?, ?);");
                pStmnt.setString(1, username);
                pStmnt.setString(2, email);
                pStmnt.setString(3, encryptedString);
                pStmnt.setString(4, salt);

                if (pStmnt.executeUpdate() > 0)
                {
                    result = true;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return result;
    }

    private static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(password.getBytes("UTF-8"));
        //String encryptedString = new String(messageDigest.digest());
        byte[] hashedByteArray = messageDigest.digest();
        String encryptedString = Base64.getEncoder().encodeToString(hashedByteArray);
        return encryptedString;
    }

    public static boolean addIncident(String type, String locatie, String submitter, String description, double longitude, double latitude)
    {
        boolean result = false;
        if(type == null || locatie == null || submitter == null) {
            return false;
        }
        //Open the connection
        if (openConnection() && !type.trim().isEmpty() && !locatie.trim().isEmpty() && !submitter.trim().isEmpty())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("INSERT INTO incident (type, location, submitter, description, longitude, latitude) VALUES(?, ?, ?, ?, ?, ?);");
                pStmnt.setString(1, type);
                pStmnt.setString(2, locatie);
                pStmnt.setString(3, submitter);
                pStmnt.setString(4, description);
                pStmnt.setDouble(5, longitude);
                pStmnt.setDouble(6, latitude);

                if (pStmnt.executeUpdate() > 0)
                {
                    result = true;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return result;
    }

    public static List<Incident> getIncidents()
    {
        List<Incident> approvedIncidents = new ArrayList<>();
        if (openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT type, location, submitter, description, date, longitude, latitude FROM incident WHERE approved = 1 ORDER BY date DESC;");
                ResultSet results = pStmnt.executeQuery();
                while (results.next())
                {
                    Incident incident = new Incident(
                            results.getString("location"),
                            results.getDouble("longitude"),
                            results.getDouble("latitude"),
                            results.getString("submitter"),
                            results.getString("type"),
                            results.getString("description"),
                            results.getString("date"));
                    approvedIncidents.add(incident);
                }
            }
            catch (Exception ex)
            {
                System.out.println("Database exception: " + ex.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return approvedIncidents;
    }

    public static int getId(String typeOfIncident, String location, String submitter)
    {
        int id = -1;
        if (openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT id FROM incident WHERE type = '" + typeOfIncident + "' AND location = '" + location + "' AND submitter = '" + submitter + "'");
                ResultSet results = pStmnt.executeQuery();
                while (results.next())
                {
                    id = results.getInt("id");
                }
            }
            catch (SQLException ex)
            {
                System.out.println("Exception: " + ex.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return id;
    }

    public static Incident getIncientById(int id)
    {
        Incident incident = null;
        if (openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT * FROM incident WHERE id = ? AND approved = 1");
                pStmnt.setInt(1, id);
                ResultSet results = pStmnt.executeQuery();
                while (results.next())
                {
                    incident = new Incident(
                            results.getString("location"),
                            results.getDouble("longitude"),
                            results.getDouble("latitude"),
                            results.getString("submitter"),
                            results.getString("type"),
                            results.getString("description"),
                            results.getString("date"));
                }
            }
            catch (SQLException ex)
            {
                System.out.println("Exception: " + ex.getMessage());
            } finally
            {
                closeConnection();
            }
        }
        return incident;
    }
    
    public static List<String> getAdviceById(int id)
    {
        List<String> advice = new ArrayList<>();
        if(openConnection())
        {
            try
            {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT adviceText FROM advice WHERE id = ?");
                pStmnt.setInt(1, id);
                ResultSet results = pStmnt.executeQuery();
                while (results.next())
                {
                    advice.add(results.getString("adviceText"));
                }
            }
            catch(SQLException ex)
            {
                System.out.println("Exception: " + ex.getMessage());
            }
            finally
            {
                closeConnection();
            }
        }
        return advice;
    }
}
