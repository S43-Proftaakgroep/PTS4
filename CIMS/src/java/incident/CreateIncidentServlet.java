/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import authentication.UserBean;
import cims.DatabaseManager;
import java.io.*;
import java.net.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joris
 */
@WebServlet(name = "CreateIncidentServlet", urlPatterns = {"/CreateIncidentServlet"})
public class CreateIncidentServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String location = request.getParameter("address");
            String description = request.getParameter("descr");
            String longtitude = request.getParameter("longtitude");
            String latitude = request.getParameter("latitude");
            UserBean currentUser = (UserBean) request.getSession().getAttribute("currentSessionUser");
            String submitter = currentUser.getUsername();
            name = name.replace("|", "");
            location = location.replace("|", "");
            description = description.replace("|", "");
            String infoString = "@1#" + name + "|" + location + "|" + description + "|" + submitter + "|" + longtitude + "|" + latitude;
            try{
            double longtitudeDouble = Double.parseDouble(longtitude);
            double latitudeDouble = Double.parseDouble(latitude);
            DatabaseManager.addIncident(name, location, submitter, description, longtitudeDouble, latitudeDouble);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
            }
            try {
                Socket socket = new Socket("145.93.105.17", 1099);
                OutputStream outSocket = socket.getOutputStream();
                ObjectOutputStream outWriter = new ObjectOutputStream(outSocket);
                outWriter.writeObject(infoString);
                outWriter.close();
                socket.close();
                response.sendRedirect("/incident/new.jsp");
            } catch (ConnectException e) {
                e.printStackTrace();
            }
        } catch (Throwable theException) {
            System.out.println(theException);
        }
    }
}
