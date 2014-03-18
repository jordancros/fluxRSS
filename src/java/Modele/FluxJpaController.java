/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modele;

import Classes.Flux;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Classes.News;
import Modele.exceptions.IllegalOrphanException;
import Modele.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jeffrey
 */
public class FluxJpaController implements Serializable {

    public FluxJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Flux flux) {
        if (flux.getNewsCollection() == null) {
            flux.setNewsCollection(new ArrayList<News>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<News> attachedNewsCollection = new ArrayList<News>();
            for (News newsCollectionNewsToAttach : flux.getNewsCollection()) {
                newsCollectionNewsToAttach = em.getReference(newsCollectionNewsToAttach.getClass(), newsCollectionNewsToAttach.getIdNews());
                attachedNewsCollection.add(newsCollectionNewsToAttach);
            }
            flux.setNewsCollection(attachedNewsCollection);
            em.persist(flux);
            for (News newsCollectionNews : flux.getNewsCollection()) {
                Flux oldIdFluxOfNewsCollectionNews = newsCollectionNews.getIdFlux();
                newsCollectionNews.setIdFlux(flux);
                newsCollectionNews = em.merge(newsCollectionNews);
                if (oldIdFluxOfNewsCollectionNews != null) {
                    oldIdFluxOfNewsCollectionNews.getNewsCollection().remove(newsCollectionNews);
                    oldIdFluxOfNewsCollectionNews = em.merge(oldIdFluxOfNewsCollectionNews);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Flux flux) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Flux persistentFlux = em.find(Flux.class, flux.getIdFlux());
            Collection<News> newsCollectionOld = persistentFlux.getNewsCollection();
            Collection<News> newsCollectionNew = flux.getNewsCollection();
            List<String> illegalOrphanMessages = null;
            for (News newsCollectionOldNews : newsCollectionOld) {
                if (!newsCollectionNew.contains(newsCollectionOldNews)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain News " + newsCollectionOldNews + " since its idFlux field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<News> attachedNewsCollectionNew = new ArrayList<News>();
            for (News newsCollectionNewNewsToAttach : newsCollectionNew) {
                newsCollectionNewNewsToAttach = em.getReference(newsCollectionNewNewsToAttach.getClass(), newsCollectionNewNewsToAttach.getIdNews());
                attachedNewsCollectionNew.add(newsCollectionNewNewsToAttach);
            }
            newsCollectionNew = attachedNewsCollectionNew;
            flux.setNewsCollection(newsCollectionNew);
            flux = em.merge(flux);
            for (News newsCollectionNewNews : newsCollectionNew) {
                if (!newsCollectionOld.contains(newsCollectionNewNews)) {
                    Flux oldIdFluxOfNewsCollectionNewNews = newsCollectionNewNews.getIdFlux();
                    newsCollectionNewNews.setIdFlux(flux);
                    newsCollectionNewNews = em.merge(newsCollectionNewNews);
                    if (oldIdFluxOfNewsCollectionNewNews != null && !oldIdFluxOfNewsCollectionNewNews.equals(flux)) {
                        oldIdFluxOfNewsCollectionNewNews.getNewsCollection().remove(newsCollectionNewNews);
                        oldIdFluxOfNewsCollectionNewNews = em.merge(oldIdFluxOfNewsCollectionNewNews);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = flux.getIdFlux();
                if (findFlux(id) == null) {
                    throw new NonexistentEntityException("The flux with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Flux flux;
            try {
                flux = em.getReference(Flux.class, id);
                flux.getIdFlux();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The flux with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<News> newsCollectionOrphanCheck = flux.getNewsCollection();
            for (News newsCollectionOrphanCheckNews : newsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Flux (" + flux + ") cannot be destroyed since the News " + newsCollectionOrphanCheckNews + " in its newsCollection field has a non-nullable idFlux field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(flux);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Flux> findFluxEntities() {
        return findFluxEntities(true, -1, -1);
    }

    public List<Flux> findFluxEntities(int maxResults, int firstResult) {
        return findFluxEntities(false, maxResults, firstResult);
    }

    private List<Flux> findFluxEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Flux.class));
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

    public Flux findFlux(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Flux.class, id);
        } finally {
            em.close();
        }
    }

    public int getFluxCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Flux> rt = cq.from(Flux.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
