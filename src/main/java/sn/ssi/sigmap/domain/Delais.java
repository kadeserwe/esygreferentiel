package sn.ssi.sigmap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Delais.
 */
@Entity
@Table(name = "delais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Delais implements Serializable {

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
    @Column(name = "unite", nullable = false)
    private String unite;

    @NotNull
    @Column(name = "valeur", nullable = false)
    private Integer valeur;

    @NotNull
    @Column(name = "debut_validite", nullable = false)
    private Instant debutValidite;

    @NotNull
    @Column(name = "fin_validite", nullable = false)
    private Instant finValidite;

    @Column(name = "commentaires")
    private String commentaires;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Delais libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public Delais code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnite() {
        return unite;
    }

    public Delais unite(String unite) {
        this.unite = unite;
        return this;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Integer getValeur() {
        return valeur;
    }

    public Delais valeur(Integer valeur) {
        this.valeur = valeur;
        return this;
    }

    public void setValeur(Integer valeur) {
        this.valeur = valeur;
    }

    public Instant getDebutValidite() {
        return debutValidite;
    }

    public Delais debutValidite(Instant debutValidite) {
        this.debutValidite = debutValidite;
        return this;
    }

    public void setDebutValidite(Instant debutValidite) {
        this.debutValidite = debutValidite;
    }

    public Instant getFinValidite() {
        return finValidite;
    }

    public Delais finValidite(Instant finValidite) {
        this.finValidite = finValidite;
        return this;
    }

    public void setFinValidite(Instant finValidite) {
        this.finValidite = finValidite;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public Delais commentaires(String commentaires) {
        this.commentaires = commentaires;
        return this;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Delais)) {
            return false;
        }
        return id != null && id.equals(((Delais) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Delais{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code='" + getCode() + "'" +
            ", unite='" + getUnite() + "'" +
            ", valeur=" + getValeur() +
            ", debutValidite='" + getDebutValidite() + "'" +
            ", finValidite='" + getFinValidite() + "'" +
            ", commentaires='" + getCommentaires() + "'" +
            "}";
    }
}
