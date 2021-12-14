package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.AutoriteContractante;
import sn.ssi.sigmap.repository.AutoriteContractanteRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.AutoriteContractante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AutoriteContractanteResource {

    private final Logger log = LoggerFactory.getLogger(AutoriteContractanteResource.class);

    private final AutoriteContractanteRepository autoriteContractanteRepository;

    public AutoriteContractanteResource(AutoriteContractanteRepository autoriteContractanteRepository) {
        this.autoriteContractanteRepository = autoriteContractanteRepository;
    }

    /**
     * {@code GET  /autorite-contractantes} : get all the autoriteContractantes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoriteContractantes in body.
     */
    @GetMapping("/autorite-contractantes")
    public ResponseEntity<List<AutoriteContractante>> getAllAutoriteContractantes(Pageable pageable) {
        log.debug("REST request to get a page of AutoriteContractantes");
        Page<AutoriteContractante> page = autoriteContractanteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autorite-contractantes/:id} : get the "id" autoriteContractante.
     *
     * @param id the id of the autoriteContractante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoriteContractante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/autorite-contractantes/{id}")
    public ResponseEntity<AutoriteContractante> getAutoriteContractante(@PathVariable Long id) {
        log.debug("REST request to get AutoriteContractante : {}", id);
        Optional<AutoriteContractante> autoriteContractante = autoriteContractanteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(autoriteContractante);
    }
}
