/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maikel
 */
public class CaptureThread implements Runnable {

    Socket insocket;

    public CaptureThread() {    
    }

    @Override
    public void run() {
        while (true) {
            ServerSocket ss;
            try {
                ss = new ServerSocket(1102);

                while (true) {
                    Socket socket = ss.accept();
                    CaptureAudio c = new CaptureAudio(socket);
                    Thread newThread = new Thread(c);
                    newThread.start();
                }
            } catch (IOException ex) {
                Logger.getLogger(CaptureThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
