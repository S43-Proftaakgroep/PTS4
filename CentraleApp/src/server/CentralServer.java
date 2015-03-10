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

    public void initServer() {
        IncidentContainer container = IncidentContainer.getInstance();
        Thread connectionThread = new Thread(new ConnectionThread(container));
        connectionThread.start();
    }
}
