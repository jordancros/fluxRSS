/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controleur;

import Classes.Admin;
import Modele.AdminJpaController;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jow
 */
@WebServlet(name = "ControleurAdmin", urlPatterns = {"/ControleurAdmin"})
public class ControleurAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String action = request.getParameter("action");

            if(action.equals("connexion"))
            {
                connexion(request,response);
            }

            if(action.equals("deconnexion"))
            {
                deconnexion(request, response);
            }
        }
        catch(Exception ex)
        {
            request.setAttribute("messageErreur", "Erreur SQL : action");
            redirection("JSPErreur", request, response);
        }
    }
    
    public void connexion(HttpServletRequest request, HttpServletResponse response)
    {
        String login = request.getParameter("login");
        String motDePasse = request.getParameter("motDePasse");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetRSSJavaPU");
        AdminJpaController ma = new AdminJpaController(emf);
        Admin admin = new Admin(login, motDePasse);
        
        try 
        {
            if(ma.connexion(request, admin)) 
            {
                request.setAttribute("messageInfo", "Admin connecté");
            }
            else 
            {
                request.setAttribute("messageInfo", "Erreur connexion");
            }
            redirection("JSPIndex", request, response);
        } 
        catch (SQLException ex) 
        {
            //System.out.println("BONJOUR LE JAVA DE MERDE");
            request.setAttribute("messageErreur", "Erreur SQL : connexion");
            redirection("JSPErreur", request, response);
        }
        catch (ClassNotFoundException ex) 
        {
            request.setAttribute("messageErreur", "Erreur SQL : connexion");
            redirection("JSPErreur", request, response);
        }
        
        catch (Exception ex) 
        {
            request.setAttribute("messageErreur", "Erreur : connexion");
            redirection("JSPErreur", request, response);
        }
    }
    
    public void deconnexion(HttpServletRequest request, HttpServletResponse response)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetRSSJavaPU");
        AdminJpaController ma = new AdminJpaController(emf);
        try {
            ma.deconnexion(request);
            request.setAttribute("messageInfo", "Admin déconnecté");
            redirection("JSPIndex", request, response);
        } catch (SQLException ex) {
            request.setAttribute("messageErreur", "Erreur SQL : deconnexion");
            redirection("JSPErreur", request, response);
        } catch (Exception ex) {
            request.setAttribute("messageErreur", "Erreur : deconnexion");
            redirection("JSPErreur", request, response);
        }
    }
    
    private void redirection(String message, HttpServletRequest request, HttpServletResponse response) {
        if(message.equals("JSPErreur"))
        {
            try 
            {
                getServletContext().getRequestDispatcher("/erreur.jsp").forward(request, response);
            } 
            catch (ServletException ex) 
            {
                Logger.getLogger(ControleurAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ControleurAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(message.equals("JSPIndex"))
        {
            try 
            {
                getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            } 
            catch (ServletException ex) 
            {
                Logger.getLogger(ControleurAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ControleurAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
