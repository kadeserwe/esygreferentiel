package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.SygAutoriteContractante;
import sn.ssi.sigmap.repository.SygAutoriteContractanteRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.SygAutoriteContractante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SygAutoriteContractanteResource {

    private final Logger log = LoggerFactory.getLogger(SygAutoriteContractanteResource.class);

    private static final String ENTITY_NAME = "referentielmsSygAutoriteContractante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SygAutoriteContractanteRepository sygAutoriteContractanteRepository;

    public SygAutoriteContractanteResource(SygAutoriteContractanteRepository sygAutoriteContractanteRepository) {
        this.sygAutoriteContractanteRepository = sygAutoriteContractanteRepository;
    }

    /**
     * {@code POST  /syg-autorite-contractantes} : Create a new sygAutoriteContractante.
     *
     * @param sygAutoriteContractante the sygAutoriteContractante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sygAutoriteContractante, or with status {@code 400 (Bad Request)} if the sygAutoriteContractante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/syg-autorite-contractantes")
    public ResponseEntity<SygAutoriteContractante> createSygAutoriteContractante(@Valid @RequestBody SygAutoriteContractante sygAutoriteContractante) throws URISyntaxException {
        log.debug("REST request to save SygAutoriteContractante : {}", sygAutoriteContractante);
        if (sygAutoriteContractante.getId() != null) {
            throw new BadRequestAlertException("A new sygAutoriteContractante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SygAutoriteContractante result = sygAutoriteContractanteRepository.save(sygAutoriteContractante);
        return ResponseEntity.created(new URI("/api/syg-autorite-contractantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /syg-autorite-contractantes} : Updates an existing sygAutoriteContractante.
     *
     * @param sygAutoriteContractante the sygAutoriteContractante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sygAutoriteContractante,
     * or with status {@code 400 (Bad Request)} if the sygAutoriteContractante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sygAutoriteContractante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/syg-autorite-contractantes")
    public ResponseEntity<SygAutoriteContractante> updateSygAutoriteContractante(@Valid @RequestBody SygAutoriteContractante sygAutoriteContractante) throws URISyntaxException {
        log.debug("REST request to update SygAutoriteContractante : {}", sygAutoriteContractante);
        if (sygAutoriteContractante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SygAutoriteContractante result = sygAutoriteContractanteRepository.save(sygAutoriteContractante);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sygAutoriteContractante.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /syg-autorite-contractantes} : get all the sygAutoriteContractantes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sygAutoriteContractantes in body.
     */
    @GetMapping("/syg-autorite-contractantes")
    public ResponseEntity<List<SygAutoriteContractante>> getAllSygAutoriteContractantes(Pageable pageable) {
        log.debug("REST request to get a page of SygAutoriteContractantes");
        Page<SygAutoriteContractante> page = sygAutoriteContractanteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /syg-autorite-contractantes/:id} : get the "id" sygAutoriteContractante.
     *
     * @param id the id of the sygAutoriteContractante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sygAutoriteContractante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/syg-autorite-contractantes/{id}")
    public ResponseEntity<SygAutoriteContractante> getSygAutoriteContractante(@PathVariable Long id) {
        log.debug("REST request to get SygAutoriteContractante : {}", id);
        Optional<SygAutoriteContractante> sygAutoriteContractante = sygAutoriteContractanteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sygAutoriteContractante);
    }

    /**
     * {@code DELETE  /syg-autorite-contractantes/:id} : delete the "id" sygAutoriteContractante.
     *
     * @param id the id of the sygAutoriteContractante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/syg-autorite-contractantes/{id}")
    public ResponseEntity<Void> deleteSygAutoriteContractante(@PathVariable Long id) {
        log.debug("REST request to delete SygAutoriteContractante : {}", id);
        sygAutoriteContractanteRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
