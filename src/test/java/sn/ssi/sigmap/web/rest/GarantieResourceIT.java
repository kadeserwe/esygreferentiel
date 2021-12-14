package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Garantie;
import sn.ssi.sigmap.repository.GarantieRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GarantieResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class GarantieResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_GARANTIE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_GARANTIE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private GarantieRepository garantieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGarantieMockMvc;

    private Garantie garantie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garantie createEntity(EntityManager em) {
        Garantie garantie = new Garantie()
            .libelle(DEFAULT_LIBELLE)
            .typeGarantie(DEFAULT_TYPE_GARANTIE)
            .description(DEFAULT_DESCRIPTION);
        return garantie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garantie createUpdatedEntity(EntityManager em) {
        Garantie garantie = new Garantie()
            .libelle(UPDATED_LIBELLE)
            .typeGarantie(UPDATED_TYPE_GARANTIE)
            .description(UPDATED_DESCRIPTION);
        return garantie;
    }

    @BeforeEach
    public void initTest() {
        garantie = createEntity(em);
    }

    @Test
    @Transactional
    public void createGarantie() throws Exception {
        int databaseSizeBeforeCreate = garantieRepository.findAll().size();
        // Create the Garantie
        restGarantieMockMvc.perform(post("/api/garanties").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(garantie)))
            .andExpect(status().isCreated());

        // Validate the Garantie in the database
        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeCreate + 1);
        Garantie testGarantie = garantieList.get(garantieList.size() - 1);
        assertThat(testGarantie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testGarantie.getTypeGarantie()).isEqualTo(DEFAULT_TYPE_GARANTIE);
        assertThat(testGarantie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createGarantieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = garantieRepository.findAll().size();

        // Create the Garantie with an existing ID
        garantie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGarantieMockMvc.perform(post("/api/garanties").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(garantie)))
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = garantieRepository.findAll().size();
        // set the field null
        garantie.setLibelle(null);

        // Create the Garantie, which fails.


        restGarantieMockMvc.perform(post("/api/garanties").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(garantie)))
            .andExpect(status().isBadRequest());

        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeGarantieIsRequired() throws Exception {
        int databaseSizeBeforeTest = garantieRepository.findAll().size();
        // set the field null
        garantie.setTypeGarantie(null);

        // Create the Garantie, which fails.


        restGarantieMockMvc.perform(post("/api/garanties").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(garantie)))
            .andExpect(status().isBadRequest());

        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGaranties() throws Exception {
        // Initialize the database
        garantieRepository.saveAndFlush(garantie);

        // Get all the garantieList
        restGarantieMockMvc.perform(get("/api/garanties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(garantie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].typeGarantie").value(hasItem(DEFAULT_TYPE_GARANTIE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getGarantie() throws Exception {
        // Initialize the database
        garantieRepository.saveAndFlush(garantie);

        // Get the garantie
        restGarantieMockMvc.perform(get("/api/garanties/{id}", garantie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(garantie.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.typeGarantie").value(DEFAULT_TYPE_GARANTIE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingGarantie() throws Exception {
        // Get the garantie
        restGarantieMockMvc.perform(get("/api/garanties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGarantie() throws Exception {
        // Initialize the database
        garantieRepository.saveAndFlush(garantie);

        int databaseSizeBeforeUpdate = garantieRepository.findAll().size();

        // Update the garantie
        Garantie updatedGarantie = garantieRepository.findById(garantie.getId()).get();
        // Disconnect from session so that the updates on updatedGarantie are not directly saved in db
        em.detach(updatedGarantie);
        updatedGarantie
            .libelle(UPDATED_LIBELLE)
            .typeGarantie(UPDATED_TYPE_GARANTIE)
            .description(UPDATED_DESCRIPTION);

        restGarantieMockMvc.perform(put("/api/garanties").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGarantie)))
            .andExpect(status().isOk());

        // Validate the Garantie in the database
        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeUpdate);
        Garantie testGarantie = garantieList.get(garantieList.size() - 1);
        assertThat(testGarantie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testGarantie.getTypeGarantie()).isEqualTo(UPDATED_TYPE_GARANTIE);
        assertThat(testGarantie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingGarantie() throws Exception {
        int databaseSizeBeforeUpdate = garantieRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGarantieMockMvc.perform(put("/api/garanties").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(garantie)))
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGarantie() throws Exception {
        // Initialize the database
        garantieRepository.saveAndFlush(garantie);

        int databaseSizeBeforeDelete = garantieRepository.findAll().size();

        // Delete the garantie
        restGarantieMockMvc.perform(delete("/api/garanties/{id}", garantie.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Garantie> garantieList = garantieRepository.findAll();
        assertThat(garantieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
