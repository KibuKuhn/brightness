package kibu.kuhn.brightness.ui;

import java.util.List;

import javax.swing.SpinnerNumberModel;

import kibu.kuhn.brightness.domain.ColorTemp;

public class ColorTempModel extends SpinnerNumberModel
{

    private static final long serialVersionUID = 1L;

    private static final int STEP = 100;

    private List<ColorTemp> values;
    private int minimum;

    public ColorTempModel(List<ColorTemp> values) {
        this.values = values;
        setMaximum(this.values.get(this.values.size() - 1).getKelvin());
        minimum = this.values.get(0).getKelvin();
        setMinimum(minimum);
        setStepSize(STEP);
        setValue(getMinimum());
    }

    public ColorTemp getColorTemp() {
        int value = ((Number) getValue()).intValue();
        value = (value - minimum) / STEP;
        return values.get(value);
    }
}
