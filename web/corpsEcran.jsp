<%-- 
    Document   : corpsEcran
    Created on : 17 mars 2014, 23:11:07
    Author     : Jow
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="Classes.Flux"%>
<%
    ArrayList<Flux> listeFlux = (ArrayList<Flux>)request.getAttribute("listeFlux");
%>
<div id="corpsEcran">
    <a href="/ControleurControleurFlux?action=afficherFlux">Afficher les sites r�f�renc�s</a>
</div>

<%
    if(listeFlux != null)
    {
        for(Flux flux : listeFlux)
        {
%>
    <site>
        <% flux.getNomFlux(); %>
    </site>
    <br/>
<%
        }
    }
    else
    {
%>
        <site>
        <p><br /> Aucun site r�f�renc�. </p>
        </site>
<%
    }
%>
