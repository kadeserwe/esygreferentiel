package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class DelaisTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Delais.class);
    Delais delais1 = new Delais();
    delais1.setId(1L);
    Delais delais2 = new Delais();
    delais2.setId(delais1.getId());
    assertThat(delais1).isEqualTo(delais2);
    delais2.setId(2L);
    assertThat(delais1).isNotEqualTo(delais2);
    delais1.setId(null);
    assertThat(delais1).isNotEqualTo(delais2);
  }
}
