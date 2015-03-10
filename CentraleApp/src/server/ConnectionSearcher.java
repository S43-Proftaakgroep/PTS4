/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sasa2905
 */
public class ConnectionSearcher implements Runnable {

    @Override
    public void run() {
        boolean listening = true;
        try {
            ServerSocket socket = new ServerSocket(1099);
            while(listening)
            {
                Socket insocket = socket.accept();
                ConnectionThread connectionThread = new ConnectionThread(insocket);
                Thread newThread = new Thread(connectionThread);
                newThread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionSearcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
