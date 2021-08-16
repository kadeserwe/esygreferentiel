package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class GarantieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Garantie.class);
        Garantie garantie1 = new Garantie();
        garantie1.setId(1L);
        Garantie garantie2 = new Garantie();
        garantie2.setId(garantie1.getId());
        assertThat(garantie1).isEqualTo(garantie2);
        garantie2.setId(2L);
        assertThat(garantie1).isNotEqualTo(garantie2);
        garantie1.setId(null);
        assertThat(garantie1).isNotEqualTo(garantie2);
    }
}
