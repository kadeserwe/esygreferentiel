package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class ConfigurationTauxTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationTaux.class);
        ConfigurationTaux configurationTaux1 = new ConfigurationTaux();
        configurationTaux1.setId(1L);
        ConfigurationTaux configurationTaux2 = new ConfigurationTaux();
        configurationTaux2.setId(configurationTaux1.getId());
        assertThat(configurationTaux1).isEqualTo(configurationTaux2);
        configurationTaux2.setId(2L);
        assertThat(configurationTaux1).isNotEqualTo(configurationTaux2);
        configurationTaux1.setId(null);
        assertThat(configurationTaux1).isNotEqualTo(configurationTaux2);
    }
}
