/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

//import authentication.UserBean;
import incident.Incident;
import incident.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        } catch (ClassNotFoundException | SQLException e) {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<String> getUnApprovedUsers() {
        List<String> users = new ArrayList<>();
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT username FROM user WHERE approved = 0");
                ResultSet rs = pStmnt.executeQuery();
                while (rs.next()) {
                    users.add(rs.getString("username"));
                }
            } catch (SQLException e) {
                return null;
            } finally {
                closeConnection();
            }
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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    public static boolean authIncident(String type, int priority, String locatie) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !type.trim().isEmpty() && !locatie.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("UPDATE incident SET approved = 1, priority = ? WHERE type = ? AND location = ?;");
                pStmnt.setInt(1, priority);
                pStmnt.setString(2, type);
                pStmnt.setString(3, locatie);

                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    public static boolean denyIncident(String type, String locatie) {
        boolean result = false;
        //Open the connection
        if (openConnection() && !type.trim().isEmpty() && !locatie.trim().isEmpty()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("DELETE FROM incident WHERE type = ? AND location = ?;;");
                pStmnt.setString(1, type);
                pStmnt.setString(2, locatie);

                if (pStmnt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    public static List<Incident> getIncidents(int approved) {
        List<Incident> incidents = new ArrayList<>();
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT type, location, longitude, latitude, submitter, description, date, priority,victims,dangergrade FROM incident WHERE approved = " + approved + ";");
                ResultSet results = pStmnt.executeQuery();
                while (results.next()) {
                    Incident incident = new Incident(
                            results.getString("location"),
                            results.getString("longitude"),
                            results.getString("latitude"),
                            results.getString("submitter"),
                            results.getString("type"),
                            results.getString("description"),
                            results.getString("date"),
                            results.getInt("priority"),
                            results.getString("victims"),
                            results.getString("dangergrade"));
                    incidents.add(incident);
                }
            } catch (SQLException ex) {
                System.out.println("Database exception: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }
        return incidents;
    }

    public static List<String> getAdviceById(int id) {
        List<String> advice = new ArrayList<>();
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT adviceText FROM advice WHERE id = ?");
                pStmnt.setInt(1, id);
                ResultSet results = pStmnt.executeQuery();
                while (results.next()) {
                    advice.add(results.getString("adviceText"));
                }
            } catch (SQLException ex) {
                System.out.println("Exception: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }
        return advice;
    }

    public static int getId(String typeOfIncident, String location, String submitter) {
        int id = -1;
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT id FROM incident WHERE type = '" + typeOfIncident + "' AND location = '" + location + "' AND submitter = '" + submitter + "'");
                ResultSet results = pStmnt.executeQuery();
                while (results.next()) {
                    id = results.getInt("id");
                }
            } catch (SQLException ex) {
                System.out.println("Exception: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }
        return id;
    }

    public static List<Message> getMessagesWithId(int id) {
        List<Message> messages = new ArrayList<>();
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("SELECT sender, messageText, datum FROM message WHERE incidentId = '" + id + "'");
                ResultSet results = pStmnt.executeQuery();
                while (results.next()) {
                    String sender = results.getString("sender");
                    String messageText = results.getString("messageText");
                    String date = results.getString("datum");
                    Message message = new Message(sender, messageText, id, date);
                    messages.add(message);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnection();
            }
        }
        return messages;
    }

    public void addMessage(Message message) {
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("INSERT INTO message(incidentId, sender, messageText, datum) VALUES(?, ?, ?, ?)");
                pStmnt.setInt(1, message.getIncidentId());
                pStmnt.setString(2, message.getSender());
                pStmnt.setString(3, message.getMessageText());
                pStmnt.setString(4, message.getDate());
                pStmnt.executeQuery();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnection();
            }

        }
    }

    public static void addAdvice(String advice, int incidentID) {
        if (openConnection()) {
            try {
                PreparedStatement pStmnt = connection.prepareStatement("INSERT INTO advice(id, adviceText) VALUES(?, ?)");
                pStmnt.setInt(1, incidentID);
                pStmnt.setString(2, advice);
                pStmnt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    

    public static List<String> getImagePaths(int id) {
        List<String> imagePaths = new ArrayList<String>();
        if (openConnection()) {
            try {
                PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM incidentfile where id = ?");
                stmnt.setInt(1, id);
                ResultSet results = stmnt.executeQuery();
                while (results.next()) {
                    imagePaths.add(results.getString("filename"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return imagePaths;
        }

        return null;
    }
}
