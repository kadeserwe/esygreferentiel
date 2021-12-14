package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.SpecialitesPersonnel;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SpecialitesPersonnel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialitesPersonnelRepository extends JpaRepository<SpecialitesPersonnel, Long> {
}
