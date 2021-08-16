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

import sn.ssi.sigmap.domain.Hierarchie;
import sn.ssi.sigmap.repository.HierarchieRepository;

/**
 * Integration tests for the {@link HierarchieResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class HierarchieResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/hierarchies";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private HierarchieRepository hierarchieRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restHierarchieMockMvc;

  private Hierarchie hierarchie;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Hierarchie createEntity(EntityManager em) {
    Hierarchie hierarchie = new Hierarchie().libelle(DEFAULT_LIBELLE);
    return hierarchie;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Hierarchie createUpdatedEntity(EntityManager em) {
    Hierarchie hierarchie = new Hierarchie().libelle(UPDATED_LIBELLE);
    return hierarchie;
  }

  @BeforeEach
  public void initTest() {
    hierarchie = createEntity(em);
  }

  @Test
  @Transactional
  void createHierarchie() throws Exception {
    int databaseSizeBeforeCreate = hierarchieRepository.findAll().size();
    // Create the Hierarchie
    restHierarchieMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hierarchie)))
      .andExpect(status().isCreated());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeCreate + 1);
    Hierarchie testHierarchie = hierarchieList.get(hierarchieList.size() - 1);
    assertThat(testHierarchie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void createHierarchieWithExistingId() throws Exception {
    // Create the Hierarchie with an existing ID
    hierarchie.setId(1L);

    int databaseSizeBeforeCreate = hierarchieRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restHierarchieMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hierarchie)))
      .andExpect(status().isBadRequest());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = hierarchieRepository.findAll().size();
    // set the field null
    hierarchie.setLibelle(null);

    // Create the Hierarchie, which fails.

    restHierarchieMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hierarchie)))
      .andExpect(status().isBadRequest());

    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllHierarchies() throws Exception {
    // Initialize the database
    hierarchieRepository.saveAndFlush(hierarchie);

    // Get all the hierarchieList
    restHierarchieMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(hierarchie.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
  }

  @Test
  @Transactional
  void getHierarchie() throws Exception {
    // Initialize the database
    hierarchieRepository.saveAndFlush(hierarchie);

    // Get the hierarchie
    restHierarchieMockMvc
      .perform(get(ENTITY_API_URL_ID, hierarchie.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(hierarchie.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
  }

  @Test
  @Transactional
  void getNonExistingHierarchie() throws Exception {
    // Get the hierarchie
    restHierarchieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewHierarchie() throws Exception {
    // Initialize the database
    hierarchieRepository.saveAndFlush(hierarchie);

    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();

    // Update the hierarchie
    Hierarchie updatedHierarchie = hierarchieRepository.findById(hierarchie.getId()).get();
    // Disconnect from session so that the updates on updatedHierarchie are not directly saved in db
    em.detach(updatedHierarchie);
    updatedHierarchie.libelle(UPDATED_LIBELLE);

    restHierarchieMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedHierarchie.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedHierarchie))
      )
      .andExpect(status().isOk());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
    Hierarchie testHierarchie = hierarchieList.get(hierarchieList.size() - 1);
    assertThat(testHierarchie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void putNonExistingHierarchie() throws Exception {
    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();
    hierarchie.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restHierarchieMockMvc
      .perform(
        put(ENTITY_API_URL_ID, hierarchie.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(hierarchie))
      )
      .andExpect(status().isBadRequest());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchHierarchie() throws Exception {
    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();
    hierarchie.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restHierarchieMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(hierarchie))
      )
      .andExpect(status().isBadRequest());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamHierarchie() throws Exception {
    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();
    hierarchie.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restHierarchieMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hierarchie)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateHierarchieWithPatch() throws Exception {
    // Initialize the database
    hierarchieRepository.saveAndFlush(hierarchie);

    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();

    // Update the hierarchie using partial update
    Hierarchie partialUpdatedHierarchie = new Hierarchie();
    partialUpdatedHierarchie.setId(hierarchie.getId());

    restHierarchieMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedHierarchie.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHierarchie))
      )
      .andExpect(status().isOk());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
    Hierarchie testHierarchie = hierarchieList.get(hierarchieList.size() - 1);
    assertThat(testHierarchie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
  }

  @Test
  @Transactional
  void fullUpdateHierarchieWithPatch() throws Exception {
    // Initialize the database
    hierarchieRepository.saveAndFlush(hierarchie);

    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();

    // Update the hierarchie using partial update
    Hierarchie partialUpdatedHierarchie = new Hierarchie();
    partialUpdatedHierarchie.setId(hierarchie.getId());

    partialUpdatedHierarchie.libelle(UPDATED_LIBELLE);

    restHierarchieMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedHierarchie.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHierarchie))
      )
      .andExpect(status().isOk());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
    Hierarchie testHierarchie = hierarchieList.get(hierarchieList.size() - 1);
    assertThat(testHierarchie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
  }

  @Test
  @Transactional
  void patchNonExistingHierarchie() throws Exception {
    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();
    hierarchie.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restHierarchieMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, hierarchie.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(hierarchie))
      )
      .andExpect(status().isBadRequest());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchHierarchie() throws Exception {
    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();
    hierarchie.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restHierarchieMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(hierarchie))
      )
      .andExpect(status().isBadRequest());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamHierarchie() throws Exception {
    int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();
    hierarchie.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restHierarchieMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hierarchie)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Hierarchie in the database
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteHierarchie() throws Exception {
    // Initialize the database
    hierarchieRepository.saveAndFlush(hierarchie);

    int databaseSizeBeforeDelete = hierarchieRepository.findAll().size();

    // Delete the hierarchie
    restHierarchieMockMvc
      .perform(delete(ENTITY_API_URL_ID, hierarchie.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
    assertThat(hierarchieList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
