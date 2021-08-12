package sn.ssi.sigmap.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ssi.sigmap.domain.Banque;

/**
 * Spring Data SQL repository for the Banque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BanqueRepository extends JpaRepository<Banque, Long> {}
