<%-- 
    Document   : erreur
    Created on : 17 mars 2014, 21:25:42
    Author     : Jow
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Page d'erreur</title>
    </head>
    <body>
        <%
            String messageErreur = (String)request.getAttribute("messageErreur");    
        %>
        <section>
            <article>
                <h1>Erreur !</h1>
                <p><%= messageErreur %></p>
            </article>
        </section>
    </body>
</html>
