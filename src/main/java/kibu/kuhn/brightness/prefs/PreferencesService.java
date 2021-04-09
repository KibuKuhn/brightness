package kibu.kuhn.brightness.prefs;

import java.awt.Point;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.Brightness;
import kibu.kuhn.brightness.domain.DisplayUnit;

public class PreferencesService implements IPreferencesService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesService.class);

    private static final int DEFAULT_COLOR_TEMP_KELVIN = 5000;
    private static final String DARK_MODE = "darkMode";
    private static final String MAIN_MENU_LOCATION = "mainMenuLocation";
    private static final String MAIN_MENU_LOCATION_UPDATE = "mainMenuLocationUpdate";
    static final String LOCALE = "locale";
    static final String LAF = "laf";
    static final String ITEMS = "items";
    private static final String CLEAN = "clean";
    private static final String ALL_UNITS = "allUnits";
    private static final String COLOR_TEMP_MODE = "colorTempMode";
    private static final String COLOR_TEMP = "colorTemp";
    private static final String COLOR_TEMP_AUTO_MODE = "colorTempAutoMode";
    private static final String COLOR_TEMP_FROM_TIME = "colorTempFromTime";
    private static final String COLOR_TEMP_TO_TIME = "colorTempToTime";
    private static final String DEFAULT_COLOR_TEMP_KELVIN_KEY = "defaultColorTempKelvinKey";

    private static final DateTimeFormatter COLOR_TEMP_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public PreferencesService() {
        if (System.getProperty(CLEAN) != null) {
            try {
                getPreferences().clear();
                LOGGER.info("Preferences cleaned");
            } catch (BackingStoreException e) {
                throw new IllegalStateException("Cannot delete preferences");
            }
        }
    }

    Preferences getPreferences() {
        return Preferences.userNodeForPackage(Brightness.class);
    }

    @Override
    public void saveLaf(LookAndFeelInfo laf) {
        getPreferences().put(LAF, laf.getClassName());
    }

    @Override
    public void saveLocale(Locale locale) {
        getPreferences().put(LOCALE, locale.getLanguage());
    }

    @Override
    public LookAndFeelInfo getLaf() {
        var laf = getPreferences().get(LAF, UIManager.getSystemLookAndFeelClassName());
        // @formatter:off
		return Arrays.stream(UIManager.getInstalledLookAndFeels()).filter(lf -> lf.getClassName().equals(laf))
				.findFirst().orElseThrow(() -> new IllegalStateException("Cannot find Look and Feel " + laf));
		// @formatter:on
    }

    @Override
    public Locale getLocale() {
        return Locale.forLanguageTag(getPreferences().get(LOCALE, Locale.getDefault().toLanguageTag()));
    }

    @Override
    public boolean isMainMenuLocationUpdatEnabled() {
        return getPreferences().getBoolean(MAIN_MENU_LOCATION_UPDATE, true);
    }

    @Override
    public void setMainMenuLocationUpdateEnabled(boolean enabled) {
        getPreferences().putBoolean(MAIN_MENU_LOCATION_UPDATE, enabled);
    }

    @Override
    public void saveMainMenuLocation(Point locationOnScreen) {
        getPreferences().put(MAIN_MENU_LOCATION, toString(locationOnScreen));

    }

    private String toString(Point locationOnScreen) {
        if (locationOnScreen == null) {
            return null;
        }
        return locationOnScreen.x + "/" + locationOnScreen.y;
    }

    @Override
    public Point getMainMenuLocation() {
        var s = getPreferences().get(MAIN_MENU_LOCATION, null);
        if (s == null) {
            return null;
        }

        var split = s.split("/");
        return new Point(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

    @Override
    public void setDarkMode(boolean selected) {
        getPreferences().putBoolean(DARK_MODE, selected);
    }

    @Override
    public boolean isDarkMode() {
        return getPreferences().getBoolean(DARK_MODE, false);
    }

    @Override
    public DisplayUnit getBrightness(DisplayUnit unit) {
        var value = getPreferences().getInt(unit.getName(), -1);
        if (value < 0) {
            value = 100;
            getPreferences().putInt(unit.getName(), value);
        }

        return new DisplayUnit(unit.getName(), value);
    }

    @Override
    public void setBrightness(DisplayUnit unit) {
        getPreferences().putInt(unit.getName(), unit.getValue());
    }

    @Override
    public void setAllUnits(boolean selected) {
        getPreferences().putBoolean(ALL_UNITS, selected);
    }

    @Override
    public boolean isAllUnits() {
        return getPreferences().getBoolean(ALL_UNITS, false);
    }

    @Override
    public boolean isColorTemp() {
        return getPreferences().getBoolean(COLOR_TEMP_MODE, false);
    }

    @Override
    public void setColorTemp(boolean mode) {
        getPreferences().putBoolean(COLOR_TEMP_MODE, mode);
    }

    @Override
    public int getDefaultColorTempKelvin() {
        return getPreferences().getInt(DEFAULT_COLOR_TEMP_KELVIN_KEY, DEFAULT_COLOR_TEMP_KELVIN);
    }

    @Override
    public int getColorTempKelvin() {
        return getPreferences().getInt(COLOR_TEMP, DEFAULT_COLOR_TEMP_KELVIN);
    }

    @Override
    public void setColorTempKelvin(int kelvin) {
        getPreferences().putInt(COLOR_TEMP, kelvin);
    }

    @Override
    public boolean isColorTempAutoMode() {
        return getPreferences().getBoolean(COLOR_TEMP_AUTO_MODE, false);
    }

    @Override
    public void setColorTempAutoMode(boolean autoMode) {
        getPreferences().putBoolean(COLOR_TEMP_AUTO_MODE, autoMode);
    }

    @Override
    public void setColorTempFromTime(LocalTime time) {
        getPreferences().put(COLOR_TEMP_FROM_TIME, time.format(COLOR_TEMP_TIME_FORMATTER));
    }

    @Override
    public void setColorTempToTime(LocalTime time) {
        getPreferences().put(COLOR_TEMP_TO_TIME, time.format(COLOR_TEMP_TIME_FORMATTER));
    }

    @Override
    public LocalTime getColorTempFromTime() {
        String from = getPreferences().get(COLOR_TEMP_FROM_TIME, LocalTime.now().format(COLOR_TEMP_TIME_FORMATTER));
        return LocalTime.parse(from, COLOR_TEMP_TIME_FORMATTER);
    }

    @Override
    public LocalTime getColorTempToTime() {
        String to = getPreferences().get(COLOR_TEMP_TO_TIME, LocalTime.now().format(COLOR_TEMP_TIME_FORMATTER));
        return LocalTime.parse(to, COLOR_TEMP_TIME_FORMATTER);
    }

    @Override
    public void setDefaultColorTempKelvin(int kelvin) {
        getPreferences().putInt(DEFAULT_COLOR_TEMP_KELVIN_KEY, kelvin);
    }
}
