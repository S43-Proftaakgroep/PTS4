/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.io.IOException;
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
            request.getParameter("name");
            request.getParameter("type");
            request.getParameter("address");
            request.getParameter("coordinates");
        } catch (Throwable theException) {
            System.out.println(theException);
        }
    }
}
