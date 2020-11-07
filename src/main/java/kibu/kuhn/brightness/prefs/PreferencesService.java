package kibu.kuhn.brightness.prefs;

import java.awt.Point;
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

class PreferencesService implements IPreferencesService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesService.class);

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

    private static IPreferencesService service = new PreferencesService();

    static IPreferencesService get() {
        return service;
    }

    PreferencesService() {
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
        var locale = getPreferences().get(LOCALE, null);
        if (locale == null) {
            return Locale.getDefault();
        }

        return Locale.forLanguageTag(locale);
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
    public int getColorTempKelvin() {
        return getPreferences().getInt(COLOR_TEMP, 5000);
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
}
