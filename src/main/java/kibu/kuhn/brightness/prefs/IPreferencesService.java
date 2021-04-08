package kibu.kuhn.brightness.prefs;

import java.awt.Point;
import java.time.LocalTime;
import java.util.Locale;

import javax.swing.UIManager.LookAndFeelInfo;

import kibu.kuhn.brightness.domain.DisplayUnit;

public interface IPreferencesService
{

    void saveLaf(LookAndFeelInfo laf);

    void saveLocale(Locale locale);

    LookAndFeelInfo getLaf();

    Locale getLocale();

    boolean isMainMenuLocationUpdatEnabled();

    void setMainMenuLocationUpdateEnabled(boolean enabled);

    void saveMainMenuLocation(Point locationOnScreen);

    Point getMainMenuLocation();

    void setDarkMode(boolean selected);

    boolean isDarkMode();

    DisplayUnit getBrightness(DisplayUnit unit);

    void setBrightness(DisplayUnit unit);

    boolean isAllUnits();

    void setAllUnits(boolean selected);

    boolean isColorTemp();

    void setColorTemp(boolean mode);

    int getColorTempKelvin();

    void setColorTempKelvin(int kelvin);

    boolean isColorTempAutoMode();

    void setColorTempAutoMode(boolean autoMode);

    void setColorTempFromTime(LocalTime time);

    void setColorTempToTime(LocalTime time);

    LocalTime getColorTempFromTime();

    LocalTime getColorTempToTime();

}