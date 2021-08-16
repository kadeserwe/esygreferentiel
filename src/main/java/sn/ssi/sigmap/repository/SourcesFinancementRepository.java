package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.SourcesFinancement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SourcesFinancement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourcesFinancementRepository extends JpaRepository<SourcesFinancement, Long> {
}
