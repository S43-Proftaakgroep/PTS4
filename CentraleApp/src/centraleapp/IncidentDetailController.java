/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import api.*;
import database.DatabaseManager;
import incident.Incident;
import incident.Message;
import incident.MessageContainer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Joris
 */
public class IncidentDetailController implements Observer, Initializable {

    @FXML
    Label lblSocial;

    //Tab Incident
    @FXML
    Label lblIncidentName;
    @FXML
    Label lblIncidentDescription;
    @FXML
    Label lblIncidentWeather;
    @FXML
    Label lblIncidentSocial;
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
    @FXML
    CheckBox cbWeather;
    @FXML
    CheckBox cbSocialMedia;
    private WebEngine webEngine;

    //Tab Advices
    @FXML
    Button btnAddAdvice;
    @FXML
    Button btnEditAdvice;
    @FXML
    Button btnRemoveAdvice;
    @FXML
    ListView lvAdvicepage;
    @FXML
    TextArea taAdvicetext;

    //Tab Sent Information
    @FXML
    TableView tableIncidentInfo;

    //Tab Sent Images
    @FXML
    TilePane tilePane;
    //Tab Assistance Units
    @FXML
    Label lblDescription;
    @FXML
    Label lblName;
    @FXML
    Label lblVictims;
    @FXML
    Label lblDanger;
    @FXML
    Label lblSuccess;
    
    private Incident incident;
    private ObservableList<String> advices;
    private String mapsHTML;
    private MessageContainer messageContainer;
    ObservableList<Message> messages;

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
        messageContainer = MessageContainer.getInstance();
        messageContainer.addObserver(this);
        cbWeather.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lblIncidentWeather.setVisible(cbWeather.isSelected());
                //Set weather to irrelevant in webpages too
            }
        });
        cbSocialMedia.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                lblIncidentSocial.setVisible(cbSocialMedia.isSelected());
                //Set social media to irrelevant in webpages too
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------
    //      Tasks and init.
    //--------------------------------------------------------------------------------------------------------------
    public void init(Incident incident) {
        this.incident = incident;

        //TAB 1 - Incident
        lblIncidentName.setText(incident.getType() + " (" + incident.getPriority() + ")");
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

        Task<ArrayList<String>> socialMediaTask = new Task<ArrayList<String>>() {

            @Override
            protected ArrayList<String> call() throws Exception {
                TwitterFeed twitterFeed = new TwitterFeed();
                String[] strings = {"CIMS", "incident", incident.getType().replace(" ", "")};
                ArrayList<String> results = twitterFeed.getByTags(strings); // generic query because tweets don't exist.
                if (results.isEmpty())results.add("Er zijn geen tweets gevonden voor #" + incident.getType() + ", #CIMS, #incident.");
                super.succeeded();
                return results;
            }
        };

        //--------------------------------------------------------------------------------------------------------------
        //      Task handlers.
        //--------------------------------------------------------------------------------------------------------------
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

        socialMediaTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            vbox.getChildren().remove(piSocialMedia);
                            WebView wv = new WebView();
                            ArrayList<String> results = socialMediaTask.get();

                            String openingtag = "<html>";
                            String closingtag = "</html>";

                            StringBuffer sb = new StringBuffer(); // muh performance
                            sb.append(openingtag);

                            for (String result : results) {
                                sb.append("<p style=\"font-family: 'Lucida Bright', 'Lucida Bright'\">" + result + "</p>");
                            }

                            sb.append(closingtag);

                            wv.getEngine().loadContent(sb.toString());
                            lblIncidentSocial.setGraphic(wv);
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
        Thread socialThread = new Thread(socialMediaTask);
        socialThread.start();
        webEngine.loadContent(GoogleMaps.getURL(incident.getLatitude(), incident.getLongitude()));
        lblIncidentWeather.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        lblIncidentSocial.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        //TAB 2 - Advice
        lvAdvicepage.setItems(advices);

        //TODO
        // TAB 4 - Sent Information
        messages = FXCollections.observableArrayList(DatabaseManager.getMessagesWithId(incident.getId()));

        tableIncidentInfo.setEditable(false);

        TableColumn<Message, String> nameCol = new TableColumn<Message, String>("Naam");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new Callback<CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Message, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleStringProperty(p.getValue().getSender());
            }
        });

        TableColumn messageCol = new TableColumn("Bericht");
        messageCol.setMinWidth(500);
        messageCol.setCellValueFactory(new Callback<CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Message, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleStringProperty(p.getValue().getMessageText());
            }
        });

        TableColumn dateCol = new TableColumn("Datum");
        dateCol.setMinWidth(200);
        dateCol.setCellValueFactory(new Callback<CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Message, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleStringProperty(p.getValue().getDate());
            }
        });
        
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        tableIncidentInfo.getColumns().addAll(nameCol, messageCol, dateCol);
        tableIncidentInfo.setItems(messages);
        tableIncidentInfo.getSortOrder().add(dateCol);

        //Tab 5 - Sent Images
        tilePane.setPadding(new Insets(10, 10, 10, 10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);

        List<String> imagePaths = DatabaseManager.getImagePaths(incident.getId());
        if (imagePaths != null && imagePaths.size() > 0) {
            //Voor te testen
            //for(int i = 1; i < 25; i++)

            int height = 150;
            String basePath = "https://a-chan.nl/cims/";
            for (String path : imagePaths) {
                //Voor te testen
                //String path = "https://a-chan.nl/cims/images/IMG_0052.jpg";
                //Image image = new Image(path);

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                Image image = new Image(basePath + path);
                                ImageView imageview = new ImageView(image);
                                //imageview.preserveRatioProperty();
                                double scale = image.getHeight() / height;
                                imageview.setFitHeight(image.getHeight() / scale);
                                imageview.setFitWidth(image.getWidth() / scale);
                                tilePane.getChildren().addAll(imageview);

                                imageview.setOnMouseClicked(new EventHandler<MouseEvent>() {

                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                            if (mouseEvent.getClickCount() == 1) {
                                                BorderPane borderPane = new BorderPane();
                                                ScrollPane scrollpane = new ScrollPane();
                                                ImageView imageView = new ImageView();
                                                imageView.setImage(image);
                                                imageView.setStyle("-fx-background-color: WHITE");
                                                imageView.setFitHeight(image.getHeight());
                                                imageView.setPreserveRatio(true);
                                                imageView.setSmooth(true);
                                                imageView.setCache(true);
                                                scrollpane.setPannable(true);
                                                scrollpane.setContent(imageView);
                                                scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                                                scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                                                borderPane.setCenter(scrollpane);
                                                borderPane.setStyle("-fx-background-color: WHITE");
                                                Stage newStage = new Stage();
                                                newStage.setTitle("Image Detail");
                                                newStage.setWidth(image.getWidth() + 3);
                                                newStage.setHeight(image.getHeight() + 24);
                                                Scene scene = new Scene(borderPane, Color.WHITE);
                                                newStage.setScene(scene);
                                                newStage.show();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                t.start();
            }
        }
        //Tab 6: Assistance units
        lblName.setText(incident.getType());
        lblDescription.setText(incident.getDescription());
        lblVictims.setText(incident.getVictims());
        lblDanger.setText(incident.getDanger());
    }
    
    @FXML
    public void btnSendRequest_Click(ActionEvent event) {
        lblSuccess.setText("De desbetreffende eenheden worden geinformeerd.");
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

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (arg != null) {
                    Message message = (Message) arg;
                    if (incident.getId() == message.getIncidentId()) {
                        //synchronised?
                        messages.add(message);
                        tableIncidentInfo.setItems(messages);
                    }
                }
            }
        });
    }
}
