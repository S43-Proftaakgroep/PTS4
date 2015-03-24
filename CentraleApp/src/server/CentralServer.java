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
    private Thread connectionThread;
    
    public void initServer() {
        connectionThread = new Thread(new ConnectionSearcher());
        connectionThread.start();
    }
}
