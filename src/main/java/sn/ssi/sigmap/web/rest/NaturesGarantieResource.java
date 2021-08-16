package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.NaturesGarantie;
import sn.ssi.sigmap.repository.NaturesGarantieRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.NaturesGarantie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NaturesGarantieResource {

    private final Logger log = LoggerFactory.getLogger(NaturesGarantieResource.class);

    private static final String ENTITY_NAME = "referentielmsNaturesGarantie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NaturesGarantieRepository naturesGarantieRepository;

    public NaturesGarantieResource(NaturesGarantieRepository naturesGarantieRepository) {
        this.naturesGarantieRepository = naturesGarantieRepository;
    }

    /**
     * {@code POST  /natures-garanties} : Create a new naturesGarantie.
     *
     * @param naturesGarantie the naturesGarantie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new naturesGarantie, or with status {@code 400 (Bad Request)} if the naturesGarantie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/natures-garanties")
    public ResponseEntity<NaturesGarantie> createNaturesGarantie(@Valid @RequestBody NaturesGarantie naturesGarantie) throws URISyntaxException {
        log.debug("REST request to save NaturesGarantie : {}", naturesGarantie);
        if (naturesGarantie.getId() != null) {
            throw new BadRequestAlertException("A new naturesGarantie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NaturesGarantie result = naturesGarantieRepository.save(naturesGarantie);
        return ResponseEntity.created(new URI("/api/natures-garanties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /natures-garanties} : Updates an existing naturesGarantie.
     *
     * @param naturesGarantie the naturesGarantie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated naturesGarantie,
     * or with status {@code 400 (Bad Request)} if the naturesGarantie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the naturesGarantie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/natures-garanties")
    public ResponseEntity<NaturesGarantie> updateNaturesGarantie(@Valid @RequestBody NaturesGarantie naturesGarantie) throws URISyntaxException {
        log.debug("REST request to update NaturesGarantie : {}", naturesGarantie);
        if (naturesGarantie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NaturesGarantie result = naturesGarantieRepository.save(naturesGarantie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, naturesGarantie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /natures-garanties} : get all the naturesGaranties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of naturesGaranties in body.
     */
    @GetMapping("/natures-garanties")
    public ResponseEntity<List<NaturesGarantie>> getAllNaturesGaranties(Pageable pageable) {
        log.debug("REST request to get a page of NaturesGaranties");
        Page<NaturesGarantie> page = naturesGarantieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /natures-garanties/:id} : get the "id" naturesGarantie.
     *
     * @param id the id of the naturesGarantie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the naturesGarantie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/natures-garanties/{id}")
    public ResponseEntity<NaturesGarantie> getNaturesGarantie(@PathVariable Long id) {
        log.debug("REST request to get NaturesGarantie : {}", id);
        Optional<NaturesGarantie> naturesGarantie = naturesGarantieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(naturesGarantie);
    }

    /**
     * {@code DELETE  /natures-garanties/:id} : delete the "id" naturesGarantie.
     *
     * @param id the id of the naturesGarantie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/natures-garanties/{id}")
    public ResponseEntity<Void> deleteNaturesGarantie(@PathVariable Long id) {
        log.debug("REST request to delete NaturesGarantie : {}", id);
        naturesGarantieRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
