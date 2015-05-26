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
 * @author Sasa2905
 */
public class CentralAudio implements Runnable {

    private Socket socket;
    private BufferedInputStream buffer;

    public CentralAudio() {
        try {
            socket = new Socket("145.144.252.55", 1102);
            buffer = new BufferedInputStream(socket.getInputStream());
            //receiveAudio();
        } catch (IOException ex) {
            Logger.getLogger(CentralAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private void receiveAudio() {
//
//        AudioInputStream inputStream;
//        try {
//            inputStream = AudioSystem.getAudioInputStream(buffer);
//            Clip clip = AudioSystem.getClip();
//            clip.open(inputStream);
//            clip.start();
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//            Logger.getLogger(CentralAudio.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    @Override
    public void run() {
        try {
            buffer = new BufferedInputStream(socket.getInputStream());
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(buffer);
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            System.out.println(clip.isOpen());
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            Logger.getLogger(CentralAudio.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
