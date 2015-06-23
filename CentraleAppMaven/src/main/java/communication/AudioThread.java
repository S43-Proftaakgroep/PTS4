/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author maikel
 */
public class AudioThread implements Runnable {

    Socket insocket;

    public AudioThread(Socket socket) {
        this.insocket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedInputStream in = new BufferedInputStream(insocket.getInputStream());
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
                Clip clip = AudioSystem.getClip();
                clip.open(inputStream);
                clip.start();
            } catch (IOException ex) {
                Logger.getLogger(VideoThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
                Logger.getLogger(AudioThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
