package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.GroupesImputation;

/**
 * Spring Data SQL repository for the GroupesImputation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupesImputationRepository extends JpaRepository<GroupesImputation, Long> {}
