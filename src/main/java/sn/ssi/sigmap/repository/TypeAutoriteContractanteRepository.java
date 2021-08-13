package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.TypeAutoriteContractante;

/**
 * Spring Data SQL repository for the TypeAutoriteContractante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeAutoriteContractanteRepository extends JpaRepository<TypeAutoriteContractante, Long> {}
