package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.ConfigurationTaux;

/**
 * Spring Data SQL repository for the ConfigurationTaux entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationTauxRepository extends JpaRepository<ConfigurationTaux, Long> {}
