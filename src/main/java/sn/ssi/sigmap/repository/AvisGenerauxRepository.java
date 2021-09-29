package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.AvisGeneraux;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AvisGeneraux entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvisGenerauxRepository extends JpaRepository<AvisGeneraux, Long> {
}
