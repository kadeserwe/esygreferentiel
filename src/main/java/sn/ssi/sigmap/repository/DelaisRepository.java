package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.Delais;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Delais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DelaisRepository extends JpaRepository<Delais, Long> {
}
