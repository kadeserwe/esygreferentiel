package sn.ssi.sigmap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Garantie.
 */
@Entity
@Table(name = "garantie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Garantie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "type_garantie", nullable = false)
    private String typeGarantie;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Garantie libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeGarantie() {
        return typeGarantie;
    }

    public Garantie typeGarantie(String typeGarantie) {
        this.typeGarantie = typeGarantie;
        return this;
    }

    public void setTypeGarantie(String typeGarantie) {
        this.typeGarantie = typeGarantie;
    }

    public String getDescription() {
        return description;
    }

    public Garantie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Garantie)) {
            return false;
        }
        return id != null && id.equals(((Garantie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Garantie{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", typeGarantie='" + getTypeGarantie() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
