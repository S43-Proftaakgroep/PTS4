package audio;

import java.applet.Applet;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maikel
 */
public class applut extends Applet {

    //Called when this applet is loaded into the browser.
    @Override
    public void init() {
        //Execute a job on the event-dispatching thread; creating this applet's GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JLabel lbl = new JLabel("Hello World");
                    add(lbl);
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }
}
