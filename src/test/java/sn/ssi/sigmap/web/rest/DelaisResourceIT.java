package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Delais;
import sn.ssi.sigmap.repository.DelaisRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DelaisResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class DelaisResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_UNITE = "AAAAAAAAAA";
    private static final String UPDATED_UNITE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALEUR = 1;
    private static final Integer UPDATED_VALEUR = 2;

    private static final Instant DEFAULT_DEBUT_VALIDITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEBUT_VALIDITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FIN_VALIDITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIN_VALIDITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTAIRES = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRES = "BBBBBBBBBB";

    @Autowired
    private DelaisRepository delaisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDelaisMockMvc;

    private Delais delais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delais createEntity(EntityManager em) {
        Delais delais = new Delais()
            .libelle(DEFAULT_LIBELLE)
            .code(DEFAULT_CODE)
            .unite(DEFAULT_UNITE)
            .valeur(DEFAULT_VALEUR)
            .debutValidite(DEFAULT_DEBUT_VALIDITE)
            .finValidite(DEFAULT_FIN_VALIDITE)
            .commentaires(DEFAULT_COMMENTAIRES);
        return delais;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delais createUpdatedEntity(EntityManager em) {
        Delais delais = new Delais()
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE)
            .unite(UPDATED_UNITE)
            .valeur(UPDATED_VALEUR)
            .debutValidite(UPDATED_DEBUT_VALIDITE)
            .finValidite(UPDATED_FIN_VALIDITE)
            .commentaires(UPDATED_COMMENTAIRES);
        return delais;
    }

    @BeforeEach
    public void initTest() {
        delais = createEntity(em);
    }

    @Test
    @Transactional
    public void createDelais() throws Exception {
        int databaseSizeBeforeCreate = delaisRepository.findAll().size();
        // Create the Delais
        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isCreated());

        // Validate the Delais in the database
        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeCreate + 1);
        Delais testDelais = delaisList.get(delaisList.size() - 1);
        assertThat(testDelais.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDelais.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDelais.getUnite()).isEqualTo(DEFAULT_UNITE);
        assertThat(testDelais.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testDelais.getDebutValidite()).isEqualTo(DEFAULT_DEBUT_VALIDITE);
        assertThat(testDelais.getFinValidite()).isEqualTo(DEFAULT_FIN_VALIDITE);
        assertThat(testDelais.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);
    }

    @Test
    @Transactional
    public void createDelaisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = delaisRepository.findAll().size();

        // Create the Delais with an existing ID
        delais.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        // Validate the Delais in the database
        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = delaisRepository.findAll().size();
        // set the field null
        delais.setLibelle(null);

        // Create the Delais, which fails.


        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = delaisRepository.findAll().size();
        // set the field null
        delais.setCode(null);

        // Create the Delais, which fails.


        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUniteIsRequired() throws Exception {
        int databaseSizeBeforeTest = delaisRepository.findAll().size();
        // set the field null
        delais.setUnite(null);

        // Create the Delais, which fails.


        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = delaisRepository.findAll().size();
        // set the field null
        delais.setValeur(null);

        // Create the Delais, which fails.


        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDebutValiditeIsRequired() throws Exception {
        int databaseSizeBeforeTest = delaisRepository.findAll().size();
        // set the field null
        delais.setDebutValidite(null);

        // Create the Delais, which fails.


        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinValiditeIsRequired() throws Exception {
        int databaseSizeBeforeTest = delaisRepository.findAll().size();
        // set the field null
        delais.setFinValidite(null);

        // Create the Delais, which fails.


        restDelaisMockMvc.perform(post("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDelais() throws Exception {
        // Initialize the database
        delaisRepository.saveAndFlush(delais);

        // Get all the delaisList
        restDelaisMockMvc.perform(get("/api/delais?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delais.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].debutValidite").value(hasItem(DEFAULT_DEBUT_VALIDITE.toString())))
            .andExpect(jsonPath("$.[*].finValidite").value(hasItem(DEFAULT_FIN_VALIDITE.toString())))
            .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
    }
    
    @Test
    @Transactional
    public void getDelais() throws Exception {
        // Initialize the database
        delaisRepository.saveAndFlush(delais);

        // Get the delais
        restDelaisMockMvc.perform(get("/api/delais/{id}", delais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(delais.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.unite").value(DEFAULT_UNITE))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.debutValidite").value(DEFAULT_DEBUT_VALIDITE.toString()))
            .andExpect(jsonPath("$.finValidite").value(DEFAULT_FIN_VALIDITE.toString()))
            .andExpect(jsonPath("$.commentaires").value(DEFAULT_COMMENTAIRES));
    }
    @Test
    @Transactional
    public void getNonExistingDelais() throws Exception {
        // Get the delais
        restDelaisMockMvc.perform(get("/api/delais/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelais() throws Exception {
        // Initialize the database
        delaisRepository.saveAndFlush(delais);

        int databaseSizeBeforeUpdate = delaisRepository.findAll().size();

        // Update the delais
        Delais updatedDelais = delaisRepository.findById(delais.getId()).get();
        // Disconnect from session so that the updates on updatedDelais are not directly saved in db
        em.detach(updatedDelais);
        updatedDelais
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE)
            .unite(UPDATED_UNITE)
            .valeur(UPDATED_VALEUR)
            .debutValidite(UPDATED_DEBUT_VALIDITE)
            .finValidite(UPDATED_FIN_VALIDITE)
            .commentaires(UPDATED_COMMENTAIRES);

        restDelaisMockMvc.perform(put("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDelais)))
            .andExpect(status().isOk());

        // Validate the Delais in the database
        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeUpdate);
        Delais testDelais = delaisList.get(delaisList.size() - 1);
        assertThat(testDelais.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDelais.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDelais.getUnite()).isEqualTo(UPDATED_UNITE);
        assertThat(testDelais.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testDelais.getDebutValidite()).isEqualTo(UPDATED_DEBUT_VALIDITE);
        assertThat(testDelais.getFinValidite()).isEqualTo(UPDATED_FIN_VALIDITE);
        assertThat(testDelais.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    public void updateNonExistingDelais() throws Exception {
        int databaseSizeBeforeUpdate = delaisRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelaisMockMvc.perform(put("/api/delais").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delais)))
            .andExpect(status().isBadRequest());

        // Validate the Delais in the database
        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDelais() throws Exception {
        // Initialize the database
        delaisRepository.saveAndFlush(delais);

        int databaseSizeBeforeDelete = delaisRepository.findAll().size();

        // Delete the delais
        restDelaisMockMvc.perform(delete("/api/delais/{id}", delais.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Delais> delaisList = delaisRepository.findAll();
        assertThat(delaisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
