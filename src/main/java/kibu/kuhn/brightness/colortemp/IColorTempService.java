package kibu.kuhn.brightness.colortemp;

import java.util.List;

import kibu.kuhn.brightness.domain.ColorTemp;

public interface IColorTempService
{

    boolean isColorTempTime();

    List<ColorTemp> getColorTempValues();

    void updateColorTemp(ColorTemp colorTemp);

    void applyDefaultColorTemp(ColorTemp colorTemp);

    void setColorTempEnabled(boolean selected);
}
