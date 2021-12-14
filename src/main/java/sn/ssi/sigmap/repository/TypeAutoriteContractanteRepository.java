package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.TypeAutoriteContractante;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TypeAutoriteContractante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeAutoriteContractanteRepository extends JpaRepository<TypeAutoriteContractante, Long> {
}
