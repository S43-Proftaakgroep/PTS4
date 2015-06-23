/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javafx.application.Platform;

/**
 *
 * @author Eric
 */
public class MessageContainer extends Observable {
    
    private List<Message> messages;
    private static MessageContainer instance = null;
    
    protected MessageContainer() {
        messages = new ArrayList<>();
    }
    
    public static MessageContainer getInstance() {
        if(instance == null) {
            instance = new MessageContainer();
        }
        return instance;
    }
    
    public List<Message> getMessagesByIncidentId(int id)
    {
        List<Message> messagesByid = new ArrayList<>();
        for(Message message : messages)
        {
            if(message.getIncidentId() == id)
            {
                messagesByid.add(message);
            }
        }
        return null;
    }
    
    public void addMessage(String sender, String messageText, int incidentId) {
        Message message = new Message(sender, messageText, incidentId);
        this.messages.add(message);
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                setChanged();
                notifyObservers(message);
            }
        });
    }
}
