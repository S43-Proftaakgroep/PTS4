<!-- Navigation bar, to be included right after <body> on every page. -->
<%@ page pageEncoding="UTF-8" %>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigatie</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">CIMS</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <form class="navbar-form navbar-right" method="POST" action="<%=request.getRequestURL() %>">
                <div class="form-group">
                    <input type="text" placeholder="Gebruikersnaam" class="form-control">
                </div>
                <div class="form-group">
                    <input type="password" placeholder="Wachtwoord" class="form-control">
                </div>
                <button type="submit" class="btn btn-success">Log in</button>
            </form>
        </div><!--/.navbar-collapse -->
    </div>
</nav>
