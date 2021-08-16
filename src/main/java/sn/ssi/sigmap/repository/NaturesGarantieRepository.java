package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.NaturesGarantie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the NaturesGarantie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NaturesGarantieRepository extends JpaRepository<NaturesGarantie, Long> {
}
