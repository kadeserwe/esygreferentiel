package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.CategorieFournisseur;
import sn.ssi.sigmap.repository.CategorieFournisseurRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<CategorieFournisseur> createCategorieFournisseur(@Valid @RequestBody CategorieFournisseur categorieFournisseur) throws URISyntaxException {
        log.debug("REST request to save CategorieFournisseur : {}", categorieFournisseur);
        if (categorieFournisseur.getId() != null) {
            throw new BadRequestAlertException("A new categorieFournisseur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorieFournisseur result = categorieFournisseurRepository.save(categorieFournisseur);
        return ResponseEntity.created(new URI("/api/categorie-fournisseurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorie-fournisseurs} : Updates an existing categorieFournisseur.
     *
     * @param categorieFournisseur the categorieFournisseur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieFournisseur,
     * or with status {@code 400 (Bad Request)} if the categorieFournisseur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorieFournisseur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorie-fournisseurs")
    public ResponseEntity<CategorieFournisseur> updateCategorieFournisseur(@Valid @RequestBody CategorieFournisseur categorieFournisseur) throws URISyntaxException {
        log.debug("REST request to update CategorieFournisseur : {}", categorieFournisseur);
        if (categorieFournisseur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CategorieFournisseur result = categorieFournisseurRepository.save(categorieFournisseur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieFournisseur.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /categorie-fournisseurs} : get all the categorieFournisseurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorieFournisseurs in body.
     */
    @GetMapping("/categorie-fournisseurs")
    public ResponseEntity<List<CategorieFournisseur>> getAllCategorieFournisseurs(Pageable pageable) {
        log.debug("REST request to get a page of CategorieFournisseurs");
        Page<CategorieFournisseur> page = categorieFournisseurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
