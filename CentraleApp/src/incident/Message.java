/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class Message {

    private String sender;
    private String messageText;
    private String date;
    private int incidentId;

    public Message(String sender, String messageText, int incidentId)
    {
        this.sender = sender;
        this.messageText = messageText;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.date = sdf.format(date);
        this.incidentId = incidentId;
    }
    
    public Message(String sender, String messageText, int incidentId, String date)
    {
        this.sender = sender;
        this.messageText = messageText;
        this.incidentId = incidentId;
        this.date = date;
    }
    
    public String getSender()
    {
        return this.sender;
    }
    
    public String getMessageText()
    {
        return this.messageText;
    }
    
    public String getDate()
    {
        return this.date;
    }
    
    public int getIncidentId()
    {
        return this.incidentId;
    }
    
    
}
