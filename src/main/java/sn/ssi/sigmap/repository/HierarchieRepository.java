package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.Hierarchie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Hierarchie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HierarchieRepository extends JpaRepository<Hierarchie, Long> {
}
