package sn.ssi.sigmap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import sn.ssi.sigmap.domain.ConfigurationTaux;
import sn.ssi.sigmap.repository.ConfigurationTauxRepository;

/**
 * Integration tests for the {@link ConfigurationTauxResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class ConfigurationTauxResourceIT {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final Double DEFAULT_TAUX = 1D;
  private static final Double UPDATED_TAUX = 2D;

  private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final Boolean DEFAULT_INVALID = false;
  private static final Boolean UPDATED_INVALID = true;

  private static final String ENTITY_API_URL = "/api/configuration-tauxes";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
  void createConfigurationTaux() throws Exception {
    int databaseSizeBeforeCreate = configurationTauxRepository.findAll().size();
    // Create the ConfigurationTaux
    restConfigurationTauxMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
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
    assertThat(testConfigurationTaux.getInvalid()).isEqualTo(DEFAULT_INVALID);
  }

  @Test
  @Transactional
  void createConfigurationTauxWithExistingId() throws Exception {
    // Create the ConfigurationTaux with an existing ID
    configurationTaux.setId(1L);

    int databaseSizeBeforeCreate = configurationTauxRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restConfigurationTauxMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
      .andExpect(status().isBadRequest());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = configurationTauxRepository.findAll().size();
    // set the field null
    configurationTaux.setCode(null);

    // Create the ConfigurationTaux, which fails.

    restConfigurationTauxMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
      .andExpect(status().isBadRequest());

    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = configurationTauxRepository.findAll().size();
    // set the field null
    configurationTaux.setLibelle(null);

    // Create the ConfigurationTaux, which fails.

    restConfigurationTauxMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
      .andExpect(status().isBadRequest());

    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkTauxIsRequired() throws Exception {
    int databaseSizeBeforeTest = configurationTauxRepository.findAll().size();
    // set the field null
    configurationTaux.setTaux(null);

    // Create the ConfigurationTaux, which fails.

    restConfigurationTauxMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
      .andExpect(status().isBadRequest());

    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllConfigurationTauxes() throws Exception {
    // Initialize the database
    configurationTauxRepository.saveAndFlush(configurationTaux);

    // Get all the configurationTauxList
    restConfigurationTauxMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(configurationTaux.getId().intValue())))
      .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.doubleValue())))
      .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
      .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
      .andExpect(jsonPath("$.[*].invalid").value(hasItem(DEFAULT_INVALID.booleanValue())));
  }

  @Test
  @Transactional
  void getConfigurationTaux() throws Exception {
    // Initialize the database
    configurationTauxRepository.saveAndFlush(configurationTaux);

    // Get the configurationTaux
    restConfigurationTauxMockMvc
      .perform(get(ENTITY_API_URL_ID, configurationTaux.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(configurationTaux.getId().intValue()))
      .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX.doubleValue()))
      .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
      .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
      .andExpect(jsonPath("$.invalid").value(DEFAULT_INVALID.booleanValue()));
  }

  @Test
  @Transactional
  void getNonExistingConfigurationTaux() throws Exception {
    // Get the configurationTaux
    restConfigurationTauxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewConfigurationTaux() throws Exception {
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

    restConfigurationTauxMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedConfigurationTaux.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedConfigurationTaux))
      )
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
    assertThat(testConfigurationTaux.getInvalid()).isEqualTo(UPDATED_INVALID);
  }

  @Test
  @Transactional
  void putNonExistingConfigurationTaux() throws Exception {
    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();
    configurationTaux.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restConfigurationTauxMockMvc
      .perform(
        put(ENTITY_API_URL_ID, configurationTaux.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(configurationTaux))
      )
      .andExpect(status().isBadRequest());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchConfigurationTaux() throws Exception {
    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();
    configurationTaux.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restConfigurationTauxMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(configurationTaux))
      )
      .andExpect(status().isBadRequest());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamConfigurationTaux() throws Exception {
    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();
    configurationTaux.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restConfigurationTauxMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationTaux)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateConfigurationTauxWithPatch() throws Exception {
    // Initialize the database
    configurationTauxRepository.saveAndFlush(configurationTaux);

    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();

    // Update the configurationTaux using partial update
    ConfigurationTaux partialUpdatedConfigurationTaux = new ConfigurationTaux();
    partialUpdatedConfigurationTaux.setId(configurationTaux.getId());

    partialUpdatedConfigurationTaux.code(UPDATED_CODE);

    restConfigurationTauxMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedConfigurationTaux.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationTaux))
      )
      .andExpect(status().isOk());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
    ConfigurationTaux testConfigurationTaux = configurationTauxList.get(configurationTauxList.size() - 1);
    assertThat(testConfigurationTaux.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testConfigurationTaux.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testConfigurationTaux.getTaux()).isEqualTo(DEFAULT_TAUX);
    assertThat(testConfigurationTaux.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
    assertThat(testConfigurationTaux.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    assertThat(testConfigurationTaux.getInvalid()).isEqualTo(DEFAULT_INVALID);
  }

  @Test
  @Transactional
  void fullUpdateConfigurationTauxWithPatch() throws Exception {
    // Initialize the database
    configurationTauxRepository.saveAndFlush(configurationTaux);

    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();

    // Update the configurationTaux using partial update
    ConfigurationTaux partialUpdatedConfigurationTaux = new ConfigurationTaux();
    partialUpdatedConfigurationTaux.setId(configurationTaux.getId());

    partialUpdatedConfigurationTaux
      .code(UPDATED_CODE)
      .libelle(UPDATED_LIBELLE)
      .taux(UPDATED_TAUX)
      .dateDebut(UPDATED_DATE_DEBUT)
      .dateFin(UPDATED_DATE_FIN)
      .invalid(UPDATED_INVALID);

    restConfigurationTauxMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedConfigurationTaux.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationTaux))
      )
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
    assertThat(testConfigurationTaux.getInvalid()).isEqualTo(UPDATED_INVALID);
  }

  @Test
  @Transactional
  void patchNonExistingConfigurationTaux() throws Exception {
    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();
    configurationTaux.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restConfigurationTauxMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, configurationTaux.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(configurationTaux))
      )
      .andExpect(status().isBadRequest());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchConfigurationTaux() throws Exception {
    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();
    configurationTaux.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restConfigurationTauxMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(configurationTaux))
      )
      .andExpect(status().isBadRequest());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamConfigurationTaux() throws Exception {
    int databaseSizeBeforeUpdate = configurationTauxRepository.findAll().size();
    configurationTaux.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restConfigurationTauxMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(configurationTaux))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the ConfigurationTaux in the database
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteConfigurationTaux() throws Exception {
    // Initialize the database
    configurationTauxRepository.saveAndFlush(configurationTaux);

    int databaseSizeBeforeDelete = configurationTauxRepository.findAll().size();

    // Delete the configurationTaux
    restConfigurationTauxMockMvc
      .perform(delete(ENTITY_API_URL_ID, configurationTaux.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ConfigurationTaux> configurationTauxList = configurationTauxRepository.findAll();
    assertThat(configurationTauxList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
