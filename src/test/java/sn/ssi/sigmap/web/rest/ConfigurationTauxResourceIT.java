package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ConfigurationTaux;
import sn.ssi.sigmap.repository.ConfigurationTauxRepository;

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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static sn.ssi.sigmap.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConfigurationTauxResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ConfigurationTauxResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_TAUX = 1D;
    private static final Double UPDATED_TAUX = 2D;

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_INVALID = false;
    private static final Boolean UPDATED_INVALID = true;

    @Autowired
    private ConfigurationTauxRepository configurationTauxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigurationTauxMockMvc;

    private ConfigurationTaux configurationTaux;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationTaux createEntity(EntityManager em) {
        ConfigurationTaux configurationTaux = new ConfigurationTaux()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .taux(DEFAULT_TAUX)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .invalid(DEFAULT_INVALID);
        return configurationTaux;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationTaux createUpdatedEntity(EntityManager em) {
        ConfigurationTaux configurationTaux = new ConfigurationTaux()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .taux(UPDATED_TAUX)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .invalid(UPDATED_INVALID);
        return configurationTaux;
    }

    @BeforeEach
    public void initTest() {
        configurationTaux = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigurationTaux() throws Exception {
        int databaseSizeBeforeCreate = configurationTauxRepository.findAll().size();
        // Create the ConfigurationTaux
        restConfigurationTauxMockMvc.perform(post("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
            .andExpect(status().isCreated());

        // Validate the ConfigurationTaux in the database
        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationTaux testConfigurationTaux = configurationTauxList.get(configurationTauxList.size() - 1);
        assertThat(testConfigurationTaux.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testConfigurationTaux.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testConfigurationTaux.getTaux()).isEqualTo(DEFAULT_TAUX);
        assertThat(testConfigurationTaux.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testConfigurationTaux.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testConfigurationTaux.isInvalid()).isEqualTo(DEFAULT_INVALID);
    }

    @Test
    @Transactional
    public void createConfigurationTauxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configurationTauxRepository.findAll().size();

        // Create the ConfigurationTaux with an existing ID
        configurationTaux.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationTauxMockMvc.perform(post("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationTaux in the database
        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationTauxRepository.findAll().size();
        // set the field null
        configurationTaux.setDateDebut(null);

        // Create the ConfigurationTaux, which fails.


        restConfigurationTauxMockMvc.perform(post("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
            .andExpect(status().isBadRequest());

        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationTauxRepository.findAll().size();
        // set the field null
        configurationTaux.setDateFin(null);

        // Create the ConfigurationTaux, which fails.


        restConfigurationTauxMockMvc.perform(post("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
            .andExpect(status().isBadRequest());

        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvalidIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationTauxRepository.findAll().size();
        // set the field null
        configurationTaux.setInvalid(null);

        // Create the ConfigurationTaux, which fails.


        restConfigurationTauxMockMvc.perform(post("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
            .andExpect(status().isBadRequest());

        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigurationTauxes() throws Exception {
        // Initialize the database
        configurationTauxRepository.saveAndFlush(configurationTaux);

        // Get all the configurationTauxList
        restConfigurationTauxMockMvc.perform(get("/api/configuration-tauxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationTaux.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].invalid").value(hasItem(DEFAULT_INVALID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getConfigurationTaux() throws Exception {
        // Initialize the database
        configurationTauxRepository.saveAndFlush(configurationTaux);

        // Get the configurationTaux
        restConfigurationTauxMockMvc.perform(get("/api/configuration-tauxes/{id}", configurationTaux.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configurationTaux.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX.doubleValue()))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.invalid").value(DEFAULT_INVALID.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingConfigurationTaux() throws Exception {
        // Get the configurationTaux
        restConfigurationTauxMockMvc.perform(get("/api/configuration-tauxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigurationTaux() throws Exception {
        // Initialize the database
        configurationTauxRepository.saveAndFlush(configurationTaux);

        int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();

        // Update the configurationTaux
        ConfigurationTaux updatedConfigurationTaux = configurationTauxRepository.findById(configurationTaux.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationTaux are not directly saved in db
        em.detach(updatedConfigurationTaux);
        updatedConfigurationTaux
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .taux(UPDATED_TAUX)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .invalid(UPDATED_INVALID);

        restConfigurationTauxMockMvc.perform(put("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigurationTaux)))
            .andExpect(status().isOk());

        // Validate the ConfigurationTaux in the database
        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationTaux testConfigurationTaux = configurationTauxList.get(configurationTauxList.size() - 1);
        assertThat(testConfigurationTaux.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testConfigurationTaux.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testConfigurationTaux.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testConfigurationTaux.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testConfigurationTaux.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testConfigurationTaux.isInvalid()).isEqualTo(UPDATED_INVALID);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigurationTaux() throws Exception {
        int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationTauxMockMvc.perform(put("/api/configuration-tauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationTaux in the database
        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigurationTaux() throws Exception {
        // Initialize the database
        configurationTauxRepository.saveAndFlush(configurationTaux);

        int databaseSizeBeforeDelete = configurationTauxRepository.findAll().size();

        // Delete the configurationTaux
        restConfigurationTauxMockMvc.perform(delete("/api/configuration-tauxes/{id}", configurationTaux.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
        assertThat(configurationTauxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
