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

import sn.ssi.sigmap.domain.JoursFeries;
import sn.ssi.sigmap.repository.JoursFeriesRepository;

/**
 * Integration tests for the {@link JoursFeriesResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class JoursFeriesResourceIT {

  private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/jours-feries";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private JoursFeriesRepository joursFeriesRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restJoursFeriesMockMvc;

  private JoursFeries joursFeries;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static JoursFeries createEntity(EntityManager em) {
    JoursFeries joursFeries = new JoursFeries().date(DEFAULT_DATE).description(DEFAULT_DESCRIPTION);
    return joursFeries;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static JoursFeries createUpdatedEntity(EntityManager em) {
    JoursFeries joursFeries = new JoursFeries().date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
    return joursFeries;
  }

  @BeforeEach
  public void initTest() {
    joursFeries = createEntity(em);
  }

  @Test
  @Transactional
  void createJoursFeries() throws Exception {
    int databaseSizeBeforeCreate = joursFeriesRepository.findAll().size();
    // Create the JoursFeries
    restJoursFeriesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joursFeries)))
      .andExpect(status().isCreated());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeCreate + 1);
    JoursFeries testJoursFeries = joursFeriesList.get(joursFeriesList.size() - 1);
    assertThat(testJoursFeries.getDate()).isEqualTo(DEFAULT_DATE);
    assertThat(testJoursFeries.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  void createJoursFeriesWithExistingId() throws Exception {
    // Create the JoursFeries with an existing ID
    joursFeries.setId(1L);

    int databaseSizeBeforeCreate = joursFeriesRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restJoursFeriesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joursFeries)))
      .andExpect(status().isBadRequest());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkDateIsRequired() throws Exception {
    int databaseSizeBeforeTest = joursFeriesRepository.findAll().size();
    // set the field null
    joursFeries.setDate(null);

    // Create the JoursFeries, which fails.

    restJoursFeriesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joursFeries)))
      .andExpect(status().isBadRequest());

    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllJoursFeries() throws Exception {
    // Initialize the database
    joursFeriesRepository.saveAndFlush(joursFeries);

    // Get all the joursFeriesList
    restJoursFeriesMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(joursFeries.getId().intValue())))
      .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
  }

  @Test
  @Transactional
  void getJoursFeries() throws Exception {
    // Initialize the database
    joursFeriesRepository.saveAndFlush(joursFeries);

    // Get the joursFeries
    restJoursFeriesMockMvc
      .perform(get(ENTITY_API_URL_ID, joursFeries.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(joursFeries.getId().intValue()))
      .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
  }

  @Test
  @Transactional
  void getNonExistingJoursFeries() throws Exception {
    // Get the joursFeries
    restJoursFeriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewJoursFeries() throws Exception {
    // Initialize the database
    joursFeriesRepository.saveAndFlush(joursFeries);

    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();

    // Update the joursFeries
    JoursFeries updatedJoursFeries = joursFeriesRepository.findById(joursFeries.getId()).get();
    // Disconnect from session so that the updates on updatedJoursFeries are not directly saved in db
    em.detach(updatedJoursFeries);
    updatedJoursFeries.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

    restJoursFeriesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedJoursFeries.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedJoursFeries))
      )
      .andExpect(status().isOk());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
    JoursFeries testJoursFeries = joursFeriesList.get(joursFeriesList.size() - 1);
    assertThat(testJoursFeries.getDate()).isEqualTo(UPDATED_DATE);
    assertThat(testJoursFeries.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void putNonExistingJoursFeries() throws Exception {
    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();
    joursFeries.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restJoursFeriesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, joursFeries.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(joursFeries))
      )
      .andExpect(status().isBadRequest());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchJoursFeries() throws Exception {
    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();
    joursFeries.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restJoursFeriesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(joursFeries))
      )
      .andExpect(status().isBadRequest());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamJoursFeries() throws Exception {
    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();
    joursFeries.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restJoursFeriesMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joursFeries)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateJoursFeriesWithPatch() throws Exception {
    // Initialize the database
    joursFeriesRepository.saveAndFlush(joursFeries);

    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();

    // Update the joursFeries using partial update
    JoursFeries partialUpdatedJoursFeries = new JoursFeries();
    partialUpdatedJoursFeries.setId(joursFeries.getId());

    partialUpdatedJoursFeries.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

    restJoursFeriesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedJoursFeries.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJoursFeries))
      )
      .andExpect(status().isOk());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
    JoursFeries testJoursFeries = joursFeriesList.get(joursFeriesList.size() - 1);
    assertThat(testJoursFeries.getDate()).isEqualTo(UPDATED_DATE);
    assertThat(testJoursFeries.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void fullUpdateJoursFeriesWithPatch() throws Exception {
    // Initialize the database
    joursFeriesRepository.saveAndFlush(joursFeries);

    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();

    // Update the joursFeries using partial update
    JoursFeries partialUpdatedJoursFeries = new JoursFeries();
    partialUpdatedJoursFeries.setId(joursFeries.getId());

    partialUpdatedJoursFeries.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

    restJoursFeriesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedJoursFeries.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJoursFeries))
      )
      .andExpect(status().isOk());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
    JoursFeries testJoursFeries = joursFeriesList.get(joursFeriesList.size() - 1);
    assertThat(testJoursFeries.getDate()).isEqualTo(UPDATED_DATE);
    assertThat(testJoursFeries.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void patchNonExistingJoursFeries() throws Exception {
    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();
    joursFeries.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restJoursFeriesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, joursFeries.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(joursFeries))
      )
      .andExpect(status().isBadRequest());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchJoursFeries() throws Exception {
    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();
    joursFeries.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restJoursFeriesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(joursFeries))
      )
      .andExpect(status().isBadRequest());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamJoursFeries() throws Exception {
    int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();
    joursFeries.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restJoursFeriesMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(joursFeries)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the JoursFeries in the database
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteJoursFeries() throws Exception {
    // Initialize the database
    joursFeriesRepository.saveAndFlush(joursFeries);

    int databaseSizeBeforeDelete = joursFeriesRepository.findAll().size();

    // Delete the joursFeries
    restJoursFeriesMockMvc
      .perform(delete(ENTITY_API_URL_ID, joursFeries.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
    assertThat(joursFeriesList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
