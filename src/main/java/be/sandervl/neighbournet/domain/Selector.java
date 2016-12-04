package be.sandervl.neighbournet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Selector.
 */
@Entity
@Table(name = "selector")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Selector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "attribute")
    private String attribute;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @ManyToOne
    @NotNull
    private Site site;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Selector> children = new HashSet<>();

    @ManyToOne
    private Selector parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public Selector value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Selector name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public Selector attribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Boolean isIsPrimary() {
        return isPrimary;
    }

    public Selector isPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
        return this;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Site getSite() {
        return site;
    }

    public Selector site(Site site) {
        this.site = site;
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @JsonIgnore
    public Set<Selector> getChildren() {
        return children;
    }

    public Selector children(Set<Selector> selectors) {
        this.children = selectors;
        return this;
    }

    public Selector addChildren(Selector selector) {
        children.add(selector);
        selector.getChildren().add(this);
        return this;
    }

    public Selector removeChildren(Selector selector) {
        children.remove(selector);
        selector.setChildren(null);
        return this;
    }

    @JsonIgnore
    public void setChildren(Set<Selector> selectors) {
        this.children = selectors;
    }

    public Selector getParent() {
        return parent;
    }

    public Selector parent(Selector selector) {
        this.parent = selector;
        return this;
    }

    public void setParent(Selector selector) {
        this.parent = selector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Selector selector = (Selector) o;
        if(selector.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, selector.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Selector{" +
            "id=" + id +
            ", value='" + value + "'" +
            ", name='" + name + "'" +
            ", attribute='" + attribute + "'" +
            ", isPrimary='" + isPrimary + "'" +
            '}';
    }

    @JsonIgnore
    public boolean isChild() {
        return this.children == null || this.children.isEmpty();
    }
}
