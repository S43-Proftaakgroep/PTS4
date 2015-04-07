/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call;

import centraleapp.CallFXMLController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author maikel
 */
public class VideoReceiver implements Runnable {

    CallFXMLController controller;
    
    public VideoReceiver(CallFXMLController controller)
    {
        this.controller = controller;
    }
    
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(1100);
            while(true)
            {
                Socket socket = ss.accept();
                VideoThread videoThread = new VideoThread(socket, controller);
                Thread newThread = new Thread(videoThread);
                newThread.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
