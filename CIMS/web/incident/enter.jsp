<%-- 
    Document   : enter
    Created on : Mar 10, 2015, 9:25:24 AM
    Author     : Sasa2905
--%>

<%@page import="java.net.ConnectException"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.Socket"%>
<%@page import="authentication.UserBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%
            String name = request.getParameter("name");
            String location = request.getParameter("location");
            String description = request.getParameter("description");
            UserBean currentUser = (UserBean) session.getAttribute("currentSessionUser");
            String submitter = currentUser.getUsername();
            try {
                String infoString = name + " " + location + " " + description + " " + submitter;
                Socket socket = new Socket("127.0.0.1", 1099);
                InputStream inSocket = socket.getInputStream();
                OutputStream outSocket = socket.getOutputStream();
                outSocket.write(infoString.getBytes());
                socket.close();
            } catch (ConnectException e) {
            %>
            <%
                }
            %>
            %>
        </h1>
    </body>
</html>
