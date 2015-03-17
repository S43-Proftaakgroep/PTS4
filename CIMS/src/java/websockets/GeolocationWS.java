/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Joris
 */
@ServerEndpoint("/geolocation")
public class GeolocationWS {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnMessage
    public String onMessage(String message) {
        System.out.println("Message received: " + message);
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        System.out.println("Longitude: " + jsonObject.get("long"));
        System.out.println("Latitude: " + jsonObject.get("lat"));
        return null;
    }
    
    @OnOpen
    public void onOpen (Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose (Session peer) {
        peers.remove(peer);
    }
}
