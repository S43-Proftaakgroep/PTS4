/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authentication;

import cims.DatabaseManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "CreateServlet", urlPatterns = {"/CreateServlet"})
public class CreateServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if(request.getParameter("emailadress") == null || request.getParameter("username") == null || request.getParameter("password") == null || !request.getParameter("emailadress").contains("@"))
                response.sendRedirect("manage/newUser.jsp?error=wrongInput");
            
            if(DatabaseManager.checkEmail(request.getParameter("emailadress")))
            {
                response.sendRedirect("manage/newUser.jsp?error=existingEmail");
            }
            else if (DatabaseManager.checkUsername(request.getParameter("username")))
            {
                response.sendRedirect("manage/newUser.jsp?error=existingUsername");
            }
            else
            {
                DatabaseManager.addUser(request.getParameter("username"), request.getParameter("emailadress"), request.getParameter("password"));
                response.sendRedirect("index.jsp?create=success");
            }
        } catch (Throwable theException) {
            System.out.println(theException);
        }
    }
}
