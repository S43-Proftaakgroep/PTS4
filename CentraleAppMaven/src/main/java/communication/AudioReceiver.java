/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author maikel
 */
public class AudioReceiver implements Runnable {
        public AudioReceiver(){
    }
    
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(1101);
            while(true)
            {
                Socket socket = ss.accept();
                AudioThread audioThread = new AudioThread(socket);
                Thread newThread = new Thread(audioThread);
                newThread.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
