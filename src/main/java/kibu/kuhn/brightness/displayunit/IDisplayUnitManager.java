package kibu.kuhn.brightness.displayunit;

import java.util.List;

import kibu.kuhn.brightness.domain.ColorTemp;
import kibu.kuhn.brightness.domain.DisplayUnit;

public interface IDisplayUnitManager
{
    List<DisplayUnit> getDisplayUnits();

    void updateBrightness(DisplayUnit unit);

    void updateColorTemp(ColorTemp colorTemp);

    void applyColorTemp(ColorTemp ct);
}
