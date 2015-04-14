/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import communication.VideoReceiver;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author maikel
 */
public class CallFXMLController implements Initializable {

     @FXML ImageView ImageViewVideo;
    /**
     * Initializes the controller class.
     */
    private Thread thread;
    private VideoReceiver videoReciever;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        videoReciever = new VideoReceiver(this);
        thread = new Thread(videoReciever);
        thread.start();
    }    
    
    public void setImage(Image image)
    {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ImageViewVideo.setImage(image);
            }
        });
    }

    
}
