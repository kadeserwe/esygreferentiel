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

import sn.ssi.sigmap.domain.CriteresQualification;
import sn.ssi.sigmap.repository.CriteresQualificationRepository;

/**
 * Integration tests for the {@link CriteresQualificationResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class CriteresQualificationResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/criteres-qualifications";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private CriteresQualificationRepository criteresQualificationRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restCriteresQualificationMockMvc;

  private CriteresQualification criteresQualification;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static CriteresQualification createEntity(EntityManager em) {
    CriteresQualification criteresQualification = new CriteresQualification().libelle(DEFAULT_LIBELLE);
    return criteresQualification;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static CriteresQualification createUpdatedEntity(EntityManager em) {
    CriteresQualification criteresQualification = new CriteresQualification().libelle(UPDATED_LIBELLE);
    return criteresQualification;
  }

  @BeforeEach
  public void initTest() {
    criteresQualification = createEntity(em);
  }

  @Test
  @Transactional
  void createCriteresQualification() throws Exception {
    int databaseSizeBeforeCreate = criteresQualificationRepository.findAll().size();
    // Create the CriteresQualification
    restCriteresQualificationMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isCreated());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeCreate + 1);
    CriteresQualification testCriteresQualification = criteresQualificationList.get(criteresQualificationList.size() - 1);
    assertThat(testCriteresQualification.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void createCriteresQualificationWithExistingId() throws Exception {
    // Create the CriteresQualification with an existing ID
    criteresQualification.setId(1L);

    int databaseSizeBeforeCreate = criteresQualificationRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restCriteresQualificationMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isBadRequest());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllCriteresQualifications() throws Exception {
    // Initialize the database
    criteresQualificationRepository.saveAndFlush(criteresQualification);

    // Get all the criteresQualificationList
    restCriteresQualificationMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(criteresQualification.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
  }

  @Test
  @Transactional
  void getCriteresQualification() throws Exception {
    // Initialize the database
    criteresQualificationRepository.saveAndFlush(criteresQualification);

    // Get the criteresQualification
    restCriteresQualificationMockMvc
      .perform(get(ENTITY_API_URL_ID, criteresQualification.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(criteresQualification.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
  }

  @Test
  @Transactional
  void getNonExistingCriteresQualification() throws Exception {
    // Get the criteresQualification
    restCriteresQualificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewCriteresQualification() throws Exception {
    // Initialize the database
    criteresQualificationRepository.saveAndFlush(criteresQualification);

    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();

    // Update the criteresQualification
    CriteresQualification updatedCriteresQualification = criteresQualificationRepository.findById(criteresQualification.getId()).get();
    // Disconnect from session so that the updates on updatedCriteresQualification are not directly saved in db
    em.detach(updatedCriteresQualification);
    updatedCriteresQualification.libelle(UPDATED_LIBELLE);

    restCriteresQualificationMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedCriteresQualification.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedCriteresQualification))
      )
      .andExpect(status().isOk());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
    CriteresQualification testCriteresQualification = criteresQualificationList.get(criteresQualificationList.size() - 1);
    assertThat(testCriteresQualification.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void putNonExistingCriteresQualification() throws Exception {
    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();
    criteresQualification.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCriteresQualificationMockMvc
      .perform(
        put(ENTITY_API_URL_ID, criteresQualification.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isBadRequest());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchCriteresQualification() throws Exception {
    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();
    criteresQualification.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCriteresQualificationMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isBadRequest());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamCriteresQualification() throws Exception {
    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();
    criteresQualification.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCriteresQualificationMockMvc
      .perform(
        put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateCriteresQualificationWithPatch() throws Exception {
    // Initialize the database
    criteresQualificationRepository.saveAndFlush(criteresQualification);

    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();

    // Update the criteresQualification using partial update
    CriteresQualification partialUpdatedCriteresQualification = new CriteresQualification();
    partialUpdatedCriteresQualification.setId(criteresQualification.getId());

    restCriteresQualificationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedCriteresQualification.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCriteresQualification))
      )
      .andExpect(status().isOk());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
    CriteresQualification testCriteresQualification = criteresQualificationList.get(criteresQualificationList.size() - 1);
    assertThat(testCriteresQualification.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void fullUpdateCriteresQualificationWithPatch() throws Exception {
    // Initialize the database
    criteresQualificationRepository.saveAndFlush(criteresQualification);

    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();

    // Update the criteresQualification using partial update
    CriteresQualification partialUpdatedCriteresQualification = new CriteresQualification();
    partialUpdatedCriteresQualification.setId(criteresQualification.getId());

    partialUpdatedCriteresQualification.libelle(UPDATED_LIBELLE);

    restCriteresQualificationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedCriteresQualification.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCriteresQualification))
      )
      .andExpect(status().isOk());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
    CriteresQualification testCriteresQualification = criteresQualificationList.get(criteresQualificationList.size() - 1);
    assertThat(testCriteresQualification.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void patchNonExistingCriteresQualification() throws Exception {
    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();
    criteresQualification.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCriteresQualificationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, criteresQualification.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isBadRequest());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchCriteresQualification() throws Exception {
    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();
    criteresQualification.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCriteresQualificationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isBadRequest());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamCriteresQualification() throws Exception {
    int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();
    criteresQualification.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCriteresQualificationMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(criteresQualification))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the CriteresQualification in the database
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteCriteresQualification() throws Exception {
    // Initialize the database
    criteresQualificationRepository.saveAndFlush(criteresQualification);

    int databaseSizeBeforeDelete = criteresQualificationRepository.findAll().size();

    // Delete the criteresQualification
    restCriteresQualificationMockMvc
      .perform(delete(ENTITY_API_URL_ID, criteresQualification.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
    assertThat(criteresQualificationList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
