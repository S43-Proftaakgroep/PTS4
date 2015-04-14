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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Eric
 */
@WebServlet(name = "SendInfoToCentral", urlPatterns =
{
    "/SendInfoToCentral"
})
public class SendInfoToCentral extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        System.out.println("yay");
        try
        {
            UserBean currentUser = (UserBean) request.getSession().getAttribute("currentSessionUser");
            String incidentIdString = request.getParameter("incidentId");
            incidentIdString = incidentIdString.replace("|", "");
            int incidentId = Integer.parseInt(incidentIdString);
            if (currentUser != null)
            {
                String messageText = request.getParameter("messageText");
                String submitter = currentUser.getUsername();
                messageText = messageText.replace("|", "");
                submitter = submitter.replace("|", "");

                // Toevoegen aan database
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateString = sdf.format(date);

                DatabaseManager.addMessage(submitter, messageText, dateString, incidentId);

                // Versturen via sockets
                String infoString = "@2#" + submitter + "|" + messageText + "|" + incidentId;

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run()
                    {
                        try
                        {
                            int port = Integer.parseInt(Property.IPPORT.getProperty());
                            Socket socket = new Socket(Property.IPADRESS.getProperty(), port);
                            OutputStream outSocket = socket.getOutputStream();
                            ObjectOutputStream outWriter = new ObjectOutputStream(outSocket);
                            outWriter.writeObject(infoString);
                            outWriter.close();
                            socket.close();
                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }

                    }
                });
                t.start();
                response.sendRedirect("incident/incident_detail.jsp?incident=" + incidentIdString);
            }
            else
            {
                // TODO error handling
                response.sendRedirect("incident/incident_detail.jsp?incident=" + incidentIdString);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
