package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.SituationMatrimoniale;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SituationMatrimoniale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituationMatrimonialeRepository extends JpaRepository<SituationMatrimoniale, Long> {
}
