package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.CategorieFournisseur;

/**
 * Spring Data SQL repository for the CategorieFournisseur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieFournisseurRepository extends JpaRepository<CategorieFournisseur, Long> {}
