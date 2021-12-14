package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygAutoriteContractante;
import sn.ssi.sigmap.domain.TypeAutoriteContractante;
import sn.ssi.sigmap.repository.SygAutoriteContractanteRepository;

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
 * Integration tests for the {@link SygAutoriteContractanteResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygAutoriteContractanteResourceIT {

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
    private SygAutoriteContractanteRepository sygAutoriteContractanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygAutoriteContractanteMockMvc;

    private SygAutoriteContractante sygAutoriteContractante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygAutoriteContractante createEntity(EntityManager em) {
        SygAutoriteContractante sygAutoriteContractante = new SygAutoriteContractante()
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
        sygAutoriteContractante.setTypeAutoriteContractante(typeAutoriteContractante);
        return sygAutoriteContractante;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygAutoriteContractante createUpdatedEntity(EntityManager em) {
        SygAutoriteContractante sygAutoriteContractante = new SygAutoriteContractante()
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
        sygAutoriteContractante.setTypeAutoriteContractante(typeAutoriteContractante);
        return sygAutoriteContractante;
    }

    @BeforeEach
    public void initTest() {
        sygAutoriteContractante = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygAutoriteContractante() throws Exception {
        int databaseSizeBeforeCreate = sygAutoriteContractanteRepository.findAll().size();
        // Create the SygAutoriteContractante
        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isCreated());

        // Validate the SygAutoriteContractante in the database
        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeCreate + 1);
        SygAutoriteContractante testSygAutoriteContractante = sygAutoriteContractanteList.get(sygAutoriteContractanteList.size() - 1);
        assertThat(testSygAutoriteContractante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testSygAutoriteContractante.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testSygAutoriteContractante.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testSygAutoriteContractante.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testSygAutoriteContractante.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSygAutoriteContractante.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testSygAutoriteContractante.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSygAutoriteContractante.getSigle()).isEqualTo(DEFAULT_SIGLE);
        assertThat(testSygAutoriteContractante.getUrlsiteweb()).isEqualTo(DEFAULT_URLSITEWEB);
        assertThat(testSygAutoriteContractante.getApprobation()).isEqualTo(DEFAULT_APPROBATION);
        assertThat(testSygAutoriteContractante.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSygAutoriteContractante.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSygAutoriteContractanteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygAutoriteContractanteRepository.findAll().size();

        // Create the SygAutoriteContractante with an existing ID
        sygAutoriteContractante.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        // Validate the SygAutoriteContractante in the database
        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setOrdre(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDenominationIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setDenomination(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setResponsable(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setAdresse(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setTelephone(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setEmail(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSigleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygAutoriteContractanteRepository.findAll().size();
        // set the field null
        sygAutoriteContractante.setSigle(null);

        // Create the SygAutoriteContractante, which fails.


        restSygAutoriteContractanteMockMvc.perform(post("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygAutoriteContractantes() throws Exception {
        // Initialize the database
        sygAutoriteContractanteRepository.saveAndFlush(sygAutoriteContractante);

        // Get all the sygAutoriteContractanteList
        restSygAutoriteContractanteMockMvc.perform(get("/api/syg-autorite-contractantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygAutoriteContractante.getId().intValue())))
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
    public void getSygAutoriteContractante() throws Exception {
        // Initialize the database
        sygAutoriteContractanteRepository.saveAndFlush(sygAutoriteContractante);

        // Get the sygAutoriteContractante
        restSygAutoriteContractanteMockMvc.perform(get("/api/syg-autorite-contractantes/{id}", sygAutoriteContractante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygAutoriteContractante.getId().intValue()))
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
    public void getNonExistingSygAutoriteContractante() throws Exception {
        // Get the sygAutoriteContractante
        restSygAutoriteContractanteMockMvc.perform(get("/api/syg-autorite-contractantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygAutoriteContractante() throws Exception {
        // Initialize the database
        sygAutoriteContractanteRepository.saveAndFlush(sygAutoriteContractante);

        int databaseSizeBeforeUpdate = sygAutoriteContractanteRepository.findAll().size();

        // Update the sygAutoriteContractante
        SygAutoriteContractante updatedSygAutoriteContractante = sygAutoriteContractanteRepository.findById(sygAutoriteContractante.getId()).get();
        // Disconnect from session so that the updates on updatedSygAutoriteContractante are not directly saved in db
        em.detach(updatedSygAutoriteContractante);
        updatedSygAutoriteContractante
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

        restSygAutoriteContractanteMockMvc.perform(put("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSygAutoriteContractante)))
            .andExpect(status().isOk());

        // Validate the SygAutoriteContractante in the database
        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeUpdate);
        SygAutoriteContractante testSygAutoriteContractante = sygAutoriteContractanteList.get(sygAutoriteContractanteList.size() - 1);
        assertThat(testSygAutoriteContractante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testSygAutoriteContractante.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testSygAutoriteContractante.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSygAutoriteContractante.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSygAutoriteContractante.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSygAutoriteContractante.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testSygAutoriteContractante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSygAutoriteContractante.getSigle()).isEqualTo(UPDATED_SIGLE);
        assertThat(testSygAutoriteContractante.getUrlsiteweb()).isEqualTo(UPDATED_URLSITEWEB);
        assertThat(testSygAutoriteContractante.getApprobation()).isEqualTo(UPDATED_APPROBATION);
        assertThat(testSygAutoriteContractante.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSygAutoriteContractante.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSygAutoriteContractante() throws Exception {
        int databaseSizeBeforeUpdate = sygAutoriteContractanteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygAutoriteContractanteMockMvc.perform(put("/api/syg-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygAutoriteContractante)))
            .andExpect(status().isBadRequest());

        // Validate the SygAutoriteContractante in the database
        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygAutoriteContractante() throws Exception {
        // Initialize the database
        sygAutoriteContractanteRepository.saveAndFlush(sygAutoriteContractante);

        int databaseSizeBeforeDelete = sygAutoriteContractanteRepository.findAll().size();

        // Delete the sygAutoriteContractante
        restSygAutoriteContractanteMockMvc.perform(delete("/api/syg-autorite-contractantes/{id}", sygAutoriteContractante.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygAutoriteContractante> sygAutoriteContractanteList = sygAutoriteContractanteRepository.findAll();
        assertThat(sygAutoriteContractanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
