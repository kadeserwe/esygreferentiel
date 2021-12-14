package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.ModeSelection;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ModeSelection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModeSelectionRepository extends JpaRepository<ModeSelection, Long> {
}
