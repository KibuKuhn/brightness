package kibu.kuhn.brightness.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SpinnerNumberModel;

import kibu.kuhn.brightness.domain.ColorTemp;

public class ColorTempModel extends SpinnerNumberModel
{

    private static final long serialVersionUID = 1L;

    private static List<ColorTemp> values;

    static {
        readValues();
    }

    private static int minimum;

    private static int maximum;

    private static int step = 100;

    public ColorTempModel() {
        setMaximum(maximum);
        setMinimum(minimum);
        setStepSize(step);
        setValue(getMinimum());
    }

    public ColorTemp getColorTemp() {
        int value = ((Number) getValue()).intValue();
        value = (value - minimum) / step;
        return values.get(value);
    }

    private static void readValues() {
        try (var stream = ColorTempModel.class.getResourceAsStream("/colorTempValues");) {
            var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            //@formatter:off
            values = reader.lines()
                           .filter(line -> !line.strip().isEmpty())
                           .map(line -> line.strip())
                           .map(ColorTemp::of)
                           .collect(Collectors.toList());
            //@formatter:on
            minimum = values.get(0).getKelvin();
            maximum = values.get(values.size() - 1).getKelvin();

        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
