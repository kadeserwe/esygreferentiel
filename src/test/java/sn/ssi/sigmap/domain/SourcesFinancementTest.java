package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class SourcesFinancementTest {

    @Test
    public void equalsVerifier() throws Exception {
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
