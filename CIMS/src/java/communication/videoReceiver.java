/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
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
//            System.out.println("Creating image!");
//            System.out.println(System.getProperty("user.home"));
//            File f = new File(System.getProperty("user.home") + "\\Documents");
//            System.out.println("Can write: " + f.canWrite());
//            FileOutputStream output = new FileOutputStream(System.getProperty("user.home") + "\\Documents");
//            output.write(imageData);
            System.out.println("Sending data!");
            Socket socket = new Socket(InetAddress.getByName("145.144.250.43"), 1100);
            //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //out.write(imageData);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            ImageIO.write(image, "png", socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
