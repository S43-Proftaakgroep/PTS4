/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Sasa2905
 */
public class CentralServer {

    public void initServer() {
        try {
            ServerSocket socket = new ServerSocket(1099);

            Socket insocket = socket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(insocket.getInputStream()));
            PrintWriter out = new PrintWriter(insocket.getOutputStream(),
                    true);

            String instring = in.readLine();
            out.println("The server got this: " + instring);
            insocket.close();
        } catch (Exception e) {
        }
    }
}
