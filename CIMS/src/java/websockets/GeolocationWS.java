/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets;

import java.io.IOException;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Joris
 */
@ServerEndpoint(value = "/geolocation", configurator = GetHttpSessionConfigurator.class)
public class GeolocationWS {

    private Session wsSession;
    private HttpSession httpSession;

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message received: " + message);
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        System.out.println("Longitude: " + jsonObject.get("long"));
        System.out.println("Latitude: " + jsonObject.get("lat"));
        if(httpSession.getAttribute("geolocation") == null){
            try {
                wsSession.getBasicRemote().sendText("refresh pls");
            } catch (IOException ex) {
                System.out.println("Error" + ex.getMessage());
            }
        }
        httpSession.setAttribute("geolocation",
                new Coordinates(
                        Double.parseDouble(jsonObject.get("long").toString()),
                        Double.parseDouble(jsonObject.get("lat").toString())));
    }

    @OnOpen
    public void onOpen(Session peer, EndpointConfig config) {
        wsSession = peer;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    }

    @OnClose
    public void onClose(Session peer) {
        wsSession = null;
    }
}
