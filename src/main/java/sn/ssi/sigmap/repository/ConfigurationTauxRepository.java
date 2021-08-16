package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.ConfigurationTaux;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ConfigurationTaux entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationTauxRepository extends JpaRepository<ConfigurationTaux, Long> {
}
