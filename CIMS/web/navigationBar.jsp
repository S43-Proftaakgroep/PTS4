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
            <a class="navbar-brand" href="index.jsp">CIMS</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <%
                UserBean currentUser = (UserBean) session.getAttribute("currentSessionUser");
                if (currentUser != null && currentUser.isValid()) {
            %>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/CIMS/manage/index.jsp" title="Beheer je account">Hallo, <%=currentUser.getUsername()%></a></li>
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
            </form>
            <% }%>
        </div><!--/.navbar-collapse -->
    </div>
</nav>
