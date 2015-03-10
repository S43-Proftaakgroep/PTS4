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
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            UserBean user = new UserBean();
            user.setUserName(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));

            user = DatabaseManager.authenticateUser(user);

            if (user.isValid()) {

                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", user);
                response.sendRedirect("index.jsp"); //logged-in page      		
            } else {
                response.sendRedirect("index.jsp"); //error page 
            }
        } catch (Throwable theException) {
            System.out.println(theException);
        }
    }
}