<!-- Navigation bar, to be included right after <body> on every page. -->
<%@ page pageEncoding="UTF-8" import="authentication.UserBean" %>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigatie</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/CIMS/index.jsp">CIMS</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">

            <ul class="nav navbar-nav">
                <%
                    UserBean currentUser = (UserBean) session.getAttribute("currentSessionUser");
                    if (currentUser != null && currentUser.isValid()) {
                %>
                <li <% if (request.getServletPath().equals("/incident/new.jsp")) {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/incident/new.jsp">Nieuw incident</a></li>
                    <% } %>
                <li <% if (request.getServletPath().equals("/incident/all.jsp")) {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/incident/all.jsp">Incidenten</a></li>
                <li <% if (request.getServletPath().equals("about.jsp")) {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/about.jsp">About</a></li>
                <li <% if (request.getServletPath().equals("contact.jsp")) {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/contact.jsp">Contact</a></li>
            </ul>
            <%
                if (currentUser != null && currentUser.isValid()) {
            %>
            <ul class="nav navbar-nav navbar-right">
                <li <% if (request.getServletPath().equals("/manage/index.jsp")) {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/manage/index.jsp" title="Beheer je account">Hallo, <%=currentUser.getUsername()%></a></li>
                <li><a href="/CIMS/LogoutServlet" title="Log uit">Log uit</a></li>
            </ul>
            <% } else { %>
            <form class="navbar-form navbar-right" action="/CIMS/LoginServlet" method="POST">
                <div class="form-group">
                    <input name="username" type="text" placeholder="Gebruikersnaam" class="form-control">
                </div>
                <div class="form-group">
                    <input name="password" type="password" placeholder="Wachtwoord" class="form-control">
                </div>
                <button type="submit" class="btn btn-success">Log in</button>
                <button type="button" class="btn btn-success" onclick="location.href = '/CIMS/manage/newUser.jsp'">Create User</button>
            </form>
            <% }%>
        </div><!--/.navbar-collapse -->
    </div>
</nav>
