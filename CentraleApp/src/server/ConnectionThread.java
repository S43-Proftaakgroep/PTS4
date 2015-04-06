/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import incident.IncidentContainer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Sasa2905
 */
public class ConnectionThread implements Runnable, Observer {

    Socket insocket;
    int newIncidentCount = 0;
    ObjectInputStream in;
    ObjectOutputStream out;
    IncidentContainer container;

    public ConnectionThread(Socket socket) {
        this.insocket = socket;
        container = IncidentContainer.getInstance();
        container.addObserver(this);
        try {
            in = new ObjectInputStream(insocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String instring = (String) in.readObject();
            if (instring.startsWith("@1#")) {
                instring = instring.replace("@1#", "");
                String[] incidentInfo = instring.split(Pattern.quote("|"));
                String typeIncident = incidentInfo[0];
                String location = incidentInfo[1];
                String description = incidentInfo[2];
                String submitter = incidentInfo[3];
                String longitude = incidentInfo[4];
                String latitude = incidentInfo[5];
                container.addIncident(location, longitude, latitude, submitter, typeIncident, description, "Today");
                insocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            out = new ObjectOutputStream(insocket.getOutputStream());
            out.writeObject("nieuw");
        } catch (IOException ex) {
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
