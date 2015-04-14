/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import centraleapp.CallFXMLController;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author maikel
 */
public class VideoThread implements Runnable {

    Socket insocket;
    ObjectInputStream in;
    CallFXMLController controller;

    public VideoThread(Socket socket, CallFXMLController controller) {
        this.insocket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedImage bi = ImageIO.read(insocket.getInputStream());
                if (bi != null) {
                    Thread t = new Thread(() -> {
                        Image i = SwingFXUtils.toFXImage(bi, null);
                        controller.setImage(i);
                    });
                    t.start();

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
