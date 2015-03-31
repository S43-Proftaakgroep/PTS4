/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.nio.ByteBuffer;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Joris
 */
@ServerEndpoint("/livevideo")
public class videoReceiver {

    @OnMessage
    public void processVideo(byte[] imageData, Session session) {
        System.out.println("INsite process Video");
        try {
            // Wrap a byte array into a buffer
            ByteBuffer buf = ByteBuffer.wrap(imageData);
            
        } catch (Throwable ioe) {
            System.out.println("Error sending message " + ioe.getMessage());
        }
    }
}
