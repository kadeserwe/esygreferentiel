package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.Delais;

/**
 * Spring Data SQL repository for the Delais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DelaisRepository extends JpaRepository<Delais, Long> {}
