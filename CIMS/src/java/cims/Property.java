/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

/**
 *
 * @author maikel
 */
public enum Property {
    DBADDRESS("dburl"),
    DBPORT("dbport"),
    DBUSERNAME("dbusername"),
    DBPASSWORD("dbpassword"),
    DBNAME("dbname"),
    IPADRESS("ipadress"),
    IPPORT("ipport");
    
    private String propertyName;
    private Property(final String propertyName){
        this.propertyName = propertyName;
    }
    public String getProperty(){
       return PropertiesController.getSettings().getProperty(propertyName);
    }
}
