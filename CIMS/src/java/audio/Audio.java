package audio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maikel
 */
import java.applet.Applet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * SimpleSoundCapture Example. This is a simple program to record sounds and
 * play them back. It uses some methods from the CapturePlayback program in the
 * JavaSoundDemo. For licensizing reasons the disclaimer above is included.
 * 
 * @author Steve Potts
 */
public class Audio extends Applet {

  final int bufSize = 16384;

  public Capture capture = new Capture();

  public Playback playback = new Playback();

  AudioInputStream audioInputStream;

  String errStr;

  double duration, seconds;

  File file;

  public Audio() {
      System.out.println("bier");
  }


    
  

  /**
   * Write data to the OutputChannel.
   */
  public class Playback implements Runnable {

    SourceDataLine line;

    Thread thread;
    
    Playback()
    {
        
    }

    public void start() {
      System.out.println("playback");
      errStr = null;
      thread = new Thread(this);
      thread.setName("Playback");
      thread.start();
    }

    public void stop() {
      thread = null;
    }

    private void shutDown(String message) {
      if ((errStr = message) != null) {
        System.err.println(errStr);
      }
      if (thread != null) {
        thread = null;
      }
    }

    @Override
    public void run() {

      // make sure we have something to play
      if (audioInputStream == null) {
        shutDown("No loaded audio to play back");
        return;
      }
      // reset to the beginnning of the stream
      try {
        audioInputStream.reset();
      } catch (Exception e) {
        shutDown("Unable to reset the stream\n" + e);
        return;
      }

      // get an AudioInputStream of the desired format for playback

      AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
      float rate = 44100.0f;
      int channels = 2;
      int sampleSize = 16;
      boolean bigEndian = true;

      AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
          * channels, rate, bigEndian);

      AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format,
          audioInputStream);

      if (playbackInputStream == null) {
        shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
        return;
      }

      // define the required attributes for our line,
      // and make sure a compatible line is supported.

      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      if (!AudioSystem.isLineSupported(info)) {
        shutDown("Line matching " + info + " not supported.");
        return;
      }

      // get and open the source data line for playback.

      try {
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format, bufSize);
      } catch (LineUnavailableException ex) {
        shutDown("Unable to open the line: " + ex);
        return;
      }

      // play back the captured Audio data

      int frameSizeInBytes = format.getFrameSize();
      int bufferLengthInFrames = line.getBufferSize() / 8;
      int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
      byte[] data = new byte[bufferLengthInBytes];
      int numBytesRead = 0;

      // start the source data line
      line.start();

      while (thread != null) {
        try {
          if ((numBytesRead = playbackInputStream.read(data)) == -1) {
            break;
          }
          int numBytesRemaining = numBytesRead;
          while (numBytesRemaining > 0) {
            numBytesRemaining -= line.write(data, 0, numBytesRemaining);
          }
        } catch (Exception e) {
          shutDown("Error during playback: " + e);
          break;
        }
      }
      // we reached the end of the stream.
      // let the data play out, then
      // stop and close the line.
      if (thread != null) {
        line.drain();
      }
      line.stop();
      line.close();
      line = null;
      shutDown(null);
    }
  } // End class Playback

  /**
   * Reads data from the input channel and writes to the output stream
   */
  public class Capture implements Runnable  {

    TargetDataLine line;

    Thread thread;

    public void start() {
        System.out.println("capture");
      errStr = null;
      thread = new Thread(this);
      thread.setName("Capture");
      thread.start();
    }

    public void stop() {
      thread = null;
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

      // play back the captured Audio data
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int frameSizeInBytes = format.getFrameSize();
      int bufferLengthInFrames = line.getBufferSize() / 8;
      int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
      byte[] data = new byte[bufferLengthInBytes];
      int numBytesRead;

      line.start();

      while (thread != null) {
        if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
          break;
        }
        out.write(data, 0, numBytesRead);
      }

      // we reached the end of the stream.
      // stop and close the line.
      line.stop();
      line.close();
      line = null;

      // stop and close the output stream
      try {
        out.flush();
        out.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

      // load bytes into the Audio input stream for playback

      byte audioBytes[] = out.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
      audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

      long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format
          .getFrameRate());
      duration = milliseconds / 1000.0;

      try {
        audioInputStream.reset();
      } catch (Exception ex) {
        ex.printStackTrace();
        return;
      }

    }
  }
}

  