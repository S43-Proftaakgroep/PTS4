/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import incident.IncidentContainer;

/**
 *
 * @author Sasa2905
 */
public class CentralServer {
<<<<<<< HEAD
    private Thread connectionThread;
    
    public void initServer() {
        connectionThread = new Thread(new ConnectionSearcher());
        connectionThread.start();
=======

    private Thread thread;
    private ConnectionSearcher connectionSearcher;
    
    public void init() {
        connectionSearcher = new ConnectionSearcher();
        thread = new Thread(connectionSearcher);
        thread.start();
>>>>>>> origin/master
    }
    
    public void stop(){
        connectionSearcher.stop();
    }
}
