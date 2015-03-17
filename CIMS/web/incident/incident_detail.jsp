<%-- 
    Document   : incident_detail
    Created on : 17-mrt-2015, 10:51:02
    Author     : Eric
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
            if (request.getParameter("create") != null)
                if (request.getParameter("create").equals("success"))
                    out.write("Successfully Created Account");
            %>
    </body>
</html>
