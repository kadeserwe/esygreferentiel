package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.TypesMarches;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TypesMarches entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypesMarchesRepository extends JpaRepository<TypesMarches, Long> {
}
