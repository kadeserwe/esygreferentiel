package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.AutoriteContractante;
import sn.ssi.sigmap.domain.TypeAutoriteContractante;
import sn.ssi.sigmap.repository.AutoriteContractanteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AutoriteContractanteResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AutoriteContractanteResourceIT {

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE = "BBBBBBBBBB";

    private static final String DEFAULT_URLSITEWEB = "AAAAAAAAAA";
    private static final String UPDATED_URLSITEWEB = "BBBBBBBBBB";

    private static final String DEFAULT_APPROBATION = "AAAAAAAAAA";
    private static final String UPDATED_APPROBATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    @Autowired
    private AutoriteContractanteRepository autoriteContractanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutoriteContractanteMockMvc;

    private AutoriteContractante autoriteContractante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoriteContractante createEntity(EntityManager em) {
        AutoriteContractante autoriteContractante = new AutoriteContractante()
            .ordre(DEFAULT_ORDRE)
            .denomination(DEFAULT_DENOMINATION)
            .responsable(DEFAULT_RESPONSABLE)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .fax(DEFAULT_FAX)
            .email(DEFAULT_EMAIL)
            .sigle(DEFAULT_SIGLE)
            .urlsiteweb(DEFAULT_URLSITEWEB)
            .approbation(DEFAULT_APPROBATION)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        // Add required entity
        TypeAutoriteContractante typeAutoriteContractante;
        if (TestUtil.findAll(em, TypeAutoriteContractante.class).isEmpty()) {
            typeAutoriteContractante = TypeAutoriteContractanteResourceIT.createEntity(em);
            em.persist(typeAutoriteContractante);
            em.flush();
        } else {
            typeAutoriteContractante = TestUtil.findAll(em, TypeAutoriteContractante.class).get(0);
        }
        autoriteContractante.setType(typeAutoriteContractante);
        return autoriteContractante;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoriteContractante createUpdatedEntity(EntityManager em) {
        AutoriteContractante autoriteContractante = new AutoriteContractante()
            .ordre(UPDATED_ORDRE)
            .denomination(UPDATED_DENOMINATION)
            .responsable(UPDATED_RESPONSABLE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .sigle(UPDATED_SIGLE)
            .urlsiteweb(UPDATED_URLSITEWEB)
            .approbation(UPDATED_APPROBATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        // Add required entity
        TypeAutoriteContractante typeAutoriteContractante;
        if (TestUtil.findAll(em, TypeAutoriteContractante.class).isEmpty()) {
            typeAutoriteContractante = TypeAutoriteContractanteResourceIT.createUpdatedEntity(em);
            em.persist(typeAutoriteContractante);
            em.flush();
        } else {
            typeAutoriteContractante = TestUtil.findAll(em, TypeAutoriteContractante.class).get(0);
        }
        autoriteContractante.setType(typeAutoriteContractante);
        return autoriteContractante;
    }

    @BeforeEach
    public void initTest() {
        autoriteContractante = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllAutoriteContractantes() throws Exception {
        // Initialize the database
        autoriteContractanteRepository.saveAndFlush(autoriteContractante);

        // Get all the autoriteContractanteList
        restAutoriteContractanteMockMvc.perform(get("/api/autorite-contractantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoriteContractante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)))
            .andExpect(jsonPath("$.[*].urlsiteweb").value(hasItem(DEFAULT_URLSITEWEB)))
            .andExpect(jsonPath("$.[*].approbation").value(hasItem(DEFAULT_APPROBATION)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }
    
    @Test
    @Transactional
    public void getAutoriteContractante() throws Exception {
        // Initialize the database
        autoriteContractanteRepository.saveAndFlush(autoriteContractante);

        // Get the autoriteContractante
        restAutoriteContractanteMockMvc.perform(get("/api/autorite-contractantes/{id}", autoriteContractante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autoriteContractante.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE))
            .andExpect(jsonPath("$.urlsiteweb").value(DEFAULT_URLSITEWEB))
            .andExpect(jsonPath("$.approbation").value(DEFAULT_APPROBATION))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }
    @Test
    @Transactional
    public void getNonExistingAutoriteContractante() throws Exception {
        // Get the autoriteContractante
        restAutoriteContractanteMockMvc.perform(get("/api/autorite-contractantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
