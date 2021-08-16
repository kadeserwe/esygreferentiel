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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ssi.sigmap.domain.SpecialitesPersonnel;
import sn.ssi.sigmap.repository.SpecialitesPersonnelRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.SpecialitesPersonnel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecialitesPersonnelResource {

  private final Logger log = LoggerFactory.getLogger(SpecialitesPersonnelResource.class);

  private static final String ENTITY_NAME = "referentielmsSpecialitesPersonnel";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SpecialitesPersonnelRepository specialitesPersonnelRepository;

  public SpecialitesPersonnelResource(SpecialitesPersonnelRepository specialitesPersonnelRepository) {
    this.specialitesPersonnelRepository = specialitesPersonnelRepository;
  }

  /**
   * {@code POST  /specialites-personnels} : Create a new specialitesPersonnel.
   *
   * @param specialitesPersonnel the specialitesPersonnel to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialitesPersonnel, or with status {@code 400 (Bad Request)} if the specialitesPersonnel has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/specialites-personnels")
  public ResponseEntity<SpecialitesPersonnel> createSpecialitesPersonnel(@Valid @RequestBody SpecialitesPersonnel specialitesPersonnel)
    throws URISyntaxException {
    log.debug("REST request to save SpecialitesPersonnel : {}", specialitesPersonnel);
    if (specialitesPersonnel.getId() != null) {
      throw new BadRequestAlertException("A new specialitesPersonnel cannot already have an ID", ENTITY_NAME, "idexists");
    }
    SpecialitesPersonnel result = specialitesPersonnelRepository.save(specialitesPersonnel);
    return ResponseEntity
      .created(new URI("/api/specialites-personnels/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /specialites-personnels/:id} : Updates an existing specialitesPersonnel.
   *
   * @param id the id of the specialitesPersonnel to save.
   * @param specialitesPersonnel the specialitesPersonnel to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialitesPersonnel,
   * or with status {@code 400 (Bad Request)} if the specialitesPersonnel is not valid,
   * or with status {@code 500 (Internal Server Error)} if the specialitesPersonnel couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/specialites-personnels/{id}")
  public ResponseEntity<SpecialitesPersonnel> updateSpecialitesPersonnel(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody SpecialitesPersonnel specialitesPersonnel
  ) throws URISyntaxException {
    log.debug("REST request to update SpecialitesPersonnel : {}, {}", id, specialitesPersonnel);
    if (specialitesPersonnel.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, specialitesPersonnel.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!specialitesPersonnelRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    SpecialitesPersonnel result = specialitesPersonnelRepository.save(specialitesPersonnel);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialitesPersonnel.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /specialites-personnels/:id} : Partial updates given fields of an existing specialitesPersonnel, field will ignore if it is null
   *
   * @param id the id of the specialitesPersonnel to save.
   * @param specialitesPersonnel the specialitesPersonnel to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialitesPersonnel,
   * or with status {@code 400 (Bad Request)} if the specialitesPersonnel is not valid,
   * or with status {@code 404 (Not Found)} if the specialitesPersonnel is not found,
   * or with status {@code 500 (Internal Server Error)} if the specialitesPersonnel couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/specialites-personnels/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<SpecialitesPersonnel> partialUpdateSpecialitesPersonnel(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody SpecialitesPersonnel specialitesPersonnel
  ) throws URISyntaxException {
    log.debug("REST request to partial update SpecialitesPersonnel partially : {}, {}", id, specialitesPersonnel);
    if (specialitesPersonnel.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, specialitesPersonnel.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!specialitesPersonnelRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<SpecialitesPersonnel> result = specialitesPersonnelRepository
      .findById(specialitesPersonnel.getId())
      .map(
        existingSpecialitesPersonnel -> {
          if (specialitesPersonnel.getLibelle() != null) {
            existingSpecialitesPersonnel.setLibelle(specialitesPersonnel.getLibelle());
          }

          return existingSpecialitesPersonnel;
        }
      )
      .map(specialitesPersonnelRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialitesPersonnel.getId().toString())
    );
  }

  /**
   * {@code GET  /specialites-personnels} : get all the specialitesPersonnels.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialitesPersonnels in body.
   */
  @GetMapping("/specialites-personnels")
  public ResponseEntity<List<SpecialitesPersonnel>> getAllSpecialitesPersonnels(Pageable pageable) {
    log.debug("REST request to get a page of SpecialitesPersonnels");
    Page<SpecialitesPersonnel> page = specialitesPersonnelRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /specialites-personnels/:id} : get the "id" specialitesPersonnel.
   *
   * @param id the id of the specialitesPersonnel to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialitesPersonnel, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/specialites-personnels/{id}")
  public ResponseEntity<SpecialitesPersonnel> getSpecialitesPersonnel(@PathVariable Long id) {
    log.debug("REST request to get SpecialitesPersonnel : {}", id);
    Optional<SpecialitesPersonnel> specialitesPersonnel = specialitesPersonnelRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(specialitesPersonnel);
  }

  /**
   * {@code DELETE  /specialites-personnels/:id} : delete the "id" specialitesPersonnel.
   *
   * @param id the id of the specialitesPersonnel to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/specialites-personnels/{id}")
  public ResponseEntity<Void> deleteSpecialitesPersonnel(@PathVariable Long id) {
    log.debug("REST request to delete SpecialitesPersonnel : {}", id);
    specialitesPersonnelRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
      .build();
  }
}
