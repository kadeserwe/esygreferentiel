package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.SituationMatrimoniale;

/**
 * Spring Data SQL repository for the SituationMatrimoniale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituationMatrimonialeRepository extends JpaRepository<SituationMatrimoniale, Long> {}
