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

    private Thread thread;
    private ConnectionSearcher connectionSearcher;
    
    public void init() {
        connectionSearcher = new ConnectionSearcher();
        thread = new Thread(connectionSearcher);
        thread.start();
    }
    
    public void stop(){
        connectionSearcher.stop();
    }
}
