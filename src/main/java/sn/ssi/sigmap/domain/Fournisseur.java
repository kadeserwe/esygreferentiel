package sn.ssi.sigmap.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "raison_sociale", length = 255, nullable = false)
    private String raisonSociale;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Size(max = 20)
    @Column(name = "email", length = 20)
    private String email;

    @Size(max = 15)
    @Column(name = "fax", length = 15)
    private String fax;

    @Size(max = 15)
    @Column(name = "telephone", length = 15)
    private String telephone;

    @Size(max = 15)
    @Column(name = "piece_jointe", length = 15)
    private String pieceJointe;

    @Size(max = 15)
    @Column(name = "numero_registre_commerce", length = 15)
    private String numeroRegistreCommerce;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Size(max = 7)
    @Column(name = "sigle", length = 7)
    private String sigle;

    @Size(max = 7)
    @Column(name = "numero_identite_fiscale", length = 7)
    private String numeroIdentiteFiscale;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "fournisseurs", allowSetters = true)
    private CategorieFournisseur categorieFournisseur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "fournisseurs", allowSetters = true)
    private Pays pays;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public Fournisseur raisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public Fournisseur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public Fournisseur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public Fournisseur fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTelephone() {
        return telephone;
    }

    public Fournisseur telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public Fournisseur pieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
        return this;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public String getNumeroRegistreCommerce() {
        return numeroRegistreCommerce;
    }

    public Fournisseur numeroRegistreCommerce(String numeroRegistreCommerce) {
        this.numeroRegistreCommerce = numeroRegistreCommerce;
        return this;
    }

    public void setNumeroRegistreCommerce(String numeroRegistreCommerce) {
        this.numeroRegistreCommerce = numeroRegistreCommerce;
    }

    public Instant getDate() {
        return date;
    }

    public Fournisseur date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getSigle() {
        return sigle;
    }

    public Fournisseur sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getNumeroIdentiteFiscale() {
        return numeroIdentiteFiscale;
    }

    public Fournisseur numeroIdentiteFiscale(String numeroIdentiteFiscale) {
        this.numeroIdentiteFiscale = numeroIdentiteFiscale;
        return this;
    }

    public void setNumeroIdentiteFiscale(String numeroIdentiteFiscale) {
        this.numeroIdentiteFiscale = numeroIdentiteFiscale;
    }

    public CategorieFournisseur getCategorieFournisseur() {
        return categorieFournisseur;
    }

    public Fournisseur categorieFournisseur(CategorieFournisseur categorieFournisseur) {
        this.categorieFournisseur = categorieFournisseur;
        return this;
    }

    public void setCategorieFournisseur(CategorieFournisseur categorieFournisseur) {
        this.categorieFournisseur = categorieFournisseur;
    }

    public Pays getPays() {
        return pays;
    }

    public Fournisseur pays(Pays pays) {
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
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", email='" + getEmail() + "'" +
            ", fax='" + getFax() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", pieceJointe='" + getPieceJointe() + "'" +
            ", numeroRegistreCommerce='" + getNumeroRegistreCommerce() + "'" +
            ", date='" + getDate() + "'" +
            ", sigle='" + getSigle() + "'" +
            ", numeroIdentiteFiscale='" + getNumeroIdentiteFiscale() + "'" +
            "}";
    }
}
