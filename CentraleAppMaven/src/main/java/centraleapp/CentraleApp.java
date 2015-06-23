/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.CentralServer;

/**
 *
 * @author Eric
 */
public class CentraleApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/IncidentValidatie.fxml"));
        Parent root2 = FXMLLoader.load(getClass().getResource("/fxml/CallFXML.fxml"));
        
        Scene scene = new Scene(root);
        Scene scene2 = new Scene(root2);
        CentralServer server = new CentralServer();
        server.init();
        stage.setScene(scene);
        stage.show();
        stage.setTitle("CIMS | Incidentenmanagement");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              server.stop();
              System.exit(1);
          }
      }); 
        Stage s =  new Stage();
        s.setTitle("CIMS | Inkomende verbindingen");
        s.setScene(scene2);
        s.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
