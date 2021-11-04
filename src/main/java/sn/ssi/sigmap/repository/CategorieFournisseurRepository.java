package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.CategorieFournisseur;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CategorieFournisseur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieFournisseurRepository extends JpaRepository<CategorieFournisseur, Long> {
}
