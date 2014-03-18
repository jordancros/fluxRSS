<%-- 
    Document   : bandeauConnexion
    Created on : 17 mars 2014, 22:47:52
    Author     : Jow
--%>
<%
boolean estAdmin;
    try {
        estAdmin = ((Boolean) session.getAttribute("estAdmin")).booleanValue();
    }
    catch(NullPointerException e) {
        estAdmin = false;
    }
    catch(Exception e) {
        estAdmin = false;
    }
%>

<header>                         
    <% 
        if(!estAdmin) {
    %>
        <div id="corpsConnexion">
            <form action="ControleurAdmin?action=connexion" method="POST">
                <div>
                    <label>Login:</label>
                    <input type="text" name="login" id="login" value="" size="50" />
                </div>
                <div>
                    <label>Mot de passe :</label>
                    <input type="password" name="motDePasse" id="motDePasse" value="" size="50" />
                </div>
                <div>
                    <input type="submit" value="Connexion" id="boutonConnexion"/>
                </div>
            </form>
        </div>
    <%
    }
    %>

    <% 
        if(estAdmin) {
    %>
        <a href="ControleurAdmin?action=deconnexion">D&eacute;connexion</a>
    <%
       }
    %>