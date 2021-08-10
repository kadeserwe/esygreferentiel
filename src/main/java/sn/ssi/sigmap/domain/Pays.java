package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pays.
 */
@Entity
@Table(name = "pays")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pays implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @Column(name = "libelle")
  private String libelle;

  @Column(name = "codepays")
  private String codepays;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Pays id(Long id) {
    this.id = id;
    return this;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public Pays libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getCodepays() {
    return this.codepays;
  }

  public Pays codepays(String codepays) {
    this.codepays = codepays;
    return this;
  }

  public void setCodepays(String codepays) {
    this.codepays = codepays;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pays)) {
      return false;
    }
    return id != null && id.equals(((Pays) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", codepays='" + getCodepays() + "'" +
            "}";
    }
}
