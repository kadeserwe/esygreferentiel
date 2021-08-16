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

import sn.ssi.sigmap.domain.GroupesImputation;
import sn.ssi.sigmap.repository.GroupesImputationRepository;

/**
 * Integration tests for the {@link GroupesImputationResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class GroupesImputationResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/groupes-imputations";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private GroupesImputationRepository groupesImputationRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restGroupesImputationMockMvc;

  private GroupesImputation groupesImputation;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static GroupesImputation createEntity(EntityManager em) {
    GroupesImputation groupesImputation = new GroupesImputation().libelle(DEFAULT_LIBELLE).description(DEFAULT_DESCRIPTION);
    return groupesImputation;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static GroupesImputation createUpdatedEntity(EntityManager em) {
    GroupesImputation groupesImputation = new GroupesImputation().libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);
    return groupesImputation;
  }

  @BeforeEach
  public void initTest() {
    groupesImputation = createEntity(em);
  }

  @Test
  @Transactional
  void createGroupesImputation() throws Exception {
    int databaseSizeBeforeCreate = groupesImputationRepository.findAll().size();
    // Create the GroupesImputation
    restGroupesImputationMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
      .andExpect(status().isCreated());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeCreate + 1);
    GroupesImputation testGroupesImputation = groupesImputationList.get(groupesImputationList.size() - 1);
    assertThat(testGroupesImputation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testGroupesImputation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  void createGroupesImputationWithExistingId() throws Exception {
    // Create the GroupesImputation with an existing ID
    groupesImputation.setId(1L);

    int databaseSizeBeforeCreate = groupesImputationRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restGroupesImputationMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
      .andExpect(status().isBadRequest());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = groupesImputationRepository.findAll().size();
    // set the field null
    groupesImputation.setLibelle(null);

    // Create the GroupesImputation, which fails.

    restGroupesImputationMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
      .andExpect(status().isBadRequest());

    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllGroupesImputations() throws Exception {
    // Initialize the database
    groupesImputationRepository.saveAndFlush(groupesImputation);

    // Get all the groupesImputationList
    restGroupesImputationMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(groupesImputation.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
  }

  @Test
  @Transactional
  void getGroupesImputation() throws Exception {
    // Initialize the database
    groupesImputationRepository.saveAndFlush(groupesImputation);

    // Get the groupesImputation
    restGroupesImputationMockMvc
      .perform(get(ENTITY_API_URL_ID, groupesImputation.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(groupesImputation.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
  }

  @Test
  @Transactional
  void getNonExistingGroupesImputation() throws Exception {
    // Get the groupesImputation
    restGroupesImputationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewGroupesImputation() throws Exception {
    // Initialize the database
    groupesImputationRepository.saveAndFlush(groupesImputation);

    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();

    // Update the groupesImputation
    GroupesImputation updatedGroupesImputation = groupesImputationRepository.findById(groupesImputation.getId()).get();
    // Disconnect from session so that the updates on updatedGroupesImputation are not directly saved in db
    em.detach(updatedGroupesImputation);
    updatedGroupesImputation.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

    restGroupesImputationMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedGroupesImputation.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedGroupesImputation))
      )
      .andExpect(status().isOk());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
    GroupesImputation testGroupesImputation = groupesImputationList.get(groupesImputationList.size() - 1);
    assertThat(testGroupesImputation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testGroupesImputation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void putNonExistingGroupesImputation() throws Exception {
    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();
    groupesImputation.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restGroupesImputationMockMvc
      .perform(
        put(ENTITY_API_URL_ID, groupesImputation.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(groupesImputation))
      )
      .andExpect(status().isBadRequest());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchGroupesImputation() throws Exception {
    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();
    groupesImputation.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restGroupesImputationMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(groupesImputation))
      )
      .andExpect(status().isBadRequest());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamGroupesImputation() throws Exception {
    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();
    groupesImputation.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restGroupesImputationMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateGroupesImputationWithPatch() throws Exception {
    // Initialize the database
    groupesImputationRepository.saveAndFlush(groupesImputation);

    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();

    // Update the groupesImputation using partial update
    GroupesImputation partialUpdatedGroupesImputation = new GroupesImputation();
    partialUpdatedGroupesImputation.setId(groupesImputation.getId());

    restGroupesImputationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedGroupesImputation.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupesImputation))
      )
      .andExpect(status().isOk());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
    GroupesImputation testGroupesImputation = groupesImputationList.get(groupesImputationList.size() - 1);
    assertThat(testGroupesImputation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testGroupesImputation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  void fullUpdateGroupesImputationWithPatch() throws Exception {
    // Initialize the database
    groupesImputationRepository.saveAndFlush(groupesImputation);

    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();

    // Update the groupesImputation using partial update
    GroupesImputation partialUpdatedGroupesImputation = new GroupesImputation();
    partialUpdatedGroupesImputation.setId(groupesImputation.getId());

    partialUpdatedGroupesImputation.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

    restGroupesImputationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedGroupesImputation.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupesImputation))
      )
      .andExpect(status().isOk());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
    GroupesImputation testGroupesImputation = groupesImputationList.get(groupesImputationList.size() - 1);
    assertThat(testGroupesImputation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testGroupesImputation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void patchNonExistingGroupesImputation() throws Exception {
    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();
    groupesImputation.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restGroupesImputationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, groupesImputation.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(groupesImputation))
      )
      .andExpect(status().isBadRequest());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchGroupesImputation() throws Exception {
    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();
    groupesImputation.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restGroupesImputationMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(groupesImputation))
      )
      .andExpect(status().isBadRequest());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamGroupesImputation() throws Exception {
    int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();
    groupesImputation.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restGroupesImputationMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(groupesImputation))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the GroupesImputation in the database
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteGroupesImputation() throws Exception {
    // Initialize the database
    groupesImputationRepository.saveAndFlush(groupesImputation);

    int databaseSizeBeforeDelete = groupesImputationRepository.findAll().size();

    // Delete the groupesImputation
    restGroupesImputationMockMvc
      .perform(delete(ENTITY_API_URL_ID, groupesImputation.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
    assertThat(groupesImputationList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
