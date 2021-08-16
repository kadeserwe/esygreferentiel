package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.Delais;
import sn.ssi.sigmap.repository.DelaisRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.Delais}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DelaisResource {

    private final Logger log = LoggerFactory.getLogger(DelaisResource.class);

    private static final String ENTITY_NAME = "referentielmsDelais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DelaisRepository delaisRepository;

    public DelaisResource(DelaisRepository delaisRepository) {
        this.delaisRepository = delaisRepository;
    }

    /**
     * {@code POST  /delais} : Create a new delais.
     *
     * @param delais the delais to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new delais, or with status {@code 400 (Bad Request)} if the delais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delais")
    public ResponseEntity<Delais> createDelais(@Valid @RequestBody Delais delais) throws URISyntaxException {
        log.debug("REST request to save Delais : {}", delais);
        if (delais.getId() != null) {
            throw new BadRequestAlertException("A new delais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Delais result = delaisRepository.save(delais);
        return ResponseEntity.created(new URI("/api/delais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delais} : Updates an existing delais.
     *
     * @param delais the delais to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delais,
     * or with status {@code 400 (Bad Request)} if the delais is not valid,
     * or with status {@code 500 (Internal Server Error)} if the delais couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delais")
    public ResponseEntity<Delais> updateDelais(@Valid @RequestBody Delais delais) throws URISyntaxException {
        log.debug("REST request to update Delais : {}", delais);
        if (delais.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Delais result = delaisRepository.save(delais);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delais.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /delais} : get all the delais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of delais in body.
     */
    @GetMapping("/delais")
    public ResponseEntity<List<Delais>> getAllDelais(Pageable pageable) {
        log.debug("REST request to get a page of Delais");
        Page<Delais> page = delaisRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delais/:id} : get the "id" delais.
     *
     * @param id the id of the delais to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the delais, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delais/{id}")
    public ResponseEntity<Delais> getDelais(@PathVariable Long id) {
        log.debug("REST request to get Delais : {}", id);
        Optional<Delais> delais = delaisRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(delais);
    }

    /**
     * {@code DELETE  /delais/:id} : delete the "id" delais.
     *
     * @param id the id of the delais to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delais/{id}")
    public ResponseEntity<Void> deleteDelais(@PathVariable Long id) {
        log.debug("REST request to delete Delais : {}", id);
        delaisRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
