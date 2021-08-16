package sn.ssi.sigmap.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ssi.sigmap.domain.Banque;
import sn.ssi.sigmap.repository.BanqueRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BanqueResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class BanqueResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String DEFAULT_SIGLE = "AAAAAAAAAA";
  private static final String UPDATED_SIGLE = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/banques";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private BanqueRepository banqueRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restBanqueMockMvc;

  private Banque banque;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Banque createEntity(EntityManager em) {
    Banque banque = new Banque().libelle(DEFAULT_LIBELLE).sigle(DEFAULT_SIGLE);
    return banque;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Banque createUpdatedEntity(EntityManager em) {
    Banque banque = new Banque().libelle(UPDATED_LIBELLE).sigle(UPDATED_SIGLE);
    return banque;
  }

  @BeforeEach
  public void initTest() {
    banque = createEntity(em);
  }

  @Test
  @Transactional
  void createBanque() throws Exception {
    int databaseSizeBeforeCreate = banqueRepository.findAll().size();
    // Create the Banque
    restBanqueMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banque)))
      .andExpect(status().isCreated());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeCreate + 1);
    Banque testBanque = banqueList.get(banqueList.size() - 1);
    assertThat(testBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testBanque.getSigle()).isEqualTo(DEFAULT_SIGLE);
  }

  @Test
  @Transactional
  void createBanqueWithExistingId() throws Exception {
    // Create the Banque with an existing ID
    banque.setId(1L);

    int databaseSizeBeforeCreate = banqueRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restBanqueMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banque)))
      .andExpect(status().isBadRequest());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = banqueRepository.findAll().size();
    // set the field null
    banque.setLibelle(null);

    // Create the Banque, which fails.

    restBanqueMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banque)))
      .andExpect(status().isBadRequest());

    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkSigleIsRequired() throws Exception {
    int databaseSizeBeforeTest = banqueRepository.findAll().size();
    // set the field null
    banque.setSigle(null);

    // Create the Banque, which fails.

    restBanqueMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banque)))
      .andExpect(status().isBadRequest());

    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllBanques() throws Exception {
    // Initialize the database
    banqueRepository.saveAndFlush(banque);

    // Get all the banqueList
    restBanqueMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(banque.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)));
  }

  @Test
  @Transactional
  void getBanque() throws Exception {
    // Initialize the database
    banqueRepository.saveAndFlush(banque);

    // Get the banque
    restBanqueMockMvc
      .perform(get(ENTITY_API_URL_ID, banque.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(banque.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE));
  }

  @Test
  @Transactional
  void getNonExistingBanque() throws Exception {
    // Get the banque
    restBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewBanque() throws Exception {
    // Initialize the database
    banqueRepository.saveAndFlush(banque);

    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

    // Update the banque
    Banque updatedBanque = banqueRepository.findById(banque.getId()).get();
    // Disconnect from session so that the updates on updatedBanque are not directly saved in db
    em.detach(updatedBanque);
    updatedBanque.libelle(UPDATED_LIBELLE).sigle(UPDATED_SIGLE);

    restBanqueMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedBanque.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedBanque))
      )
      .andExpect(status().isOk());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    Banque testBanque = banqueList.get(banqueList.size() - 1);
    assertThat(testBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testBanque.getSigle()).isEqualTo(UPDATED_SIGLE);
  }

  @Test
  @Transactional
  void putNonExistingBanque() throws Exception {
    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
    banque.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBanqueMockMvc
      .perform(
        put(ENTITY_API_URL_ID, banque.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banque))
      )
      .andExpect(status().isBadRequest());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchBanque() throws Exception {
    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
    banque.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBanqueMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(banque))
      )
      .andExpect(status().isBadRequest());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamBanque() throws Exception {
    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
    banque.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBanqueMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banque)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateBanqueWithPatch() throws Exception {
    // Initialize the database
    banqueRepository.saveAndFlush(banque);

    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

    // Update the banque using partial update
    Banque partialUpdatedBanque = new Banque();
    partialUpdatedBanque.setId(banque.getId());

    partialUpdatedBanque.libelle(UPDATED_LIBELLE);

    restBanqueMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedBanque.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanque))
      )
      .andExpect(status().isOk());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    Banque testBanque = banqueList.get(banqueList.size() - 1);
    assertThat(testBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testBanque.getSigle()).isEqualTo(DEFAULT_SIGLE);
  }

  @Test
  @Transactional
  void fullUpdateBanqueWithPatch() throws Exception {
    // Initialize the database
    banqueRepository.saveAndFlush(banque);

    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

    // Update the banque using partial update
    Banque partialUpdatedBanque = new Banque();
    partialUpdatedBanque.setId(banque.getId());

    partialUpdatedBanque.libelle(UPDATED_LIBELLE).sigle(UPDATED_SIGLE);

    restBanqueMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedBanque.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanque))
      )
      .andExpect(status().isOk());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    Banque testBanque = banqueList.get(banqueList.size() - 1);
    assertThat(testBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testBanque.getSigle()).isEqualTo(UPDATED_SIGLE);
  }

  @Test
  @Transactional
  void patchNonExistingBanque() throws Exception {
    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
    banque.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBanqueMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, banque.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(banque))
      )
      .andExpect(status().isBadRequest());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchBanque() throws Exception {
    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
    banque.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBanqueMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(banque))
      )
      .andExpect(status().isBadRequest());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamBanque() throws Exception {
    int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
    banque.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBanqueMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(banque)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Banque in the database
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteBanque() throws Exception {
    // Initialize the database
    banqueRepository.saveAndFlush(banque);

    int databaseSizeBeforeDelete = banqueRepository.findAll().size();

    // Delete the banque
    restBanqueMockMvc
      .perform(delete(ENTITY_API_URL_ID, banque.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Banque> banqueList = banqueRepository.findAll();
    assertThat(banqueList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
