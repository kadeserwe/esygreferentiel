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
import sn.ssi.sigmap.domain.PersonnesRessources;
import sn.ssi.sigmap.repository.PersonnesRessourcesRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.PersonnesRessources}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonnesRessourcesResource {

  private final Logger log = LoggerFactory.getLogger(PersonnesRessourcesResource.class);

  private static final String ENTITY_NAME = "referentielmsPersonnesRessources";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PersonnesRessourcesRepository personnesRessourcesRepository;

  public PersonnesRessourcesResource(PersonnesRessourcesRepository personnesRessourcesRepository) {
    this.personnesRessourcesRepository = personnesRessourcesRepository;
  }

  /**
   * {@code POST  /personnes-ressources} : Create a new personnesRessources.
   *
   * @param personnesRessources the personnesRessources to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personnesRessources, or with status {@code 400 (Bad Request)} if the personnesRessources has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/personnes-ressources")
  public ResponseEntity<PersonnesRessources> createPersonnesRessources(@Valid @RequestBody PersonnesRessources personnesRessources)
    throws URISyntaxException {
    log.debug("REST request to save PersonnesRessources : {}", personnesRessources);
    if (personnesRessources.getId() != null) {
      throw new BadRequestAlertException("A new personnesRessources cannot already have an ID", ENTITY_NAME, "idexists");
    }
    PersonnesRessources result = personnesRessourcesRepository.save(personnesRessources);
    return ResponseEntity
      .created(new URI("/api/personnes-ressources/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /personnes-ressources/:id} : Updates an existing personnesRessources.
   *
   * @param id the id of the personnesRessources to save.
   * @param personnesRessources the personnesRessources to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnesRessources,
   * or with status {@code 400 (Bad Request)} if the personnesRessources is not valid,
   * or with status {@code 500 (Internal Server Error)} if the personnesRessources couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/personnes-ressources/{id}")
  public ResponseEntity<PersonnesRessources> updatePersonnesRessources(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody PersonnesRessources personnesRessources
  ) throws URISyntaxException {
    log.debug("REST request to update PersonnesRessources : {}, {}", id, personnesRessources);
    if (personnesRessources.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, personnesRessources.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!personnesRessourcesRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    PersonnesRessources result = personnesRessourcesRepository.save(personnesRessources);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnesRessources.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /personnes-ressources/:id} : Partial updates given fields of an existing personnesRessources, field will ignore if it is null
   *
   * @param id the id of the personnesRessources to save.
   * @param personnesRessources the personnesRessources to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnesRessources,
   * or with status {@code 400 (Bad Request)} if the personnesRessources is not valid,
   * or with status {@code 404 (Not Found)} if the personnesRessources is not found,
   * or with status {@code 500 (Internal Server Error)} if the personnesRessources couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/personnes-ressources/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<PersonnesRessources> partialUpdatePersonnesRessources(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody PersonnesRessources personnesRessources
  ) throws URISyntaxException {
    log.debug("REST request to partial update PersonnesRessources partially : {}, {}", id, personnesRessources);
    if (personnesRessources.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, personnesRessources.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!personnesRessourcesRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<PersonnesRessources> result = personnesRessourcesRepository
      .findById(personnesRessources.getId())
      .map(
        existingPersonnesRessources -> {
          if (personnesRessources.getNom() != null) {
            existingPersonnesRessources.setNom(personnesRessources.getNom());
          }
          if (personnesRessources.getPrenom() != null) {
            existingPersonnesRessources.setPrenom(personnesRessources.getPrenom());
          }
          if (personnesRessources.getTelephone() != null) {
            existingPersonnesRessources.setTelephone(personnesRessources.getTelephone());
          }
          if (personnesRessources.getEmail() != null) {
            existingPersonnesRessources.setEmail(personnesRessources.getEmail());
          }
          if (personnesRessources.getFonction() != null) {
            existingPersonnesRessources.setFonction(personnesRessources.getFonction());
          }
          if (personnesRessources.getCommentaires() != null) {
            existingPersonnesRessources.setCommentaires(personnesRessources.getCommentaires());
          }

          return existingPersonnesRessources;
        }
      )
      .map(personnesRessourcesRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnesRessources.getId().toString())
    );
  }

  /**
   * {@code GET  /personnes-ressources} : get all the personnesRessources.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnesRessources in body.
   */
  @GetMapping("/personnes-ressources")
  public List<PersonnesRessources> getAllPersonnesRessources() {
    log.debug("REST request to get all PersonnesRessources");
    return personnesRessourcesRepository.findAll();
  }

  /**
   * {@code GET  /personnes-ressources/:id} : get the "id" personnesRessources.
   *
   * @param id the id of the personnesRessources to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personnesRessources, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/personnes-ressources/{id}")
  public ResponseEntity<PersonnesRessources> getPersonnesRessources(@PathVariable Long id) {
    log.debug("REST request to get PersonnesRessources : {}", id);
    Optional<PersonnesRessources> personnesRessources = personnesRessourcesRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(personnesRessources);
  }

  /**
   * {@code DELETE  /personnes-ressources/:id} : delete the "id" personnesRessources.
   *
   * @param id the id of the personnesRessources to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/personnes-ressources/{id}")
  public ResponseEntity<Void> deletePersonnesRessources(@PathVariable Long id) {
    log.debug("REST request to delete PersonnesRessources : {}", id);
    personnesRessourcesRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
      .build();
  }
}
