package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class NaturesGarantieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NaturesGarantie.class);
        NaturesGarantie naturesGarantie1 = new NaturesGarantie();
        naturesGarantie1.setId(1L);
        NaturesGarantie naturesGarantie2 = new NaturesGarantie();
        naturesGarantie2.setId(naturesGarantie1.getId());
        assertThat(naturesGarantie1).isEqualTo(naturesGarantie2);
        naturesGarantie2.setId(2L);
        assertThat(naturesGarantie1).isNotEqualTo(naturesGarantie2);
        naturesGarantie1.setId(null);
        assertThat(naturesGarantie1).isNotEqualTo(naturesGarantie2);
    }
}
