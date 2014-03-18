/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modele;

import Classes.Admin;
import Modele.exceptions.NonexistentEntityException;
import Modele.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jeffrey
 */
public class AdminJpaController implements Serializable {

    public AdminJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    private HttpSession session = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Admin admin) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(admin);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdmin(admin.getLogin()) != null) {
                throw new PreexistingEntityException("Admin " + admin + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Admin admin) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            admin = em.merge(admin);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = admin.getLogin();
                if (findAdmin(id) == null) {
                    throw new NonexistentEntityException("The admin with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Admin admin;
            try {
                admin = em.getReference(Admin.class, id);
                admin.getLogin();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The admin with id " + id + " no longer exists.", enfe);
            }
            em.remove(admin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Admin> findAdminEntities() {
        return findAdminEntities(true, -1, -1);
    }

    public List<Admin> findAdminEntities(int maxResults, int firstResult) {
        return findAdminEntities(false, maxResults, firstResult);
    }

    private List<Admin> findAdminEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Admin.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Admin findAdmin(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Admin.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdminCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Admin> rt = cq.from(Admin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public boolean connexion(HttpServletRequest request, Admin admin) throws SQLException, Exception
    {
        
        if(valider(admin))
        {
            if(isAdmin(admin))
            {
                creerSession(request, admin);
                return true;
            }
        }
        return false;
    }
    
    private boolean valider(Admin admin)
    {
        if(admin.getLogin() == null || admin.getMdp()== null) 
        {
            return false;
        }
        
        if(admin.getLogin().equals("") || admin.getMdp().equals("")) 
        {
            return false;
        }
        
        /* Test des champs champs saisis par l'utilisateur */
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+");
        Matcher mLogin = p.matcher(admin.getLogin());
        Matcher mMotDePasse = p.matcher(admin.getMdp());
        boolean bLogin = mLogin.matches();
        boolean bMotDePasse = mMotDePasse.matches();
        
        return bLogin && bMotDePasse;
    }
            
    private void creerSession(HttpServletRequest request, Admin admin)
    {
        session = request.getSession(true);
        session.setAttribute("estAdmin", true);
    }
    
    public void deconnexion(HttpServletRequest request) throws SQLException, Exception
    {
        session = request.getSession();
        session.invalidate();
    }
    
    public boolean isAdmin(Admin admin) throws SQLException, Exception
    {
        Persistance bdd = Persistance.getInstance();
        
        String requete = "SELECT * FROM admin WHERE login = '" + admin.getLogin()
                + "' AND mdp = '" + admin.getMdp()+ "'";
        
        ResultSet resultat = bdd.lecture(requete);
        return resultat.next();
    }
    
}
