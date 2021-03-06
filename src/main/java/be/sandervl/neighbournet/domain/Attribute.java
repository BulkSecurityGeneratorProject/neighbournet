package be.sandervl.neighbournet.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 * A Attribute.
 */
@Entity
@Table(name = "attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @NotNull
    private Document document;

    @ManyToOne
    @NotNull
    private Selector selector;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "attribute_relatives",
               joinColumns = @JoinColumn(name="attributes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="relatives_id", referencedColumnName="ID"))
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
    private Set<Attribute> relatives = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public Attribute value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Document getDocument() {
        return document;
    }

    public Attribute document(Document document) {
        this.document = document;
        return this;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Selector getSelector() {
        return selector;
    }

    public Attribute selector(Selector selector) {
        this.selector = selector;
        return this;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public Set<Attribute> getRelatives() {
        return relatives;
    }

    public Attribute relatives(Set<Attribute> attributes) {
        this.relatives = attributes;
        return this;
    }

    public Attribute addRelatives(Attribute attribute) {
        relatives.add(attribute);
        attribute.getRelatives().add(this);
        return this;
    }

    public Attribute removeRelatives(Attribute attribute) {
        relatives.remove(attribute);
        attribute.getRelatives().remove(this);
        return this;
    }

    public void setRelatives(Set<Attribute> attributes) {
        this.relatives = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        if(attribute.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, attribute.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + id +
            ", value='" + value + "'" +
            '}';
    }
}
