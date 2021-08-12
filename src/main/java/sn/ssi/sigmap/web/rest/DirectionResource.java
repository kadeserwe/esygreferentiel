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
import sn.ssi.sigmap.domain.Direction;
import sn.ssi.sigmap.repository.DirectionRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.Direction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DirectionResource {

  private final Logger log = LoggerFactory.getLogger(DirectionResource.class);

  private static final String ENTITY_NAME = "referentielmsDirection";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final DirectionRepository directionRepository;

  public DirectionResource(DirectionRepository directionRepository) {
    this.directionRepository = directionRepository;
  }

  /**
   * {@code POST  /directions} : Create a new direction.
   *
   * @param direction the direction to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new direction, or with status {@code 400 (Bad Request)} if the direction has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/directions")
  public ResponseEntity<Direction> createDirection(@Valid @RequestBody Direction direction) throws URISyntaxException {
    log.debug("REST request to save Direction : {}", direction);
    if (direction.getId() != null) {
      throw new BadRequestAlertException("A new direction cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Direction result = directionRepository.save(direction);
    return ResponseEntity
      .created(new URI("/api/directions/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /directions/:id} : Updates an existing direction.
   *
   * @param id the id of the direction to save.
   * @param direction the direction to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direction,
   * or with status {@code 400 (Bad Request)} if the direction is not valid,
   * or with status {@code 500 (Internal Server Error)} if the direction couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/directions/{id}")
  public ResponseEntity<Direction> updateDirection(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody Direction direction
  ) throws URISyntaxException {
    log.debug("REST request to update Direction : {}, {}", id, direction);
    if (direction.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, direction.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!directionRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Direction result = directionRepository.save(direction);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direction.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /directions/:id} : Partial updates given fields of an existing direction, field will ignore if it is null
   *
   * @param id the id of the direction to save.
   * @param direction the direction to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direction,
   * or with status {@code 400 (Bad Request)} if the direction is not valid,
   * or with status {@code 404 (Not Found)} if the direction is not found,
   * or with status {@code 500 (Internal Server Error)} if the direction couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/directions/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<Direction> partialUpdateDirection(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody Direction direction
  ) throws URISyntaxException {
    log.debug("REST request to partial update Direction partially : {}, {}", id, direction);
    if (direction.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, direction.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!directionRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<Direction> result = directionRepository
      .findById(direction.getId())
      .map(
        existingDirection -> {
          if (direction.getSigle() != null) {
            existingDirection.setSigle(direction.getSigle());
          }
          if (direction.getLibelle() != null) {
            existingDirection.setLibelle(direction.getLibelle());
          }
          if (direction.getDescription() != null) {
            existingDirection.setDescription(direction.getDescription());
          }

          return existingDirection;
        }
      )
      .map(directionRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direction.getId().toString())
    );
  }

  /**
   * {@code GET  /directions} : get all the directions.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directions in body.
   */
  @GetMapping("/directions")
  public List<Direction> getAllDirections() {
    log.debug("REST request to get all Directions");
    return directionRepository.findAll();
  }

  /**
   * {@code GET  /directions/:id} : get the "id" direction.
   *
   * @param id the id of the direction to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the direction, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/directions/{id}")
  public ResponseEntity<Direction> getDirection(@PathVariable Long id) {
    log.debug("REST request to get Direction : {}", id);
    Optional<Direction> direction = directionRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(direction);
  }

  /**
   * {@code DELETE  /directions/:id} : delete the "id" direction.
   *
   * @param id the id of the direction to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/directions/{id}")
  public ResponseEntity<Void> deleteDirection(@PathVariable Long id) {
    log.debug("REST request to delete Direction : {}", id);
    directionRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
      .build();
  }
}
