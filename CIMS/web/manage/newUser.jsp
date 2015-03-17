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
        <title>Crisis Informatie Management Systeem</title>
        <link href="/CIMS/css/bootstrap.min.css" rel="stylesheet">
        <link href="/CIMS/css/Site.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="/navigationBar.jsp" %>
        <div class="container">
        <h1>Nieuwe gebruiker</h1>
        <%
            if (request.getParameter("error") != null) {
                if (request.getParameter("error").equals("wrongInput")) {
                    out.write("Some of your input was invalid. Please try again.");
                } else if (request.getParameter("error").equals("existingUsername")) {
                    out.write("Your username already exists. Please choose another username.");
                } else if (request.getParameter("error").equals("existingEmail")) {
                    out.write("A user with this email-adress has already been registered");
                }
            }
        %>
        <form class="registerform" style="width:45%" action="/CIMS/CreateServlet" method="POST">
            <div class="form-group">
                <input name="username" type="text" placeholder="Gebruikersnaam" class="form-control">
                <input name="password" type="password" placeholder="Wachtwoord" class="form-control">
                <input name="emailadress" type="text" placeholder="EmailAdres" class="form-control">
            </div>
            <div class="registerButton">
                <button type="submit" class="btn btn-success">Register</button>
            </div>
        </form>
        <br>
        <footer>
            <p>&copy; <b>CIMS</b> 2015</p>
        </footer>
    </div> <!-- /container -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>
