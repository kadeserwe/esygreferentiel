package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CriteresQualification.
 */
@Entity
@Table(name = "criteres_qualification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CriteresQualification implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @Column(name = "libelle")
  private String libelle;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CriteresQualification id(Long id) {
    this.id = id;
    return this;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public CriteresQualification libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CriteresQualification)) {
      return false;
    }
    return id != null && id.equals(((CriteresQualification) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CriteresQualification{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
