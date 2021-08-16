package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class SpecialitesPersonnelTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(SpecialitesPersonnel.class);
    SpecialitesPersonnel specialitesPersonnel1 = new SpecialitesPersonnel();
    specialitesPersonnel1.setId(1L);
    SpecialitesPersonnel specialitesPersonnel2 = new SpecialitesPersonnel();
    specialitesPersonnel2.setId(specialitesPersonnel1.getId());
    assertThat(specialitesPersonnel1).isEqualTo(specialitesPersonnel2);
    specialitesPersonnel2.setId(2L);
    assertThat(specialitesPersonnel1).isNotEqualTo(specialitesPersonnel2);
    specialitesPersonnel1.setId(null);
    assertThat(specialitesPersonnel1).isNotEqualTo(specialitesPersonnel2);
  }
}
