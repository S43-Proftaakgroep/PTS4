/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Joris
 */
@ServerEndpoint("/livevideo")
public class videoReceiver {

    @OnMessage
    public void processVideo(byte[] imageData, Session session) {
        System.out.println("Message received.");
        try {
            Socket socket = new Socket(InetAddress.getByName("145.144.250.43"), 1100);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.write(imageData);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
