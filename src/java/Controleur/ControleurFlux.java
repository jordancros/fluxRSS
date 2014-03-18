/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controleur;

import Classes.Flux;
import Modele.FluxJpaController;
import java.io.IOException;
import java.util.List;
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
@WebServlet(name = "ControleurFlux", urlPatterns = {"/ControleurFlux"})
public class ControleurFlux extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        try
        {
            if (action.equals("afficherFlux"))
            {
                afficherFlux(request, response);
            }
        }
        catch (Exception e)
        {
            request.setAttribute("messageErreur", "Erreur SQL : action");
            redirection("JSPErreur", request, response);
        }
    }
    
    public void afficherFlux(HttpServletRequest request, HttpServletResponse response)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetRSSJavaPU");
        FluxJpaController modeleFlux = new FluxJpaController(emf);
        try {
            List<Flux> listeFlux = modeleFlux.findFluxEntities();
            request.setAttribute("listeFlux", listeFlux);
            redirection("JSPIndex", request, response);
        } 
        catch (Exception ex) {
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
