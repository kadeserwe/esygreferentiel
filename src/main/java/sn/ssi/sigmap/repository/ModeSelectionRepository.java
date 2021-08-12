package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.ModeSelection;

/**
 * Spring Data SQL repository for the ModeSelection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModeSelectionRepository extends JpaRepository<ModeSelection, Long> {}
