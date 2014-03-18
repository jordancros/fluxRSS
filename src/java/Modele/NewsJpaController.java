/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modele;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Classes.Flux;
import Classes.News;
import Modele.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jeffrey
 */
public class NewsJpaController implements Serializable {

    public NewsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(News news) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Flux idFlux = news.getIdFlux();
            if (idFlux != null) {
                idFlux = em.getReference(idFlux.getClass(), idFlux.getIdFlux());
                news.setIdFlux(idFlux);
            }
            em.persist(news);
            if (idFlux != null) {
                idFlux.getNewsCollection().add(news);
                idFlux = em.merge(idFlux);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(News news) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            News persistentNews = em.find(News.class, news.getIdNews());
            Flux idFluxOld = persistentNews.getIdFlux();
            Flux idFluxNew = news.getIdFlux();
            if (idFluxNew != null) {
                idFluxNew = em.getReference(idFluxNew.getClass(), idFluxNew.getIdFlux());
                news.setIdFlux(idFluxNew);
            }
            news = em.merge(news);
            if (idFluxOld != null && !idFluxOld.equals(idFluxNew)) {
                idFluxOld.getNewsCollection().remove(news);
                idFluxOld = em.merge(idFluxOld);
            }
            if (idFluxNew != null && !idFluxNew.equals(idFluxOld)) {
                idFluxNew.getNewsCollection().add(news);
                idFluxNew = em.merge(idFluxNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = news.getIdNews();
                if (findNews(id) == null) {
                    throw new NonexistentEntityException("The news with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            News news;
            try {
                news = em.getReference(News.class, id);
                news.getIdNews();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The news with id " + id + " no longer exists.", enfe);
            }
            Flux idFlux = news.getIdFlux();
            if (idFlux != null) {
                idFlux.getNewsCollection().remove(news);
                idFlux = em.merge(idFlux);
            }
            em.remove(news);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<News> findNewsEntities() {
        return findNewsEntities(true, -1, -1);
    }

    public List<News> findNewsEntities(int maxResults, int firstResult) {
        return findNewsEntities(false, maxResults, firstResult);
    }

    private List<News> findNewsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(News.class));
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

    public News findNews(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(News.class, id);
        } finally {
            em.close();
        }
    }

    public int getNewsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<News> rt = cq.from(News.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
