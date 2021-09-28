package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.SygAutoriteContractante;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SygAutoriteContractante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SygAutoriteContractanteRepository extends JpaRepository<SygAutoriteContractante, Long> {
}
