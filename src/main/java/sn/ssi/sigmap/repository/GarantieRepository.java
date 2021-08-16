package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.Garantie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Garantie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GarantieRepository extends JpaRepository<Garantie, Long> {
}
