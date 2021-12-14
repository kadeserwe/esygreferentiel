package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.PiecesAdministratives;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PiecesAdministratives entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PiecesAdministrativesRepository extends JpaRepository<PiecesAdministratives, Long> {
}
