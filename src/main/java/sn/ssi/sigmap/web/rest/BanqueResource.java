package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.Banque;
import sn.ssi.sigmap.repository.BanqueRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.Banque}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BanqueResource {

    private final Logger log = LoggerFactory.getLogger(BanqueResource.class);

    private static final String ENTITY_NAME = "referentielmsBanque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BanqueRepository banqueRepository;

    public BanqueResource(BanqueRepository banqueRepository) {
        this.banqueRepository = banqueRepository;
    }

    /**
     * {@code POST  /banques} : Create a new banque.
     *
     * @param banque the banque to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new banque, or with status {@code 400 (Bad Request)} if the banque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banques")
    public ResponseEntity<Banque> createBanque(@Valid @RequestBody Banque banque) throws URISyntaxException {
        log.debug("REST request to save Banque : {}", banque);
        if (banque.getId() != null) {
            throw new BadRequestAlertException("A new banque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Banque result = banqueRepository.save(banque);
        return ResponseEntity.created(new URI("/api/banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banques} : Updates an existing banque.
     *
     * @param banque the banque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banque,
     * or with status {@code 400 (Bad Request)} if the banque is not valid,
     * or with status {@code 500 (Internal Server Error)} if the banque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banques")
    public ResponseEntity<Banque> updateBanque(@Valid @RequestBody Banque banque) throws URISyntaxException {
        log.debug("REST request to update Banque : {}", banque);
        if (banque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Banque result = banqueRepository.save(banque);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, banque.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /banques} : get all the banques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banques in body.
     */
    @GetMapping("/banques")
    public ResponseEntity<List<Banque>> getAllBanques(Pageable pageable) {
        log.debug("REST request to get a page of Banques");
        Page<Banque> page = banqueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banques/:id} : get the "id" banque.
     *
     * @param id the id of the banque to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the banque, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banques/{id}")
    public ResponseEntity<Banque> getBanque(@PathVariable Long id) {
        log.debug("REST request to get Banque : {}", id);
        Optional<Banque> banque = banqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(banque);
    }

    /**
     * {@code DELETE  /banques/:id} : delete the "id" banque.
     *
     * @param id the id of the banque to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banques/{id}")
    public ResponseEntity<Void> deleteBanque(@PathVariable Long id) {
        log.debug("REST request to delete Banque : {}", id);
        banqueRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
