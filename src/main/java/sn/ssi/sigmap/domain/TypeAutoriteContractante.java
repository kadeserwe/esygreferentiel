package sn.ssi.sigmap.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ssi.sigmap.domain.enumeration.enumTypeAutoContract;

/**
 * A TypeAutoriteContractante.
 */
@Entity
@Table(name = "type_autorite_contractante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeAutoriteContractante implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Column(name = "libelle", nullable = false)
  private String libelle;

  @NotNull
  @Column(name = "code", nullable = false)
  private String code;

  @NotNull
  @Column(name = "ordre", nullable = false)
  private Integer ordre;

  @Enumerated(EnumType.STRING)
  @Column(name = "chapitre")
  private enumTypeAutoContract chapitre;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TypeAutoriteContractante id(Long id) {
    this.id = id;
    return this;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public TypeAutoriteContractante libelle(String libelle) {
    this.libelle = libelle;
    return this;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getCode() {
    return this.code;
  }

  public TypeAutoriteContractante code(String code) {
    this.code = code;
    return this;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getOrdre() {
    return this.ordre;
  }

  public TypeAutoriteContractante ordre(Integer ordre) {
    this.ordre = ordre;
    return this;
  }

  public void setOrdre(Integer ordre) {
    this.ordre = ordre;
  }

  public enumTypeAutoContract getChapitre() {
    return this.chapitre;
  }

  public TypeAutoriteContractante chapitre(enumTypeAutoContract chapitre) {
    this.chapitre = chapitre;
    return this;
  }

  public void setChapitre(enumTypeAutoContract chapitre) {
    this.chapitre = chapitre;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TypeAutoriteContractante)) {
      return false;
    }
    return id != null && id.equals(((TypeAutoriteContractante) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "TypeAutoriteContractante{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code='" + getCode() + "'" +
            ", ordre=" + getOrdre() +
            ", chapitre='" + getChapitre() + "'" +
            "}";
    }
}
