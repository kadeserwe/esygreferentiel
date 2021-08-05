package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.Fonction;

/**
 * Spring Data SQL repository for the Fonction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FonctionRepository extends JpaRepository<Fonction, Long> {}
