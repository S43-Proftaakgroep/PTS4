/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author maikel
 */
public enum Property {
    DBADDRESS("dburl"),
    DBPORT("dbport"),
    DBUSERNAME("dbusername"),
    DBPASSWORD("dbpassword"),
    DBNAME("dbname");
    
    private String propertyName;
    private Property(final String propertyName){
        this.propertyName = propertyName;
    }
    public String getProperty(){
       return PropertiesController.getSettings().getProperty(propertyName);
    }
}
