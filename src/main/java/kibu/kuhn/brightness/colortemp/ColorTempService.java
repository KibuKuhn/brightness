package kibu.kuhn.brightness.colortemp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.displayunit.IDisplayUnitManager;
import kibu.kuhn.brightness.domain.ColorTemp;
import kibu.kuhn.brightness.prefs.IPreferencesService;

public class ColorTempService implements IColorTempService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ColorTempService.class);

    private ActionListener checkColorTempTimeListener = this::checkColorTempTime;

    private Supplier<Boolean> colorTempTimeDecision;

    private boolean colorTempApplied;
    private ColorTempTimer colorTempTimer;
    private List<ColorTemp> colorTempValues;

    @Inject
    private IPreferencesService preferences;
    @Inject
    private IDisplayUnitManager displayUnitManager;

    private Supplier<Boolean> manualDecision = () -> {
        var fromTime = preferences.getColorTempFromTime();
        var toTime = preferences.getColorTempToTime();
        if (fromTime.equals(toTime)) {
            return false;
        }

        var now = LocalDate.now();
        var from = LocalDateTime.from(fromTime.atDate(now));
        var to = LocalDateTime.from(toTime.atDate(now));
        if (to.isBefore(from)) {
            to = to.plusDays(1);
        }

        var current = LocalDateTime.now();
        if (current.isBefore(from)) {
            return false;
        }
        if (!current.isAfter(to)) {
            return true;
        }
        return false;
    };

    private Supplier<Boolean> autoDecision = () -> {
        return SunriseSunset.isNight(preferences.getLatitude(), preferences.getLongitude());
    };

    @PostConstruct
    public void init() {
        readColorTempValues();
        colorTempApplied = true;
        colorTempTimeDecision = preferences.isColorTempAutoMode() ? autoDecision : manualDecision;
        checkColorTemp();
    }

    private void checkColorTemp() {
        if (!preferences.isColorTemp()) {
            return;
        }
        if (colorTempTimer == null) {
            colorTempTimer = new ColorTempTimer(checkColorTempTimeListener);
            colorTempTimer.start();
        }
    }

    @Override
    public void updateColorTemp(ColorTemp colorTemp) {
        if (!preferences.isColorTemp()) {
            return;
        }
        if (!isColorTempTime()) {
            return;
        }

        displayUnitManager.updateColorTemp(colorTemp);
    }

    private void checkColorTempTime(ActionEvent event) {
        boolean colorTempTime = isColorTempTime();
        LOGGER.debug("isColorTempTime: {}, colorTempApplied: {}", colorTempTime, colorTempApplied);
        if (!colorTempTime) {
            if (colorTempApplied) {
                displayUnitManager.applyColorTemp(getColorTemp(preferences.getDefaultColorTempKelvin()));
                colorTempApplied = false;
            }
            return;
        }
        if (colorTempApplied) {
            return;
        }

        displayUnitManager.applyColorTemp(getColorTemp(preferences.getColorTempKelvin()));
        colorTempApplied = true;
    }

    private ColorTemp getColorTemp(int kelvin) {
        //@formatter:off
        return colorTempValues.stream()
                              .filter(ct -> ct.getKelvin() == kelvin)
                              .findFirst()
                              .get();
        //@formatter:on
    }

    @Override
    public List<ColorTemp> getColorTempValues() {
        return colorTempValues;
    }

    private void readColorTempValues() {
        try (var stream = ColorTempService.class.getResourceAsStream("/colorTempValues");) {
            var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            //@formatter:off
            colorTempValues = reader.lines()
                                    .filter(line -> !line.strip().isEmpty())
                                    .map(line -> line.strip())
                                    .map(ColorTemp::of)
                                    .collect(Collectors.toList());
            //@formatter:on

        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void setColorTempEnabled(boolean enabled) {
        preferences.setColorTemp(enabled);
        if (enabled) {
            checkColorTemp();
        } else {
            int defaultKelvin = preferences.getDefaultColorTempKelvin();
            displayUnitManager.updateColorTemp(getColorTemp(defaultKelvin));
            preferences.setColorTempKelvin(defaultKelvin);
        }

    }

    @Override
    public void applyDefaultColorTemp(ColorTemp colorTemp) {
        if (!preferences.isColorTemp()) {
            return;
        }

        displayUnitManager.updateColorTemp(colorTemp);
    }

    @Override
    public boolean isColorTempTime() {
        return colorTempTimeDecision.get();
    }
}
