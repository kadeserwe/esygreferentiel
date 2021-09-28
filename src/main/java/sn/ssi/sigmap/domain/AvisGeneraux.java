package sn.ssi.sigmap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A AvisGeneraux.
 */
@Entity
@Table(name = "avis_generaux")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AvisGeneraux implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "annee")
    private String annee;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "fichier_avis")
    private byte[] fichierAvis;

    @Column(name = "fichier_avis_content_type")
    private String fichierAvisContentType;

    @Column(name = "version")
    private Integer version;

    @Column(name = "last_version_valid")
    private Integer lastVersionValid;

    @Column(name = "etat")
    private String etat;

    @Column(name = "date_publication")
    private Instant datePublication;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public AvisGeneraux numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAnnee() {
        return annee;
    }

    public AvisGeneraux annee(String annee) {
        this.annee = annee;
        return this;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getDescription() {
        return description;
    }

    public AvisGeneraux description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFichierAvis() {
        return fichierAvis;
    }

    public AvisGeneraux fichierAvis(byte[] fichierAvis) {
        this.fichierAvis = fichierAvis;
        return this;
    }

    public void setFichierAvis(byte[] fichierAvis) {
        this.fichierAvis = fichierAvis;
    }

    public String getFichierAvisContentType() {
        return fichierAvisContentType;
    }

    public AvisGeneraux fichierAvisContentType(String fichierAvisContentType) {
        this.fichierAvisContentType = fichierAvisContentType;
        return this;
    }

    public void setFichierAvisContentType(String fichierAvisContentType) {
        this.fichierAvisContentType = fichierAvisContentType;
    }

    public Integer getVersion() {
        return version;
    }

    public AvisGeneraux version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLastVersionValid() {
        return lastVersionValid;
    }

    public AvisGeneraux lastVersionValid(Integer lastVersionValid) {
        this.lastVersionValid = lastVersionValid;
        return this;
    }

    public void setLastVersionValid(Integer lastVersionValid) {
        this.lastVersionValid = lastVersionValid;
    }

    public String getEtat() {
        return etat;
    }

    public AvisGeneraux etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Instant getDatePublication() {
        return datePublication;
    }

    public AvisGeneraux datePublication(Instant datePublication) {
        this.datePublication = datePublication;
        return this;
    }

    public void setDatePublication(Instant datePublication) {
        this.datePublication = datePublication;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvisGeneraux)) {
            return false;
        }
        return id != null && id.equals(((AvisGeneraux) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvisGeneraux{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", annee='" + getAnnee() + "'" +
            ", description='" + getDescription() + "'" +
            ", fichierAvis='" + getFichierAvis() + "'" +
            ", fichierAvisContentType='" + getFichierAvisContentType() + "'" +
            ", version=" + getVersion() +
            ", lastVersionValid=" + getLastVersionValid() +
            ", etat='" + getEtat() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            "}";
    }
}
