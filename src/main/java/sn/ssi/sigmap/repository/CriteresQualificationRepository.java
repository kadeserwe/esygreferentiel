package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.CriteresQualification;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CriteresQualification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriteresQualificationRepository extends JpaRepository<CriteresQualification, Long> {
}
