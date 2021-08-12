package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.CriteresQualification;

/**
 * Spring Data SQL repository for the CriteresQualification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriteresQualificationRepository extends JpaRepository<CriteresQualification, Long> {}
