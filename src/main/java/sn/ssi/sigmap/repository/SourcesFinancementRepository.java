package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.SourcesFinancement;

/**
 * Spring Data SQL repository for the SourcesFinancement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourcesFinancementRepository extends JpaRepository<SourcesFinancement, Long> {}
