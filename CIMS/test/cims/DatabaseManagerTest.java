/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

import authentication.UserBean;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Joris
 */
public class DatabaseManagerTest {
    
    public DatabaseManagerTest() {
    }

    /**
     * Test of authenticateUser method, of class DatabaseManager.
     */
    @Test
    public void testAuthenticateUser() {
        //Fastest time measured: 442ms
        long firstTime = System.currentTimeMillis();
        UserBean user = new UserBean();
        user.setUserName("test");
        user.setPassword("test");
        Assert.assertTrue(DatabaseManager.authenticateUser(user).isValid());
        System.out.println("Operation took: " + (System.currentTimeMillis() - firstTime) + " milliseconds");
    }
    
    /**
     *  Test of AddUser method, of class DatabaseManager.
     */
    @Test
    public void testAddUser() {
        Assert.assertTrue(DatabaseManager.addUser("test", "maikelhoeks@hotmail.com", "test"));
    }
    
    @Test
    public void testgetIncidentTypes() {
        Assert.assertTrue(DatabaseManager.getIncidentTypes().size() > 1);
    }
}
