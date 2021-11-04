package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.PersonnesRessources;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PersonnesRessources entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnesRessourcesRepository extends JpaRepository<PersonnesRessources, Long> {
}
