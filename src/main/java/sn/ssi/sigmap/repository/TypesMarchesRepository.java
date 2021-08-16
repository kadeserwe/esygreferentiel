package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.TypesMarches;

/**
 * Spring Data SQL repository for the TypesMarches entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypesMarchesRepository extends JpaRepository<TypesMarches, Long> {}
