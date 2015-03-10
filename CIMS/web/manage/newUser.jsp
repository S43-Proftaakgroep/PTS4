<%-- 
    Document   : newUser
    Created on : 3-mrt-2015, 20:04:06
    Author     : Frank
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <% 
            if (request.getParameter("error") != null)
                if (request.getParameter("error").equals("wrongInput"))
                {
                    out.write("Some of your input was invalid. Please try again.");
                }
                else if (request.getParameter("error").equals("existingUsername"))
                {
                    out.write("Your username already exists. Please choose another username.");
                }
                else if (request.getParameter("error").equals("existingEmail"))
                {
                    out.write("A user with this email-adress has already been registered");
                }
        %>
        <form class="registerform" action="/CIMS/CreateServlet" method="POST">
            <div class="registerFields">
                <input name="username" type="text" placeholder="Gebruikersnaam" class="form-input">
                <input name="password" type="text" placeholder="Wachtwoord" class="form-control">
                <input name="emailadress" type="text" placeholder="EmailAdres" class="form-control">
            </div>
            <div class="registerButton">
                <button type="submit" class="btn btn-success">Register</button>
            </div>
        </form>
    </body>
</html>
