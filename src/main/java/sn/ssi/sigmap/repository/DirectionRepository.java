package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.Direction;

/**
 * Spring Data SQL repository for the Direction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {}
