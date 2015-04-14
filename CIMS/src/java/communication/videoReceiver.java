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

    public videoReceiver() {
        try {
            socket = new Socket(InetAddress.getByName("145.144.251.35"), 1100);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @OnMessage
    public void processVideo(byte[] imageData, Session session) {
        try {
            System.out.println("Sending data!" + System.currentTimeMillis());
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            ImageIO.write(image, "jpeg", socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
