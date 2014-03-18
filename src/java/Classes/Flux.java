/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jeffrey
 */
@Entity
@Table(name = "flux")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Flux.findAll", query = "SELECT f FROM Flux f"),
    @NamedQuery(name = "Flux.findByIdFlux", query = "SELECT f FROM Flux f WHERE f.idFlux = :idFlux"),
    @NamedQuery(name = "Flux.findByNomFlux", query = "SELECT f FROM Flux f WHERE f.nomFlux = :nomFlux")})
public class Flux implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFlux")
    private Integer idFlux;
    @Basic(optional = false)
    @Column(name = "nomFlux")
    private String nomFlux;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFlux")
    private Collection<News> newsCollection;

    public Flux() {
    }

    public Flux(Integer idFlux) {
        this.idFlux = idFlux;
    }

    public Flux(Integer idFlux, String nomFlux) {
        this.idFlux = idFlux;
        this.nomFlux = nomFlux;
    }

    public Integer getIdFlux() {
        return idFlux;
    }

    public void setIdFlux(Integer idFlux) {
        this.idFlux = idFlux;
    }

    public String getNomFlux() {
        return nomFlux;
    }

    public void setNomFlux(String nomFlux) {
        this.nomFlux = nomFlux;
    }

    @XmlTransient
    public Collection<News> getNewsCollection() {
        return newsCollection;
    }

    public void setNewsCollection(Collection<News> newsCollection) {
        this.newsCollection = newsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFlux != null ? idFlux.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flux)) {
            return false;
        }
        Flux other = (Flux) object;
        if ((this.idFlux == null && other.idFlux != null) || (this.idFlux != null && !this.idFlux.equals(other.idFlux))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.Flux[ idFlux=" + idFlux + " ]";
    }
    
}
