package sn.ssi.sigmap.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "configurationTauxes", allowSetters = true)
    private Pays pays;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public ConfigurationTaux code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public ConfigurationTaux libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getTaux() {
        return taux;
    }

    public ConfigurationTaux taux(Double taux) {
        this.taux = taux;
        return this;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public ConfigurationTaux dateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public ConfigurationTaux dateFin(Instant dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean isInvalid() {
        return invalid;
    }

    public ConfigurationTaux invalid(Boolean invalid) {
        this.invalid = invalid;
        return this;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public Pays getPays() {
        return pays;
    }

    public ConfigurationTaux pays(Pays pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
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
        return 31;
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
            ", invalid='" + isInvalid() + "'" +
            "}";
    }
}
