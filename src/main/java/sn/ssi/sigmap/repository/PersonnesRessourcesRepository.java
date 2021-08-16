package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.PersonnesRessources;

/**
 * Spring Data SQL repository for the PersonnesRessources entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnesRessourcesRepository extends JpaRepository<PersonnesRessources, Long> {}
