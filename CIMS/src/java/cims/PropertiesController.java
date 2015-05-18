/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Static class for changing and getting the properties
 *
 * @author Jur
 */
public final class PropertiesController {

    private static Properties props = new Properties();

    /**
     * Gets the Properties of the game
     *
     * @return The properties file this controller manages
     */
    public static Properties getSettings() {
        //Check if properties is not null
        if (props == null) {
            props = new Properties();
            loadProperties();
        }
        //Check if properties are correct,
        //if so return properties
        if (isCorrectlyConfigured()) {
            return props;
        } //Else return new configured properties
        else {
            resetProperties();
            return props;
        }
    }

    /**
     * Loads the properties file from the root folder of the applications
     *
     * @return True if success, false if failed
     */
    public static boolean loadProperties() {
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");

            // load a properties file
            props.load(input);

            if (!isCorrectlyConfigured()) {
                resetProperties();
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if(resetProperties())
            {
                return true;
            }
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Writes the properties file at the root folder of the application with
     * speciefied parameters. All parameters must have a value.
     *
     * @param dbUrl The url of the database
     * @param dbPort The port of the databse
     * @param dbUsername The username to the database
     * @param dbPassword The password to the database
     * @return If succesfully written or not
     */
    public static boolean writeProperties(
            String dbUrl, String dbPort, String dbUsername, String dbPassword, String dbName, String ipAdress, String ipPort) {
        OutputStream output = null;
        if (dbUrl.isEmpty() || dbPort.isEmpty() || dbUsername.isEmpty() || dbPassword.isEmpty() || dbName.isEmpty()) {
            return false;
        } else {
            try {

                output = new FileOutputStream("config.properties");

                //Set database properties
                props.setProperty("dburl", dbUrl);
                props.setProperty("dbport", dbPort);
                props.setProperty("dbusername", dbUsername);
                props.setProperty("dbpassword", dbPassword);
                props.setProperty("dbname", dbName);
                props.setProperty("ipadress", ipAdress);
                props.setProperty("ipport", ipPort);

                if (!isCorrectlyConfigured()) {
                    resetProperties();
                }
                // save properties to project root folder
                props.store(output, null);

            } catch (IOException io) {
                System.out.println(io.getMessage());
                return false;
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the properties file is correctly configured
     *
     * @return False if not correctly configured, otherwise true.
     */
    public static boolean isCorrectlyConfigured() {
        if (props == null) {
            return false;
        }
        if (!props.containsKey("dburl")) {
            return false;
        }
        if (!props.containsKey("dbport")) {
            return false;
        }
        if (!props.containsKey("dbusername")) {
            return false;
        }
        if (!props.containsKey("dbpassword")) {
            return false;
        }
        if (!props.containsKey("dbname")) {
            return false;
        }
        if (!props.containsKey("ipadress")) {
            return false;
        }
        if (!props.containsKey("ipport")) {
            return false;
        }
        return true;
    }

    /**
     * Resets the properties file to its original state
     *
     * @return True if succesfull, false if failed
     */
    private static boolean resetProperties() {
        try {

            OutputStream output = new FileOutputStream("config.properties");

            //Set database properties
//            props.setProperty("dburl", "www.douven.me");
//            props.setProperty("dbport", "3306");
//            props.setProperty("dbusername", "remoteuser");
//            props.setProperty("dbpassword", "420blaze");
//            props.setProperty("dbname", "cims");
            
            props.setProperty("dburl", "a-chan.nl");
            props.setProperty("dbport", "3306");
            props.setProperty("dbusername", "deb82648_pts4");
            props.setProperty("dbpassword", "watdenkjezelf");
            props.setProperty("dbname", "deb82648_cims");
            //props.setProperty("ipadress", "145.93.104.191");
            props.setProperty("ipadress", "145.93.162.23");
            props.setProperty("ipport", "1099");
            //Save properties to project root folder
            props.store(output, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
