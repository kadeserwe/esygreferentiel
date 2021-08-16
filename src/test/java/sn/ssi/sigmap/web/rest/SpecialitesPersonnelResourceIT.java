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

import sn.ssi.sigmap.domain.SpecialitesPersonnel;
import sn.ssi.sigmap.repository.SpecialitesPersonnelRepository;

/**
 * Integration tests for the {@link SpecialitesPersonnelResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class SpecialitesPersonnelResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/specialites-personnels";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private SpecialitesPersonnelRepository specialitesPersonnelRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restSpecialitesPersonnelMockMvc;

  private SpecialitesPersonnel specialitesPersonnel;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SpecialitesPersonnel createEntity(EntityManager em) {
    SpecialitesPersonnel specialitesPersonnel = new SpecialitesPersonnel().libelle(DEFAULT_LIBELLE);
    return specialitesPersonnel;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SpecialitesPersonnel createUpdatedEntity(EntityManager em) {
    SpecialitesPersonnel specialitesPersonnel = new SpecialitesPersonnel().libelle(UPDATED_LIBELLE);
    return specialitesPersonnel;
  }

  @BeforeEach
  public void initTest() {
    specialitesPersonnel = createEntity(em);
  }

  @Test
  @Transactional
  void createSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeCreate = specialitesPersonnelRepository.findAll().size();
    // Create the SpecialitesPersonnel
    restSpecialitesPersonnelMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isCreated());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeCreate + 1);
    SpecialitesPersonnel testSpecialitesPersonnel = specialitesPersonnelList.get(specialitesPersonnelList.size() - 1);
    assertThat(testSpecialitesPersonnel.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void createSpecialitesPersonnelWithExistingId() throws Exception {
    // Create the SpecialitesPersonnel with an existing ID
    specialitesPersonnel.setId(1L);

    int databaseSizeBeforeCreate = specialitesPersonnelRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restSpecialitesPersonnelMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isBadRequest());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = specialitesPersonnelRepository.findAll().size();
    // set the field null
    specialitesPersonnel.setLibelle(null);

    // Create the SpecialitesPersonnel, which fails.

    restSpecialitesPersonnelMockMvc
      .perform(
        post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isBadRequest());

    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllSpecialitesPersonnels() throws Exception {
    // Initialize the database
    specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

    // Get all the specialitesPersonnelList
    restSpecialitesPersonnelMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(specialitesPersonnel.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
  }

  @Test
  @Transactional
  void getSpecialitesPersonnel() throws Exception {
    // Initialize the database
    specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

    // Get the specialitesPersonnel
    restSpecialitesPersonnelMockMvc
      .perform(get(ENTITY_API_URL_ID, specialitesPersonnel.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(specialitesPersonnel.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
  }

  @Test
  @Transactional
  void getNonExistingSpecialitesPersonnel() throws Exception {
    // Get the specialitesPersonnel
    restSpecialitesPersonnelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewSpecialitesPersonnel() throws Exception {
    // Initialize the database
    specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();

    // Update the specialitesPersonnel
    SpecialitesPersonnel updatedSpecialitesPersonnel = specialitesPersonnelRepository.findById(specialitesPersonnel.getId()).get();
    // Disconnect from session so that the updates on updatedSpecialitesPersonnel are not directly saved in db
    em.detach(updatedSpecialitesPersonnel);
    updatedSpecialitesPersonnel.libelle(UPDATED_LIBELLE);

    restSpecialitesPersonnelMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedSpecialitesPersonnel.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedSpecialitesPersonnel))
      )
      .andExpect(status().isOk());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
    SpecialitesPersonnel testSpecialitesPersonnel = specialitesPersonnelList.get(specialitesPersonnelList.size() - 1);
    assertThat(testSpecialitesPersonnel.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void putNonExistingSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();
    specialitesPersonnel.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSpecialitesPersonnelMockMvc
      .perform(
        put(ENTITY_API_URL_ID, specialitesPersonnel.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isBadRequest());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();
    specialitesPersonnel.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSpecialitesPersonnelMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isBadRequest());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();
    specialitesPersonnel.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSpecialitesPersonnelMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateSpecialitesPersonnelWithPatch() throws Exception {
    // Initialize the database
    specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();

    // Update the specialitesPersonnel using partial update
    SpecialitesPersonnel partialUpdatedSpecialitesPersonnel = new SpecialitesPersonnel();
    partialUpdatedSpecialitesPersonnel.setId(specialitesPersonnel.getId());

    restSpecialitesPersonnelMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSpecialitesPersonnel.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialitesPersonnel))
      )
      .andExpect(status().isOk());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
    SpecialitesPersonnel testSpecialitesPersonnel = specialitesPersonnelList.get(specialitesPersonnelList.size() - 1);
    assertThat(testSpecialitesPersonnel.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void fullUpdateSpecialitesPersonnelWithPatch() throws Exception {
    // Initialize the database
    specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();

    // Update the specialitesPersonnel using partial update
    SpecialitesPersonnel partialUpdatedSpecialitesPersonnel = new SpecialitesPersonnel();
    partialUpdatedSpecialitesPersonnel.setId(specialitesPersonnel.getId());

    partialUpdatedSpecialitesPersonnel.libelle(UPDATED_LIBELLE);

    restSpecialitesPersonnelMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSpecialitesPersonnel.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialitesPersonnel))
      )
      .andExpect(status().isOk());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
    SpecialitesPersonnel testSpecialitesPersonnel = specialitesPersonnelList.get(specialitesPersonnelList.size() - 1);
    assertThat(testSpecialitesPersonnel.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void patchNonExistingSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();
    specialitesPersonnel.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSpecialitesPersonnelMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, specialitesPersonnel.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isBadRequest());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();
    specialitesPersonnel.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSpecialitesPersonnelMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isBadRequest());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamSpecialitesPersonnel() throws Exception {
    int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();
    specialitesPersonnel.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSpecialitesPersonnelMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the SpecialitesPersonnel in the database
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteSpecialitesPersonnel() throws Exception {
    // Initialize the database
    specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

    int databaseSizeBeforeDelete = specialitesPersonnelRepository.findAll().size();

    // Delete the specialitesPersonnel
    restSpecialitesPersonnelMockMvc
      .perform(delete(ENTITY_API_URL_ID, specialitesPersonnel.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
    assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
