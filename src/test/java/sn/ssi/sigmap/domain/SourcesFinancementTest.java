package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class SourcesFinancementTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(SourcesFinancement.class);
    SourcesFinancement sourcesFinancement1 = new SourcesFinancement();
    sourcesFinancement1.setId(1L);
    SourcesFinancement sourcesFinancement2 = new SourcesFinancement();
    sourcesFinancement2.setId(sourcesFinancement1.getId());
    assertThat(sourcesFinancement1).isEqualTo(sourcesFinancement2);
    sourcesFinancement2.setId(2L);
    assertThat(sourcesFinancement1).isNotEqualTo(sourcesFinancement2);
    sourcesFinancement1.setId(null);
    assertThat(sourcesFinancement1).isNotEqualTo(sourcesFinancement2);
  }
}
