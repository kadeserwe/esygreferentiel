package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Direction.
 */
@Entity
@Table(name = "direction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Direction implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Column(name = "sigle", nullable = false)
  private String sigle;

  @NotNull
  @Column(name = "libelle", nullable = false)
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

  public Direction id(Long id) {
    this.id = id;
    return this;
  }

  public String getSigle() {
    return this.sigle;
  }

  public Direction sigle(String sigle) {
    this.sigle = sigle;
    return this;
  }

  public void setSigle(String sigle) {
    this.sigle = sigle;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public Direction libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getDescription() {
    return this.description;
  }

  public Direction description(String description) {
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
    if (!(o instanceof Direction)) {
      return false;
    }
    return id != null && id.equals(((Direction) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "Direction{" +
            "id=" + getId() +
            ", sigle='" + getSigle() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
