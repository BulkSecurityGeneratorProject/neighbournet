package be.sandervl.neighbournet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created")
    private LocalDate created;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "document_matches",
        joinColumns = @JoinColumn(name = "documents_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "matches_id", referencedColumnName = "ID"))
    private Set<Document> matches = new HashSet<>();

    @ManyToOne
    private Site site;

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Attribute> attributes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Document created(LocalDate created) {
        this.created = created;
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public Document url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Document> getMatches() {
        return matches;
    }

    public Document matches(Set<Document> documents) {
        this.matches = documents;
        return this;
    }

    public Document addMatches(Document document) {
        matches.add(document);
        document.getMatches().add(this);
        return this;
    }

    public Document removeMatches(Document document) {
        matches.remove(document);
        document.getMatches().remove(this);
        return this;
    }

    public void setMatches(Set<Document> documents) {
        this.matches = documents;
    }

    public Site getSite() {
        return site;
    }

    public Document site(Site site) {
        this.site = site;
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Document attributes(List<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public Document addAttributes(Attribute attribute) {
        attributes.add(attribute);
        attribute.setDocument(this);
        return this;
    }

    public Document removeAttributes(Attribute attribute) {
        attributes.remove(attribute);
        attribute.setDocument(null);
        return this;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Document document = (Document) o;
        if (document.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + id +
            ", created='" + created + "'" +
            ", url='" + url + "'" +
            '}';
    }
}
