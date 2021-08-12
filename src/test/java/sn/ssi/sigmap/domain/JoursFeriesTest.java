package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class JoursFeriesTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(JoursFeries.class);
    JoursFeries joursFeries1 = new JoursFeries();
    joursFeries1.setId(1L);
    JoursFeries joursFeries2 = new JoursFeries();
    joursFeries2.setId(joursFeries1.getId());
    assertThat(joursFeries1).isEqualTo(joursFeries2);
    joursFeries2.setId(2L);
    assertThat(joursFeries1).isNotEqualTo(joursFeries2);
    joursFeries1.setId(null);
    assertThat(joursFeries1).isNotEqualTo(joursFeries2);
  }
}
