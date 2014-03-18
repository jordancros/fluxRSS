<%-- 
    Document   : index
    Created on : 17 mars 2014, 21:11:16
    Author     : Jow
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%  
        String messageInfo = (String)request.getAttribute("messageInfo");
    %>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Page principale &bull; Site de news</title>
    </head>
    <body>
        <%
            if(messageInfo != null)
            {
        %>
            <article>
                <h1>Information</h1>
                <p><%= messageInfo %></p>
            </article>
        <%
            }
        %>
        <h1>Bienvennue sur le site de News</h1>
        
        <%@include file="bandeauConnexion.jsp" %> 
        <%@include file="corpsEcran.jsp" %>
    </body>
</html>
