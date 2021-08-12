package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ssi.sigmap.domain.enumeration.enumLocalisation;

/**
 * A PiecesAdministratives.
 */
@Entity
@Table(name = "pieces_administratives")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PiecesAdministratives implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Column(name = "code", nullable = false)
  private String code;

  @NotNull
  @Column(name = "libelle", nullable = false)
  private String libelle;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "localisation", nullable = false)
  private enumLocalisation localisation;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PiecesAdministratives id(Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return this.code;
  }

  public PiecesAdministratives code(String code) {
    this.code = code;
    return this;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public PiecesAdministratives libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public enumLocalisation getLocalisation() {
    return this.localisation;
  }

  public PiecesAdministratives localisation(enumLocalisation localisation) {
    this.localisation = localisation;
    return this;
  }

  public void setLocalisation(enumLocalisation localisation) {
    this.localisation = localisation;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PiecesAdministratives)) {
      return false;
    }
    return id != null && id.equals(((PiecesAdministratives) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "PiecesAdministratives{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            "}";
    }
}
