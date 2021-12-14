package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.GroupesImputation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the GroupesImputation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupesImputationRepository extends JpaRepository<GroupesImputation, Long> {
}
