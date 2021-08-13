package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class CategorieFournisseurTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CategorieFournisseur.class);
    CategorieFournisseur categorieFournisseur1 = new CategorieFournisseur();
    categorieFournisseur1.setId(1L);
    CategorieFournisseur categorieFournisseur2 = new CategorieFournisseur();
    categorieFournisseur2.setId(categorieFournisseur1.getId());
    assertThat(categorieFournisseur1).isEqualTo(categorieFournisseur2);
    categorieFournisseur2.setId(2L);
    assertThat(categorieFournisseur1).isNotEqualTo(categorieFournisseur2);
    categorieFournisseur1.setId(null);
    assertThat(categorieFournisseur1).isNotEqualTo(categorieFournisseur2);
  }
}
