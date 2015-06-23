/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cims;

import authentication.UserBean;
import static cims.DatabaseManager.connection;
import java.sql.*;
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
	 * Test of DDDD™: Database Duplicates Detection & Deletion™.
	 */
	@Test
	public void testDuplicateChecking() throws SQLException, ClassNotFoundException{
		/* Add new incident. */ 
		Assert.assertTrue("Nieuw incident niet toegevoegd.",        DatabaseManager.addIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Maatwerk.", 0, 0, "0", "Hoog"));
		Assert.assertTrue("Nieuw incident niet toegevoegd.",        DatabaseManager.addIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Meer maatwerk.", 0, 0, "0", "Hoog"));
		Assert.assertTrue("Nieuw incident niet toegevoegd.",        DatabaseManager.addIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Nog meer maatwerk.", 0, 0, "0", "Hoog"));
		/* Add incident again to test duplication prevention. */ 
		Assert.assertFalse("Bestaand incident opnieuw toegevoegd.", DatabaseManager.addIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Maatwerk.", 0, 0, "0", "Hoog"));
		Assert.assertFalse("Bestaand incident opnieuw toegevoegd.", DatabaseManager.addIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Meer maatwerk.", 0, 0, "0", "Hoog"));
		Assert.assertFalse("Bestaand incident opnieuw toegevoegd.", DatabaseManager.addIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Nog meer maatwerk.", 0, 0, "0", "Hoog"));
		/* Delete added incident. */ 
		Assert.assertTrue(DatabaseManager.deleteTestIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Maatwerk."));
		Assert.assertTrue(DatabaseManager.deleteTestIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Meer maatwerk."));
		Assert.assertTrue(DatabaseManager.deleteTestIncident("Fontys Hogescholen", "Rachelsmolen", "A-chan", "Nog meer maatwerk."));
	}
	
    /**
     *  Test of AddUser method, of class DatabaseManager.
     */
    @Test
    public void testAddUser() {
        Assert.assertTrue(DatabaseManager.addUser("test", "maikelhoeks@hotmail.com", "test"));
    }
    
    @Test
    public void testAddIncident() {
        Assert.assertTrue(DatabaseManager.addIncident("unitTest", "noplace", "Meny", "new description", 5.0002, 6.0002,"0","Hoog"));
        Assert.assertTrue(DatabaseManager.addIncident("unitTest", "noplace", "Meny", "", 5.0002, 6.0002,"1-5","Laag"));
        
        Assert.assertFalse(DatabaseManager.addIncident(null, null, null, null, 0, 0,null,null));
        Assert.assertFalse(DatabaseManager.addIncident("", "een plaats", "Meny", "niet adden",0,0, "0", "Laag"));
        Assert.assertFalse(DatabaseManager.addIncident("testing", "", "Meny", "niet adden",0,0, "2","Gemiddeld"));
        Assert.assertFalse(DatabaseManager.addIncident("testing", "something", "", "niet adden", 0, 0,"2","Hoog"));
    }
}
