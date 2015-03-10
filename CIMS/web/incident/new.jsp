<%-- 
    Document   : new
    Created on : 3-mrt-2015, 10:16:51
    Author     : Joris
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
            <h1>Meld incident</h1>
            <p>Hier kunnen ingelogde gebruikers incidenten melden.</p>
            <form ACTION="Submit.jsp" method="POST">
                <div class="incident">
                    <label> Type incident</label> <input type="text" name="name"><BR>
                    <label> Locatie</label> <input type="text" name="location"><BR>
                    <label> Beschrijving</label> <input type="text" name="description"><BR>
                </div>
                <br>
                <input type="submit" value="Submit">
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
