package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.JoursFeries;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the JoursFeries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JoursFeriesRepository extends JpaRepository<JoursFeries, Long> {
}
