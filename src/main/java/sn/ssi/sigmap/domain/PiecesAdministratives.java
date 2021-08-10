package sn.ssi.sigmap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

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
    @Column(name = "code_piece", nullable = false)
    private String codePiece;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "localisation", nullable = false, unique = true)
    private enumLocalisation localisation;

    @Column(name = "type")
    private Integer type;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePiece() {
        return codePiece;
    }

    public PiecesAdministratives codePiece(String codePiece) {
        this.codePiece = codePiece;
        return this;
    }

    public void setCodePiece(String codePiece) {
        this.codePiece = codePiece;
    }

    public String getLibelle() {
        return libelle;
    }

    public PiecesAdministratives libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public enumLocalisation getLocalisation() {
        return localisation;
    }

    public PiecesAdministratives localisation(enumLocalisation localisation) {
        this.localisation = localisation;
        return this;
    }

    public void setLocalisation(enumLocalisation localisation) {
        this.localisation = localisation;
    }

    public Integer getType() {
        return type;
    }

    public PiecesAdministratives type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PiecesAdministratives{" +
            "id=" + getId() +
            ", codePiece='" + getCodePiece() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            ", type=" + getType() +
            "}";
    }
}
