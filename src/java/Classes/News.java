/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jeffrey
 */
@Entity
@Table(name = "news")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "News.findAll", query = "SELECT n FROM News n"),
    @NamedQuery(name = "News.findByIdNews", query = "SELECT n FROM News n WHERE n.idNews = :idNews"),
    @NamedQuery(name = "News.findByTitle", query = "SELECT n FROM News n WHERE n.title = :title"),
    @NamedQuery(name = "News.findByDescription", query = "SELECT n FROM News n WHERE n.description = :description"),
    @NamedQuery(name = "News.findByLink", query = "SELECT n FROM News n WHERE n.link = :link"),
    @NamedQuery(name = "News.findByGuid", query = "SELECT n FROM News n WHERE n.guid = :guid"),
    @NamedQuery(name = "News.findByPubDate", query = "SELECT n FROM News n WHERE n.pubDate = :pubDate"),
    @NamedQuery(name = "News.findByCategory", query = "SELECT n FROM News n WHERE n.category = :category")})
public class News implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idNews")
    private Integer idNews;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "link")
    private String link;
    @Basic(optional = false)
    @Column(name = "guid")
    private String guid;
    @Basic(optional = false)
    @Column(name = "pubDate")
    @Temporal(TemporalType.DATE)
    private Date pubDate;
    @Basic(optional = false)
    @Column(name = "category")
    private String category;
    @JoinColumn(name = "idFlux", referencedColumnName = "idFlux")
    @ManyToOne(optional = false)
    private Flux idFlux;

    public News() {
    }

    public News(Integer idNews) {
        this.idNews = idNews;
    }

    public News(Integer idNews, String title, String description, String link, String guid, Date pubDate, String category) {
        this.idNews = idNews;
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        this.pubDate = pubDate;
        this.category = category;
    }

    public Integer getIdNews() {
        return idNews;
    }

    public void setIdNews(Integer idNews) {
        this.idNews = idNews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Flux getIdFlux() {
        return idFlux;
    }

    public void setIdFlux(Flux idFlux) {
        this.idFlux = idFlux;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNews != null ? idNews.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof News)) {
            return false;
        }
        News other = (News) object;
        if ((this.idNews == null && other.idNews != null) || (this.idNews != null && !this.idNews.equals(other.idNews))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.News[ idNews=" + idNews + " ]";
    }
    
}
