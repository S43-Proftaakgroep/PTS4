<%-- 
    Document   : enter
    Created on : Mar 10, 2015, 9:25:24 AM
    Author     : Sasa2905
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%String name = request.getParameter("name");
            String location = request.getParameter("location");
            String description = request.getParameter("description");
        %>
        </h1>
    </body>
</html>
