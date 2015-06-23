/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Sasa2905
 */
@ServerEndpoint("/liveaudio")
public class audioReceiver {

    Socket audioSocket;
    ObjectOutputStream outAudio;
    BufferedOutputStream buffer;
    CentralAudio audioReceiver;
    public audioReceiver() {
        try {
            audioReceiver = new CentralAudio();
            Thread t = new Thread(audioReceiver);
            t.start();
            audioSocket = new Socket(InetAddress.getByName("145.144.248.88"), 1110);
            //outAudio = new ObjectOutputStream(audioSocket.getOutputStream());
            buffer = new BufferedOutputStream(audioSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(audioReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void processVideo(byte[] audioData, Session session) {
        //System.out.println("Audio:" + audioData.length);
        try {
            buffer.write(audioData);
            audioSocket.getOutputStream().flush();
        } catch (IOException ex) {
            Logger.getLogger(audioReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
