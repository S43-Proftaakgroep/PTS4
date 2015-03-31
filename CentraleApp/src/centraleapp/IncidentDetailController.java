/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import api.WeatherFeed;
import database.DatabaseManager;
import incident.Incident;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.*;

/**
 * FXML Controller class
 *
 * @author Joris
 */
public class IncidentDetailController implements Initializable {

    @FXML
    Label lblIncidentName;

    @FXML
    Label lblIncidentDescription;

    @FXML
    Label lblIncidentWeather;

    @FXML
    ProgressIndicator piAdvice;

    @FXML
    ProgressIndicator piWeather;

    @FXML
    ProgressIndicator piSocialMedia;

    @FXML
    VBox vbox;

    @FXML
    GridPane grid;

    @FXML
    ListView<String> lvAdvices;

    @FXML
    ListView<String> lvSocialMedia;

    @FXML
    WebView webView;
    private WebEngine webEngine;

    private Incident incident;
    private ObservableList<String> advices;
    private String mapsHTML;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        advices = FXCollections.observableArrayList();
    }

    public void init(Incident incident) {
        this.incident = incident;
        
        //TAB 1 - Incident
        lblIncidentName.setText(incident.getType());
        lblIncidentDescription.setText(incident.getDescription());
        lvAdvices.setItems(advices);
        Task<List<String>> adviceTask = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {
                List<String> data = DatabaseManager.getAdviceById(incident.getId());
                if (data.size() < 1) {
                    data.add("<Geen adviezen>");
                }
                super.succeeded();
                return data;
            }
        };

        Task<String> weatherTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                String result = getWeatherInfo(incident.getLocation());
                super.succeeded();
                return result;
            }
        };

        Task<List<String>> socialMediaTask = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        adviceTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            grid.getChildren().remove(piAdvice);
                            advices.addAll(adviceTask.get());
                        } catch (InterruptedException | ExecutionException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });
            }
        });

        weatherTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            vbox.getChildren().remove(piWeather);
                            WebView wv = new WebView();
                            wv.getEngine().loadContent(weatherTask.get());
                            lblIncidentWeather.setGraphic(wv);
                        } catch (InterruptedException | ExecutionException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });
            }
        });

        Thread adviceThread = new Thread(adviceTask);
        adviceThread.start();
        Thread weatherThread = new Thread(weatherTask);
        weatherThread.start();
        webEngine.loadContent(GoogleMaps.getURL(incident.getLatitude(), incident.getLongitude()));
        lblIncidentWeather.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        
        //TAB 2 - Advice
        //TODO
    }

    private String getWeatherInfo(String location) {
        WeatherFeed wf = new WeatherFeed(location, WeatherFeed.Query.TEMPERATURE);
        String temp = wf.getData();
        wf.setQuery(WeatherFeed.Query.DESCRIPTION);
        String desc = wf.getData();
        wf.setQuery(WeatherFeed.Query.HUMIDITY);
        String humi = wf.getData();
        wf.setQuery(WeatherFeed.Query.PRESSURE);
        String pres = wf.getData();
        wf.setQuery(WeatherFeed.Query.WINDSPEED);
        String speed = wf.getData();
        wf.setQuery(WeatherFeed.Query.WINDDIRECTION);
        String deg = wf.getData();
        return "<html><p style=\"font-family: 'Lucida Bright', 'Lucida Bright'\">" + temp + " " + desc + " <b>Luchtvochtigheid:</b> " + humi + " <b>Luchtdruk:</b> " + pres + " <b>Wind:</b> " + speed + "@" + deg + "</p></html>";
    }
}
