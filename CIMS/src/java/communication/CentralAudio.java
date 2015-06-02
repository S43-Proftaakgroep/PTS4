/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
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
            socket = new Socket("145.144.251.8", 1102);
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
            playSound();
        } catch (IOException ex) {
            Logger.getLogger(CentralAudio.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void playSound() {
        SourceDataLine line = null;
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        final int bufSize = 16384;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = true;

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
                * channels, rate, bigEndian);

        AudioInputStream playbackInputStream = null;
//            byte[] bufferBytes = null;
//            buffer.read(bufferBytes);
//            ByteArrayInputStream bais = new ByteArrayInputStream(bufferBytes);
        playbackInputStream = new AudioInputStream(buffer, format, bufSize);
        // define the required attributes for our line,
        // and make sure a compatible line is supported.
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        // get and open the source data line for playback.
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufSize);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

        // play back the captured audio data
        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead = 0;

        // start the source data line
        line.start();

        while (true) {
            try {
                if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                    break;
                }
                int numBytesRemaining = numBytesRead;
                while (numBytesRemaining > 0) {
                    numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // we reached the end of the stream.
        // let the data play out, then
        // stop and close the line.
    }
}
