package sn.ssi.sigmap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import sn.ssi.sigmap.domain.SourcesFinancement;
import sn.ssi.sigmap.domain.enumeration.enumSourceFin;
import sn.ssi.sigmap.repository.SourcesFinancementRepository;

/**
 * Integration tests for the {@link SourcesFinancementResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class SourcesFinancementResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final enumSourceFin DEFAULT_TYPE = enumSourceFin.RESSOURCESEXTERNES;
  private static final enumSourceFin UPDATED_TYPE = enumSourceFin.RESSOURCESINTERNESBUDGETDEETAT;

  private static final String ENTITY_API_URL = "/api/sources-financements";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private SourcesFinancementRepository sourcesFinancementRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restSourcesFinancementMockMvc;

  private SourcesFinancement sourcesFinancement;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SourcesFinancement createEntity(EntityManager em) {
    SourcesFinancement sourcesFinancement = new SourcesFinancement().libelle(DEFAULT_LIBELLE).type(DEFAULT_TYPE);
    return sourcesFinancement;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SourcesFinancement createUpdatedEntity(EntityManager em) {
    SourcesFinancement sourcesFinancement = new SourcesFinancement().libelle(UPDATED_LIBELLE).type(UPDATED_TYPE);
    return sourcesFinancement;
  }

  @BeforeEach
  public void initTest() {
    sourcesFinancement = createEntity(em);
  }

  @Test
  @Transactional
  void createSourcesFinancement() throws Exception {
    int databaseSizeBeforeCreate = sourcesFinancementRepository.findAll().size();
    // Create the SourcesFinancement
    restSourcesFinancementMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
      .andExpect(status().isCreated());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeCreate + 1);
    SourcesFinancement testSourcesFinancement = sourcesFinancementList.get(sourcesFinancementList.size() - 1);
    assertThat(testSourcesFinancement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testSourcesFinancement.getType()).isEqualTo(DEFAULT_TYPE);
  }

  @Test
  @Transactional
  void createSourcesFinancementWithExistingId() throws Exception {
    // Create the SourcesFinancement with an existing ID
    sourcesFinancement.setId(1L);

    int databaseSizeBeforeCreate = sourcesFinancementRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restSourcesFinancementMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
      .andExpect(status().isBadRequest());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = sourcesFinancementRepository.findAll().size();
    // set the field null
    sourcesFinancement.setLibelle(null);

    // Create the SourcesFinancement, which fails.

    restSourcesFinancementMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
      .andExpect(status().isBadRequest());

    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkTypeIsRequired() throws Exception {
    int databaseSizeBeforeTest = sourcesFinancementRepository.findAll().size();
    // set the field null
    sourcesFinancement.setType(null);

    // Create the SourcesFinancement, which fails.

    restSourcesFinancementMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
      .andExpect(status().isBadRequest());

    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllSourcesFinancements() throws Exception {
    // Initialize the database
    sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

    // Get all the sourcesFinancementList
    restSourcesFinancementMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(sourcesFinancement.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
  }

  @Test
  @Transactional
  void getSourcesFinancement() throws Exception {
    // Initialize the database
    sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

    // Get the sourcesFinancement
    restSourcesFinancementMockMvc
      .perform(get(ENTITY_API_URL_ID, sourcesFinancement.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(sourcesFinancement.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
  }

  @Test
  @Transactional
  void getNonExistingSourcesFinancement() throws Exception {
    // Get the sourcesFinancement
    restSourcesFinancementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewSourcesFinancement() throws Exception {
    // Initialize the database
    sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();

    // Update the sourcesFinancement
    SourcesFinancement updatedSourcesFinancement = sourcesFinancementRepository.findById(sourcesFinancement.getId()).get();
    // Disconnect from session so that the updates on updatedSourcesFinancement are not directly saved in db
    em.detach(updatedSourcesFinancement);
    updatedSourcesFinancement.libelle(UPDATED_LIBELLE).type(UPDATED_TYPE);

    restSourcesFinancementMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedSourcesFinancement.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedSourcesFinancement))
      )
      .andExpect(status().isOk());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
    SourcesFinancement testSourcesFinancement = sourcesFinancementList.get(sourcesFinancementList.size() - 1);
    assertThat(testSourcesFinancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testSourcesFinancement.getType()).isEqualTo(UPDATED_TYPE);
  }

  @Test
  @Transactional
  void putNonExistingSourcesFinancement() throws Exception {
    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();
    sourcesFinancement.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSourcesFinancementMockMvc
      .perform(
        put(ENTITY_API_URL_ID, sourcesFinancement.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement))
      )
      .andExpect(status().isBadRequest());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchSourcesFinancement() throws Exception {
    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();
    sourcesFinancement.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSourcesFinancementMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement))
      )
      .andExpect(status().isBadRequest());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamSourcesFinancement() throws Exception {
    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();
    sourcesFinancement.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSourcesFinancementMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateSourcesFinancementWithPatch() throws Exception {
    // Initialize the database
    sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();

    // Update the sourcesFinancement using partial update
    SourcesFinancement partialUpdatedSourcesFinancement = new SourcesFinancement();
    partialUpdatedSourcesFinancement.setId(sourcesFinancement.getId());

    partialUpdatedSourcesFinancement.libelle(UPDATED_LIBELLE);

    restSourcesFinancementMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSourcesFinancement.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourcesFinancement))
      )
      .andExpect(status().isOk());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
    SourcesFinancement testSourcesFinancement = sourcesFinancementList.get(sourcesFinancementList.size() - 1);
    assertThat(testSourcesFinancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testSourcesFinancement.getType()).isEqualTo(DEFAULT_TYPE);
  }

  @Test
  @Transactional
  void fullUpdateSourcesFinancementWithPatch() throws Exception {
    // Initialize the database
    sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();

    // Update the sourcesFinancement using partial update
    SourcesFinancement partialUpdatedSourcesFinancement = new SourcesFinancement();
    partialUpdatedSourcesFinancement.setId(sourcesFinancement.getId());

    partialUpdatedSourcesFinancement.libelle(UPDATED_LIBELLE).type(UPDATED_TYPE);

    restSourcesFinancementMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSourcesFinancement.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourcesFinancement))
      )
      .andExpect(status().isOk());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
    SourcesFinancement testSourcesFinancement = sourcesFinancementList.get(sourcesFinancementList.size() - 1);
    assertThat(testSourcesFinancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testSourcesFinancement.getType()).isEqualTo(UPDATED_TYPE);
  }

  @Test
  @Transactional
  void patchNonExistingSourcesFinancement() throws Exception {
    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();
    sourcesFinancement.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSourcesFinancementMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, sourcesFinancement.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement))
      )
      .andExpect(status().isBadRequest());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchSourcesFinancement() throws Exception {
    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();
    sourcesFinancement.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSourcesFinancementMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement))
      )
      .andExpect(status().isBadRequest());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamSourcesFinancement() throws Exception {
    int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();
    sourcesFinancement.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSourcesFinancementMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sourcesFinancement))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the SourcesFinancement in the database
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteSourcesFinancement() throws Exception {
    // Initialize the database
    sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

    int databaseSizeBeforeDelete = sourcesFinancementRepository.findAll().size();

    // Delete the sourcesFinancement
    restSourcesFinancementMockMvc
      .perform(delete(ENTITY_API_URL_ID, sourcesFinancement.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
    assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
