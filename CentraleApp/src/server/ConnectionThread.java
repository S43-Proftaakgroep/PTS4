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
import java.util.regex.Pattern;

/**
 *
 * @author Sasa2905
 */
public class ConnectionThread implements Runnable {

    Socket insocket;

    public ConnectionThread(Socket socket) {
        this.insocket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(insocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(insocket.getOutputStream());
            IncidentContainer container = IncidentContainer.getInstance();
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
            } else if(instring.startsWith("@2#")) {
                out.writeObject("nieuwe incidenten");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
