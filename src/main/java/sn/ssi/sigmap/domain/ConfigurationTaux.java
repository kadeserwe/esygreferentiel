package sn.ssi.sigmap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConfigurationTaux.
 */
@Entity
@Table(name = "configuration_taux")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigurationTaux implements Serializable {

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
  @Column(name = "taux", nullable = false)
  private Double taux;

  @Column(name = "date_debut")
  private Instant dateDebut;

  @Column(name = "date_fin")
  private Instant dateFin;

  @Column(name = "invalid")
  private Boolean invalid;

  @ManyToOne
  private Pays paysA;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ConfigurationTaux id(Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return this.code;
  }

  public ConfigurationTaux code(String code) {
    this.code = code;
    return this;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public ConfigurationTaux libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public Double getTaux() {
    return this.taux;
  }

  public ConfigurationTaux taux(Double taux) {
    this.taux = taux;
    return this;
  }

  public void setTaux(Double taux) {
    this.taux = taux;
  }

  public Instant getDateDebut() {
    return this.dateDebut;
  }

  public ConfigurationTaux dateDebut(Instant dateDebut) {
    this.dateDebut = dateDebut;
    return this;
  }

  public void setDateDebut(Instant dateDebut) {
    this.dateDebut = dateDebut;
  }

  public Instant getDateFin() {
    return this.dateFin;
  }

  public ConfigurationTaux dateFin(Instant dateFin) {
    this.dateFin = dateFin;
    return this;
  }

  public void setDateFin(Instant dateFin) {
    this.dateFin = dateFin;
  }

  public Boolean getInvalid() {
    return this.invalid;
  }

  public ConfigurationTaux invalid(Boolean invalid) {
    this.invalid = invalid;
    return this;
  }

  public void setInvalid(Boolean invalid) {
    this.invalid = invalid;
  }

  public Pays getPaysA() {
    return this.paysA;
  }

  public ConfigurationTaux paysA(Pays pays) {
    this.setPaysA(pays);
    return this;
  }

  public void setPaysA(Pays pays) {
    this.paysA = pays;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConfigurationTaux)) {
      return false;
    }
    return id != null && id.equals(((ConfigurationTaux) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ConfigurationTaux{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", taux=" + getTaux() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", invalid='" + getInvalid() + "'" +
            "}";
    }
}
