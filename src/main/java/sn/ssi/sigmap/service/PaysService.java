package sn.ssi.sigmap.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ssi.sigmap.domain.Pays;
import sn.ssi.sigmap.repository.PaysRepository;

/**
 * Service Implementation for managing {@link Pays}.
 */
@Service
@Transactional
public class PaysService {

  private final Logger log = LoggerFactory.getLogger(PaysService.class);

  private final PaysRepository paysRepository;

  public PaysService(PaysRepository paysRepository) {
    this.paysRepository = paysRepository;
  }

  /**
   * Save a pays.
   *
   * @param pays the entity to save.
   * @return the persisted entity.
   */
  public Pays save(Pays pays) {
    log.debug("Request to save Pays : {}", pays);
    return paysRepository.save(pays);
  }

  /**
   * Partially update a pays.
   *
   * @param pays the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<Pays> partialUpdate(Pays pays) {
    log.debug("Request to partially update Pays : {}", pays);

    return paysRepository
      .findById(pays.getId())
      .map(
        existingPays -> {
          if (pays.getLibelle() != null) {
            existingPays.setLibelle(pays.getLibelle());
          }
          if (pays.getCodepays() != null) {
            existingPays.setCodepays(pays.getCodepays());
          }

          return existingPays;
        }
      )
      .map(paysRepository::save);
  }

  /**
   * Get all the pays.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<Pays> findAll(Pageable pageable) {
    log.debug("Request to get all Pays");
    return paysRepository.findAll(pageable);
  }

  /**
   * Get one pays by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<Pays> findOne(Long id) {
    log.debug("Request to get Pays : {}", id);
    return paysRepository.findById(id);
  }

  /**
   * Delete the pays by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Pays : {}", id);
    paysRepository.deleteById(id);
  }
}
