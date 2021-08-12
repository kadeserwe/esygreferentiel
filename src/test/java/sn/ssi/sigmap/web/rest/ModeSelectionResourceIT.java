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

import sn.ssi.sigmap.domain.ModeSelection;
import sn.ssi.sigmap.repository.ModeSelectionRepository;

/**
 * Integration tests for the {@link ModeSelectionResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class ModeSelectionResourceIT {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/mode-selections";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private ModeSelectionRepository modeSelectionRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restModeSelectionMockMvc;

  private ModeSelection modeSelection;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static ModeSelection createEntity(EntityManager em) {
    ModeSelection modeSelection = new ModeSelection().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE).description(DEFAULT_DESCRIPTION);
    return modeSelection;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static ModeSelection createUpdatedEntity(EntityManager em) {
    ModeSelection modeSelection = new ModeSelection().code(UPDATED_CODE).libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);
    return modeSelection;
  }

  @BeforeEach
  public void initTest() {
    modeSelection = createEntity(em);
  }

  @Test
  @Transactional
  void createModeSelection() throws Exception {
    int databaseSizeBeforeCreate = modeSelectionRepository.findAll().size();
    // Create the ModeSelection
    restModeSelectionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeSelection)))
      .andExpect(status().isCreated());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeCreate + 1);
    ModeSelection testModeSelection = modeSelectionList.get(modeSelectionList.size() - 1);
    assertThat(testModeSelection.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testModeSelection.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testModeSelection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  void createModeSelectionWithExistingId() throws Exception {
    // Create the ModeSelection with an existing ID
    modeSelection.setId(1L);

    int databaseSizeBeforeCreate = modeSelectionRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restModeSelectionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeSelection)))
      .andExpect(status().isBadRequest());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = modeSelectionRepository.findAll().size();
    // set the field null
    modeSelection.setCode(null);

    // Create the ModeSelection, which fails.

    restModeSelectionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeSelection)))
      .andExpect(status().isBadRequest());

    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = modeSelectionRepository.findAll().size();
    // set the field null
    modeSelection.setLibelle(null);

    // Create the ModeSelection, which fails.

    restModeSelectionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeSelection)))
      .andExpect(status().isBadRequest());

    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllModeSelections() throws Exception {
    // Initialize the database
    modeSelectionRepository.saveAndFlush(modeSelection);

    // Get all the modeSelectionList
    restModeSelectionMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(modeSelection.getId().intValue())))
      .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
  }

  @Test
  @Transactional
  void getModeSelection() throws Exception {
    // Initialize the database
    modeSelectionRepository.saveAndFlush(modeSelection);

    // Get the modeSelection
    restModeSelectionMockMvc
      .perform(get(ENTITY_API_URL_ID, modeSelection.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(modeSelection.getId().intValue()))
      .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
  }

  @Test
  @Transactional
  void getNonExistingModeSelection() throws Exception {
    // Get the modeSelection
    restModeSelectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewModeSelection() throws Exception {
    // Initialize the database
    modeSelectionRepository.saveAndFlush(modeSelection);

    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();

    // Update the modeSelection
    ModeSelection updatedModeSelection = modeSelectionRepository.findById(modeSelection.getId()).get();
    // Disconnect from session so that the updates on updatedModeSelection are not directly saved in db
    em.detach(updatedModeSelection);
    updatedModeSelection.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

    restModeSelectionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedModeSelection.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedModeSelection))
      )
      .andExpect(status().isOk());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
    ModeSelection testModeSelection = modeSelectionList.get(modeSelectionList.size() - 1);
    assertThat(testModeSelection.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testModeSelection.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testModeSelection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void putNonExistingModeSelection() throws Exception {
    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();
    modeSelection.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restModeSelectionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, modeSelection.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(modeSelection))
      )
      .andExpect(status().isBadRequest());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchModeSelection() throws Exception {
    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();
    modeSelection.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restModeSelectionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(modeSelection))
      )
      .andExpect(status().isBadRequest());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamModeSelection() throws Exception {
    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();
    modeSelection.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restModeSelectionMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeSelection)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateModeSelectionWithPatch() throws Exception {
    // Initialize the database
    modeSelectionRepository.saveAndFlush(modeSelection);

    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();

    // Update the modeSelection using partial update
    ModeSelection partialUpdatedModeSelection = new ModeSelection();
    partialUpdatedModeSelection.setId(modeSelection.getId());

    partialUpdatedModeSelection.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

    restModeSelectionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedModeSelection.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModeSelection))
      )
      .andExpect(status().isOk());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
    ModeSelection testModeSelection = modeSelectionList.get(modeSelectionList.size() - 1);
    assertThat(testModeSelection.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testModeSelection.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testModeSelection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void fullUpdateModeSelectionWithPatch() throws Exception {
    // Initialize the database
    modeSelectionRepository.saveAndFlush(modeSelection);

    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();

    // Update the modeSelection using partial update
    ModeSelection partialUpdatedModeSelection = new ModeSelection();
    partialUpdatedModeSelection.setId(modeSelection.getId());

    partialUpdatedModeSelection.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

    restModeSelectionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedModeSelection.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModeSelection))
      )
      .andExpect(status().isOk());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
    ModeSelection testModeSelection = modeSelectionList.get(modeSelectionList.size() - 1);
    assertThat(testModeSelection.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testModeSelection.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testModeSelection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void patchNonExistingModeSelection() throws Exception {
    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();
    modeSelection.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restModeSelectionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, modeSelection.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(modeSelection))
      )
      .andExpect(status().isBadRequest());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchModeSelection() throws Exception {
    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();
    modeSelection.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restModeSelectionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(modeSelection))
      )
      .andExpect(status().isBadRequest());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamModeSelection() throws Exception {
    int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();
    modeSelection.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restModeSelectionMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(modeSelection)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the ModeSelection in the database
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteModeSelection() throws Exception {
    // Initialize the database
    modeSelectionRepository.saveAndFlush(modeSelection);

    int databaseSizeBeforeDelete = modeSelectionRepository.findAll().size();

    // Delete the modeSelection
    restModeSelectionMockMvc
      .perform(delete(ENTITY_API_URL_ID, modeSelection.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
    assertThat(modeSelectionList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
