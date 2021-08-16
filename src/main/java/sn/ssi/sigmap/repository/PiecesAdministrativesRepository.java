package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.PiecesAdministratives;

/**
 * Spring Data SQL repository for the PiecesAdministratives entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PiecesAdministrativesRepository extends JpaRepository<PiecesAdministratives, Long> {}
