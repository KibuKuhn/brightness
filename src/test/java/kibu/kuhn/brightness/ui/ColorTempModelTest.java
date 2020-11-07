package kibu.kuhn.brightness.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kibu.kuhn.brightness.domain.ColorTemp;

public class ColorTempModelTest
{

    private List<ColorTemp> colorTemps;

    @BeforeEach
    public void init() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("/colorTempValues");) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            //@formatter:off
            colorTemps = reader.lines()
                               .filter(line -> !line.strip().isEmpty())
                               .map(line -> line.strip())
                               .map(ColorTemp::of)
                               .collect(Collectors.toList());
            //@formatter:on

        }

    }

    @Test
    public void testColorTempValues() {
        assertThat(colorTemps).size().isEqualTo(111);
        ColorTemp ct = colorTemps.get(0);
        assertThat(ct.getRed()).isEqualTo(255 / 1000f);
        assertThat(ct.getGreen()).isEqualTo(56 / 1000f);
        assertThat(ct.getBlue()).isEqualTo(0f);
        assertThat(ct.getKelvin()).isEqualTo(1000);

        ct = colorTemps.get(110);
        assertThat(ct.getRed()).isEqualTo(195 / 1000f);
        assertThat(ct.getGreen()).isEqualTo(209 / 1000f);
        assertThat(ct.getBlue()).isEqualTo(255 / 1000f);
        assertThat(ct.getKelvin()).isEqualTo(12000);

    }

}
