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

import sn.ssi.sigmap.domain.SituationMatrimoniale;
import sn.ssi.sigmap.repository.SituationMatrimonialeRepository;

/**
 * Integration tests for the {@link SituationMatrimonialeResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class SituationMatrimonialeResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/situation-matrimoniales";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private SituationMatrimonialeRepository situationMatrimonialeRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restSituationMatrimonialeMockMvc;

  private SituationMatrimoniale situationMatrimoniale;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SituationMatrimoniale createEntity(EntityManager em) {
    SituationMatrimoniale situationMatrimoniale = new SituationMatrimoniale().libelle(DEFAULT_LIBELLE);
    return situationMatrimoniale;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SituationMatrimoniale createUpdatedEntity(EntityManager em) {
    SituationMatrimoniale situationMatrimoniale = new SituationMatrimoniale().libelle(UPDATED_LIBELLE);
    return situationMatrimoniale;
  }

  @BeforeEach
  public void initTest() {
    situationMatrimoniale = createEntity(em);
  }

  @Test
  @Transactional
  void createSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeCreate = situationMatrimonialeRepository.findAll().size();
    // Create the SituationMatrimoniale
    restSituationMatrimonialeMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isCreated());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeCreate + 1);
    SituationMatrimoniale testSituationMatrimoniale = situationMatrimonialeList.get(situationMatrimonialeList.size() - 1);
    assertThat(testSituationMatrimoniale.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void createSituationMatrimonialeWithExistingId() throws Exception {
    // Create the SituationMatrimoniale with an existing ID
    situationMatrimoniale.setId(1L);

    int databaseSizeBeforeCreate = situationMatrimonialeRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restSituationMatrimonialeMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isBadRequest());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = situationMatrimonialeRepository.findAll().size();
    // set the field null
    situationMatrimoniale.setLibelle(null);

    // Create the SituationMatrimoniale, which fails.

    restSituationMatrimonialeMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isBadRequest());

    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllSituationMatrimoniales() throws Exception {
    // Initialize the database
    situationMatrimonialeRepository.saveAndFlush(situationMatrimoniale);

    // Get all the situationMatrimonialeList
    restSituationMatrimonialeMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(situationMatrimoniale.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
  }

  @Test
  @Transactional
  void getSituationMatrimoniale() throws Exception {
    // Initialize the database
    situationMatrimonialeRepository.saveAndFlush(situationMatrimoniale);

    // Get the situationMatrimoniale
    restSituationMatrimonialeMockMvc
      .perform(get(ENTITY_API_URL_ID, situationMatrimoniale.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(situationMatrimoniale.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
  }

  @Test
  @Transactional
  void getNonExistingSituationMatrimoniale() throws Exception {
    // Get the situationMatrimoniale
    restSituationMatrimonialeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewSituationMatrimoniale() throws Exception {
    // Initialize the database
    situationMatrimonialeRepository.saveAndFlush(situationMatrimoniale);

    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();

    // Update the situationMatrimoniale
    SituationMatrimoniale updatedSituationMatrimoniale = situationMatrimonialeRepository.findById(situationMatrimoniale.getId()).get();
    // Disconnect from session so that the updates on updatedSituationMatrimoniale are not directly saved in db
    em.detach(updatedSituationMatrimoniale);
    updatedSituationMatrimoniale.libelle(UPDATED_LIBELLE);

    restSituationMatrimonialeMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedSituationMatrimoniale.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedSituationMatrimoniale))
      )
      .andExpect(status().isOk());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
    SituationMatrimoniale testSituationMatrimoniale = situationMatrimonialeList.get(situationMatrimonialeList.size() - 1);
    assertThat(testSituationMatrimoniale.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void putNonExistingSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();
    situationMatrimoniale.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSituationMatrimonialeMockMvc
      .perform(
        put(ENTITY_API_URL_ID, situationMatrimoniale.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isBadRequest());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();
    situationMatrimoniale.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSituationMatrimonialeMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isBadRequest());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();
    situationMatrimoniale.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSituationMatrimonialeMockMvc
      .perform(
        put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateSituationMatrimonialeWithPatch() throws Exception {
    // Initialize the database
    situationMatrimonialeRepository.saveAndFlush(situationMatrimoniale);

    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();

    // Update the situationMatrimoniale using partial update
    SituationMatrimoniale partialUpdatedSituationMatrimoniale = new SituationMatrimoniale();
    partialUpdatedSituationMatrimoniale.setId(situationMatrimoniale.getId());

    restSituationMatrimonialeMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSituationMatrimoniale.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSituationMatrimoniale))
      )
      .andExpect(status().isOk());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
    SituationMatrimoniale testSituationMatrimoniale = situationMatrimonialeList.get(situationMatrimonialeList.size() - 1);
    assertThat(testSituationMatrimoniale.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void fullUpdateSituationMatrimonialeWithPatch() throws Exception {
    // Initialize the database
    situationMatrimonialeRepository.saveAndFlush(situationMatrimoniale);

    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();

    // Update the situationMatrimoniale using partial update
    SituationMatrimoniale partialUpdatedSituationMatrimoniale = new SituationMatrimoniale();
    partialUpdatedSituationMatrimoniale.setId(situationMatrimoniale.getId());

    partialUpdatedSituationMatrimoniale.libelle(UPDATED_LIBELLE);

    restSituationMatrimonialeMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSituationMatrimoniale.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSituationMatrimoniale))
      )
      .andExpect(status().isOk());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
    SituationMatrimoniale testSituationMatrimoniale = situationMatrimonialeList.get(situationMatrimonialeList.size() - 1);
    assertThat(testSituationMatrimoniale.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void patchNonExistingSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();
    situationMatrimoniale.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSituationMatrimonialeMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, situationMatrimoniale.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isBadRequest());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();
    situationMatrimoniale.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSituationMatrimonialeMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isBadRequest());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamSituationMatrimoniale() throws Exception {
    int databaseSizeBeforeUpdate = situationMatrimonialeRepository.findAll().size();
    situationMatrimoniale.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSituationMatrimonialeMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(situationMatrimoniale))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the SituationMatrimoniale in the database
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteSituationMatrimoniale() throws Exception {
    // Initialize the database
    situationMatrimonialeRepository.saveAndFlush(situationMatrimoniale);

    int databaseSizeBeforeDelete = situationMatrimonialeRepository.findAll().size();

    // Delete the situationMatrimoniale
    restSituationMatrimonialeMockMvc
      .perform(delete(ENTITY_API_URL_ID, situationMatrimoniale.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<SituationMatrimoniale> situationMatrimonialeList = situationMatrimonialeRepository.findAll();
    assertThat(situationMatrimonialeList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
