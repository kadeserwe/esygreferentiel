package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.AutoriteContractante;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AutoriteContractante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoriteContractanteRepository extends JpaRepository<AutoriteContractante, Long> {
}
