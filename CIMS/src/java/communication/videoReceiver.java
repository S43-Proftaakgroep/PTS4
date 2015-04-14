/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Joris
 */
@ServerEndpoint("/livevideo")
public class videoReceiver {

    Socket socket;
    ObjectOutputStream out;
    public videoReceiver() {
        try {
            socket = new Socket(InetAddress.getByName("localhost"), 1100);
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @OnMessage
    public void processVideo(byte[] imageData, Session session) {
        try {
            System.out.println("Sending data!" + System.currentTimeMillis() + "Bytes: " + imageData.length);
            out.writeObject(imageData);
            socket.getOutputStream().flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
