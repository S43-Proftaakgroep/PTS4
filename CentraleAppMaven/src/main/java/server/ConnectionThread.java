/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import incident.IncidentContainer;
import incident.Message;
import incident.MessageContainer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private Socket insocket;
    private int newIncidentCount = 0;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private IncidentContainer container;
    private MessageContainer messageContainer;

    public ConnectionThread(Socket socket)
    {
        this.insocket = socket;
        container = IncidentContainer.getInstance();
        container.addObserver(this);
        
        messageContainer = MessageContainer.getInstance();
        try
        {
            in = new ObjectInputStream(insocket.getInputStream());
        }
        catch (IOException ex)
        {
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        try
        {
            String instring = (String) in.readObject();
            if (instring.startsWith("@1#"))
            {
                instring = instring.replace("@1#", "");
                String[] incidentInfo = instring.split(Pattern.quote("|"));
                String typeIncident = incidentInfo[0];
                String location = incidentInfo[1];
                String description = incidentInfo[2];
                String submitter = incidentInfo[3];
                String longitude = incidentInfo[4];
                String latitude = incidentInfo[5];
                //int priority = Integer.parseInt(incidentInfo[6]);
                String victims = incidentInfo[6];
                String dangerGrade = incidentInfo[7];
                container.addIncident(location, longitude, latitude, submitter, typeIncident, description, "Today", 0,victims,dangerGrade);
                insocket.close();
            }
            else if (instring.startsWith("@2#"))
            {
                // Als er een bericht binnen komt van een eenheid via de website
                instring = instring.replace("@2#", "");
                String[] incidentInfo = instring.split(Pattern.quote("|"));
                String sender = incidentInfo[0];
                String messageText = incidentInfo[1];
                int incidentId = Integer.parseInt(incidentInfo[2]);
                System.out.println(sender + ", " + messageText + ", id:" + incidentId);
                messageContainer.addMessage(sender, messageText, incidentId);
                insocket.close();
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            out = new ObjectOutputStream(insocket.getOutputStream());
            out.writeObject("Niewe incidenten zijn toegevoegd, ververs de pagina voor meer informatie.");
        }
        catch (IOException ex)
        {
            
        }
    }

}
