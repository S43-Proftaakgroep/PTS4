/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import authentication.UserBean;
import cims.DatabaseManager;
import cims.Property;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
        boolean success = false;
        try {
            String name = request.getParameter("name");
            String location = request.getParameter("address");
            String description = request.getParameter("descr");
            String longtitude = request.getParameter("longtitude");
            String latitude = request.getParameter("latitude");
            if(longtitude.equals("") || latitude.equals("")) {
                request.setAttribute("errorMessageLocation", "Please enter a location for the incident");
                request.getRequestDispatcher("incident/new.jsp").forward(request, response);
            }
            UserBean currentUser = (UserBean) request.getSession().getAttribute("currentSessionUser");
            String submitter = currentUser.getUsername();
            name = name.replace("|", "");
            location = location.replace("|", "");
            description = description.replace("|", "");
            String infoString = "@1#" + name + "|" + location + "|" + description + "|" + submitter + "|" + longtitude + "|" + latitude;
            try{
            double longtitudeDouble = Double.parseDouble(longtitude);
            double latitudeDouble = Double.parseDouble(latitude);
            success = DatabaseManager.addIncident(name, location, submitter, description, longtitudeDouble, latitudeDouble);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
            }
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                                try {
                int port = Integer.parseInt(Property.IPPORT.getProperty());
                Socket socket = new Socket(Property.IPADRESS.getProperty(), port);
                OutputStream outSocket = socket.getOutputStream();
                ObjectOutputStream outWriter = new ObjectOutputStream(outSocket);
                outWriter.writeObject(infoString);
                outWriter.close();
                socket.close();
                
            } catch (ConnectException e) {
                e.printStackTrace();
            }       catch (IOException ex) {
                        Logger.getLogger(CreateIncidentServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();
            //response.sendRedirect("incident/new.jsp");
            ServletContext sc = getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher("/incident/new.jsp");
            request.setAttribute("showBanner", success);
            rd.forward(request, response);
        } catch (Throwable theException) {
            System.out.println(theException);
        }
    }
}
