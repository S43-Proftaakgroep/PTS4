/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Eric
 */
public class IncidentValidatieController implements Initializable {
    
    @FXML
    Label lblName;
    Label lblDate;
    Label lblLocation;
    Label lblStatus;
    Label lblDescription;
    
    @FXML
    private void btnApprove_Click(ActionEvent event)
    {
        
    }
    
    @FXML
    private void btnDeny_Click(ActionEvent event)
    {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
