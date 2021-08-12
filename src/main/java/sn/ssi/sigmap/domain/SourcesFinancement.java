package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ssi.sigmap.domain.enumeration.enumSourceFin;

/**
 * A SourcesFinancement.
 */
@Entity
@Table(name = "sources_financement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SourcesFinancement implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Column(name = "libelle", nullable = false)
  private String libelle;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private enumSourceFin type;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SourcesFinancement id(Long id) {
    this.id = id;
    return this;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public SourcesFinancement libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public enumSourceFin getType() {
    return this.type;
  }

  public SourcesFinancement type(enumSourceFin type) {
    this.type = type;
    return this;
  }

  public void setType(enumSourceFin type) {
    this.type = type;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SourcesFinancement)) {
      return false;
    }
    return id != null && id.equals(((SourcesFinancement) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "SourcesFinancement{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
