package sn.ssi.sigmap.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SygAutoriteContractante.
 */
@Entity
@Table(name = "syg_autorite_contractante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SygAutoriteContractante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "ordre", nullable = false)
    private Integer ordre;

    @NotNull
    @Column(name = "denomination", nullable = false)
    private String denomination;

    @NotNull
    @Column(name = "responsable", nullable = false)
    private String responsable;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "fax")
    private String fax;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "sigle", nullable = false)
    private String sigle;

    @Column(name = "urlsiteweb")
    private String urlsiteweb;

    @Column(name = "approbation")
    private String approbation;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "sygAutoriteContractantes", allowSetters = true)
    private TypeAutoriteContractante typeAutoriteContractante;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public SygAutoriteContractante ordre(Integer ordre) {
        this.ordre = ordre;
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getDenomination() {
        return denomination;
    }

    public SygAutoriteContractante denomination(String denomination) {
        this.denomination = denomination;
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getResponsable() {
        return responsable;
    }

    public SygAutoriteContractante responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getAdresse() {
        return adresse;
    }

    public SygAutoriteContractante adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public SygAutoriteContractante telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public SygAutoriteContractante fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public SygAutoriteContractante email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSigle() {
        return sigle;
    }

    public SygAutoriteContractante sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getUrlsiteweb() {
        return urlsiteweb;
    }

    public SygAutoriteContractante urlsiteweb(String urlsiteweb) {
        this.urlsiteweb = urlsiteweb;
        return this;
    }

    public void setUrlsiteweb(String urlsiteweb) {
        this.urlsiteweb = urlsiteweb;
    }

    public String getApprobation() {
        return approbation;
    }

    public SygAutoriteContractante approbation(String approbation) {
        this.approbation = approbation;
        return this;
    }

    public void setApprobation(String approbation) {
        this.approbation = approbation;
    }

    public byte[] getLogo() {
        return logo;
    }

    public SygAutoriteContractante logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public SygAutoriteContractante logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public TypeAutoriteContractante getTypeAutoriteContractante() {
        return typeAutoriteContractante;
    }

    public SygAutoriteContractante typeAutoriteContractante(TypeAutoriteContractante typeAutoriteContractante) {
        this.typeAutoriteContractante = typeAutoriteContractante;
        return this;
    }

    public void setTypeAutoriteContractante(TypeAutoriteContractante typeAutoriteContractante) {
        this.typeAutoriteContractante = typeAutoriteContractante;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SygAutoriteContractante)) {
            return false;
        }
        return id != null && id.equals(((SygAutoriteContractante) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SygAutoriteContractante{" +
            "id=" + getId() +
            ", ordre=" + getOrdre() +
            ", denomination='" + getDenomination() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", fax='" + getFax() + "'" +
            ", email='" + getEmail() + "'" +
            ", sigle='" + getSigle() + "'" +
            ", urlsiteweb='" + getUrlsiteweb() + "'" +
            ", approbation='" + getApprobation() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
