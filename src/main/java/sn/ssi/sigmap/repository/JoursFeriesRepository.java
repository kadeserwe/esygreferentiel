package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.JoursFeries;

/**
 * Spring Data SQL repository for the JoursFeries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JoursFeriesRepository extends JpaRepository<JoursFeries, Long> {}
