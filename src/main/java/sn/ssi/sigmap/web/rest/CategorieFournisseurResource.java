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
import sn.ssi.sigmap.domain.CategorieFournisseur;
import sn.ssi.sigmap.repository.CategorieFournisseurRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.CategorieFournisseur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CategorieFournisseurResource {

  private final Logger log = LoggerFactory.getLogger(CategorieFournisseurResource.class);

  private static final String ENTITY_NAME = "referentielmsCategorieFournisseur";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final CategorieFournisseurRepository categorieFournisseurRepository;

  public CategorieFournisseurResource(CategorieFournisseurRepository categorieFournisseurRepository) {
    this.categorieFournisseurRepository = categorieFournisseurRepository;
  }

  /**
   * {@code POST  /categorie-fournisseurs} : Create a new categorieFournisseur.
   *
   * @param categorieFournisseur the categorieFournisseur to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorieFournisseur, or with status {@code 400 (Bad Request)} if the categorieFournisseur has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/categorie-fournisseurs")
  public ResponseEntity<CategorieFournisseur> createCategorieFournisseur(@Valid @RequestBody CategorieFournisseur categorieFournisseur)
    throws URISyntaxException {
    log.debug("REST request to save CategorieFournisseur : {}", categorieFournisseur);
    if (categorieFournisseur.getId() != null) {
      throw new BadRequestAlertException("A new categorieFournisseur cannot already have an ID", ENTITY_NAME, "idexists");
    }
    CategorieFournisseur result = categorieFournisseurRepository.save(categorieFournisseur);
    return ResponseEntity
      .created(new URI("/api/categorie-fournisseurs/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /categorie-fournisseurs/:id} : Updates an existing categorieFournisseur.
   *
   * @param id the id of the categorieFournisseur to save.
   * @param categorieFournisseur the categorieFournisseur to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieFournisseur,
   * or with status {@code 400 (Bad Request)} if the categorieFournisseur is not valid,
   * or with status {@code 500 (Internal Server Error)} if the categorieFournisseur couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/categorie-fournisseurs/{id}")
  public ResponseEntity<CategorieFournisseur> updateCategorieFournisseur(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody CategorieFournisseur categorieFournisseur
  ) throws URISyntaxException {
    log.debug("REST request to update CategorieFournisseur : {}, {}", id, categorieFournisseur);
    if (categorieFournisseur.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, categorieFournisseur.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!categorieFournisseurRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    CategorieFournisseur result = categorieFournisseurRepository.save(categorieFournisseur);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieFournisseur.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /categorie-fournisseurs/:id} : Partial updates given fields of an existing categorieFournisseur, field will ignore if it is null
   *
   * @param id the id of the categorieFournisseur to save.
   * @param categorieFournisseur the categorieFournisseur to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieFournisseur,
   * or with status {@code 400 (Bad Request)} if the categorieFournisseur is not valid,
   * or with status {@code 404 (Not Found)} if the categorieFournisseur is not found,
   * or with status {@code 500 (Internal Server Error)} if the categorieFournisseur couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/categorie-fournisseurs/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<CategorieFournisseur> partialUpdateCategorieFournisseur(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody CategorieFournisseur categorieFournisseur
  ) throws URISyntaxException {
    log.debug("REST request to partial update CategorieFournisseur partially : {}, {}", id, categorieFournisseur);
    if (categorieFournisseur.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, categorieFournisseur.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!categorieFournisseurRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<CategorieFournisseur> result = categorieFournisseurRepository
      .findById(categorieFournisseur.getId())
      .map(
        existingCategorieFournisseur -> {
          if (categorieFournisseur.getLibelle() != null) {
            existingCategorieFournisseur.setLibelle(categorieFournisseur.getLibelle());
          }
          if (categorieFournisseur.getDescription() != null) {
            existingCategorieFournisseur.setDescription(categorieFournisseur.getDescription());
          }

          return existingCategorieFournisseur;
        }
      )
      .map(categorieFournisseurRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieFournisseur.getId().toString())
    );
  }

  /**
   * {@code GET  /categorie-fournisseurs} : get all the categorieFournisseurs.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorieFournisseurs in body.
   */
  @GetMapping("/categorie-fournisseurs")
  public List<CategorieFournisseur> getAllCategorieFournisseurs() {
    log.debug("REST request to get all CategorieFournisseurs");
    return categorieFournisseurRepository.findAll();
  }

  /**
   * {@code GET  /categorie-fournisseurs/:id} : get the "id" categorieFournisseur.
   *
   * @param id the id of the categorieFournisseur to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorieFournisseur, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/categorie-fournisseurs/{id}")
  public ResponseEntity<CategorieFournisseur> getCategorieFournisseur(@PathVariable Long id) {
    log.debug("REST request to get CategorieFournisseur : {}", id);
    Optional<CategorieFournisseur> categorieFournisseur = categorieFournisseurRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(categorieFournisseur);
  }

  /**
   * {@code DELETE  /categorie-fournisseurs/:id} : delete the "id" categorieFournisseur.
   *
   * @param id the id of the categorieFournisseur to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/categorie-fournisseurs/{id}")
  public ResponseEntity<Void> deleteCategorieFournisseur(@PathVariable Long id) {
    log.debug("REST request to delete CategorieFournisseur : {}", id);
    categorieFournisseurRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
      .build();
  }
}
