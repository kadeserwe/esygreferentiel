package sn.ssi.sigmap.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A AutoriteContractante.
 */
@Entity
@Table(name = "autorite_contractante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AutoriteContractante implements Serializable {

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

    @NotNull
    @Column(name = "approbation", nullable = false)
    private String approbation;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "autoriteContractantes", allowSetters = true)
    private TypeAutoriteContractante type;

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

    public AutoriteContractante ordre(Integer ordre) {
        this.ordre = ordre;
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getDenomination() {
        return denomination;
    }

    public AutoriteContractante denomination(String denomination) {
        this.denomination = denomination;
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getResponsable() {
        return responsable;
    }

    public AutoriteContractante responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getAdresse() {
        return adresse;
    }

    public AutoriteContractante adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public AutoriteContractante telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public AutoriteContractante fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public AutoriteContractante email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSigle() {
        return sigle;
    }

    public AutoriteContractante sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getUrlsiteweb() {
        return urlsiteweb;
    }

    public AutoriteContractante urlsiteweb(String urlsiteweb) {
        this.urlsiteweb = urlsiteweb;
        return this;
    }

    public void setUrlsiteweb(String urlsiteweb) {
        this.urlsiteweb = urlsiteweb;
    }

    public String getApprobation() {
        return approbation;
    }

    public AutoriteContractante approbation(String approbation) {
        this.approbation = approbation;
        return this;
    }

    public void setApprobation(String approbation) {
        this.approbation = approbation;
    }

    public byte[] getLogo() {
        return logo;
    }

    public AutoriteContractante logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public AutoriteContractante logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public TypeAutoriteContractante getType() {
        return type;
    }

    public AutoriteContractante type(TypeAutoriteContractante typeAutoriteContractante) {
        this.type = typeAutoriteContractante;
        return this;
    }

    public void setType(TypeAutoriteContractante typeAutoriteContractante) {
        this.type = typeAutoriteContractante;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoriteContractante)) {
            return false;
        }
        return id != null && id.equals(((AutoriteContractante) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutoriteContractante{" +
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
