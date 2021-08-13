package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CategorieFournisseur.
 */
@Entity
@Table(name = "categorie_fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CategorieFournisseur implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Size(max = 255)
  @Column(name = "libelle", length = 255, nullable = false)
  private String libelle;

  @Column(name = "description")
  private String description;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CategorieFournisseur id(Long id) {
    this.id = id;
    return this;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public CategorieFournisseur libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getDescription() {
    return this.description;
  }

  public CategorieFournisseur description(String description) {
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
    if (!(o instanceof CategorieFournisseur)) {
      return false;
    }
    return id != null && id.equals(((CategorieFournisseur) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CategorieFournisseur{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
