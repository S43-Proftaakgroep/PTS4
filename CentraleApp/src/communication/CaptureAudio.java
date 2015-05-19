/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author maikel
 */
public class CaptureAudio implements Runnable {

    TargetDataLine line;
    Thread thread;
    String errStr;
    double duration;
    AudioInputStream audioInputStream;
    int count = 0;
    Socket socket;
    BufferedOutputStream buffer;

    public CaptureAudio(Socket socket) {
        this.socket = socket;
        try {
            buffer = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(CaptureAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void shutDown(String message) {
        if ((errStr = message) != null && thread != null) {
            thread = null;
            System.err.println(errStr);
        }
    }

    @Override
    public void run() {

        duration = 0;
        audioInputStream = null;

        // define the required attributes for our line,
        // and make sure a compatible line is supported.
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = true;

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
                * channels, rate, bigEndian);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            shutDown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the target data line for capture.
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
        } catch (LineUnavailableException ex) {
            shutDown("Unable to open the line: " + ex);
            return;
        } catch (SecurityException ex) {
            shutDown(ex.toString());
            //JavaSound.showInfoDialog();
            return;
        } catch (Exception ex) {
            shutDown(ex.toString());
            return;
        }

        // play back the captured audio data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        line.start();
        while (true) {
            numBytesRead = line.read(data, 0, bufferLengthInBytes);
            try {
                buffer.write(data);
                socket.getOutputStream().flush();
                System.out.println("test: " + numBytesRead + " + " + data.length);
            } catch (IOException ex) {
                Logger.getLogger(CaptureAudio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
} // End class Capture

