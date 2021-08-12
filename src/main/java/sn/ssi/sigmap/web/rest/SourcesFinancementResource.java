package sn.ssi.sigmap.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sn.ssi.sigmap.domain.SourcesFinancement;
import sn.ssi.sigmap.repository.SourcesFinancementRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.SourcesFinancement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SourcesFinancementResource {

  private final Logger log = LoggerFactory.getLogger(SourcesFinancementResource.class);

  private static final String ENTITY_NAME = "referentielmsSourcesFinancement";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SourcesFinancementRepository sourcesFinancementRepository;

  public SourcesFinancementResource(SourcesFinancementRepository sourcesFinancementRepository) {
    this.sourcesFinancementRepository = sourcesFinancementRepository;
  }

  /**
   * {@code POST  /sources-financements} : Create a new sourcesFinancement.
   *
   * @param sourcesFinancement the sourcesFinancement to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourcesFinancement, or with status {@code 400 (Bad Request)} if the sourcesFinancement has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/sources-financements")
  public ResponseEntity<SourcesFinancement> createSourcesFinancement(@Valid @RequestBody SourcesFinancement sourcesFinancement)
    throws URISyntaxException {
    log.debug("REST request to save SourcesFinancement : {}", sourcesFinancement);
    if (sourcesFinancement.getId() != null) {
      throw new BadRequestAlertException("A new sourcesFinancement cannot already have an ID", ENTITY_NAME, "idexists");
    }
    SourcesFinancement result = sourcesFinancementRepository.save(sourcesFinancement);
    return ResponseEntity
      .created(new URI("/api/sources-financements/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /sources-financements/:id} : Updates an existing sourcesFinancement.
   *
   * @param id the id of the sourcesFinancement to save.
   * @param sourcesFinancement the sourcesFinancement to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcesFinancement,
   * or with status {@code 400 (Bad Request)} if the sourcesFinancement is not valid,
   * or with status {@code 500 (Internal Server Error)} if the sourcesFinancement couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/sources-financements/{id}")
  public ResponseEntity<SourcesFinancement> updateSourcesFinancement(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody SourcesFinancement sourcesFinancement
  ) throws URISyntaxException {
    log.debug("REST request to update SourcesFinancement : {}, {}", id, sourcesFinancement);
    if (sourcesFinancement.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, sourcesFinancement.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!sourcesFinancementRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    SourcesFinancement result = sourcesFinancementRepository.save(sourcesFinancement);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcesFinancement.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /sources-financements/:id} : Partial updates given fields of an existing sourcesFinancement, field will ignore if it is null
   *
   * @param id the id of the sourcesFinancement to save.
   * @param sourcesFinancement the sourcesFinancement to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcesFinancement,
   * or with status {@code 400 (Bad Request)} if the sourcesFinancement is not valid,
   * or with status {@code 404 (Not Found)} if the sourcesFinancement is not found,
   * or with status {@code 500 (Internal Server Error)} if the sourcesFinancement couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/sources-financements/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<SourcesFinancement> partialUpdateSourcesFinancement(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody SourcesFinancement sourcesFinancement
  ) throws URISyntaxException {
    log.debug("REST request to partial update SourcesFinancement partially : {}, {}", id, sourcesFinancement);
    if (sourcesFinancement.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, sourcesFinancement.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!sourcesFinancementRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<SourcesFinancement> result = sourcesFinancementRepository
      .findById(sourcesFinancement.getId())
      .map(
        existingSourcesFinancement -> {
          if (sourcesFinancement.getLibelle() != null) {
            existingSourcesFinancement.setLibelle(sourcesFinancement.getLibelle());
          }
          if (sourcesFinancement.getType() != null) {
            existingSourcesFinancement.setType(sourcesFinancement.getType());
          }

          return existingSourcesFinancement;
        }
      )
      .map(sourcesFinancementRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcesFinancement.getId().toString())
    );
  }

  /**
   * {@code GET  /sources-financements} : get all the sourcesFinancements.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourcesFinancements in body.
   */
  @GetMapping("/sources-financements")
  public List<SourcesFinancement> getAllSourcesFinancements() {
    log.debug("REST request to get all SourcesFinancements");
    return sourcesFinancementRepository.findAll();
  }

  /**
   * {@code GET  /sources-financements/:id} : get the "id" sourcesFinancement.
   *
   * @param id the id of the sourcesFinancement to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourcesFinancement, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/sources-financements/{id}")
  public ResponseEntity<SourcesFinancement> getSourcesFinancement(@PathVariable Long id) {
    log.debug("REST request to get SourcesFinancement : {}", id);
    Optional<SourcesFinancement> sourcesFinancement = sourcesFinancementRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(sourcesFinancement);
  }

  /**
   * {@code DELETE  /sources-financements/:id} : delete the "id" sourcesFinancement.
   *
   * @param id the id of the sourcesFinancement to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/sources-financements/{id}")
  public ResponseEntity<Void> deleteSourcesFinancement(@PathVariable Long id) {
    log.debug("REST request to delete SourcesFinancement : {}", id);
    sourcesFinancementRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
      .build();
  }
}
