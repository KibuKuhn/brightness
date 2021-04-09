package kibu.kuhn.brightness.displayunit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.domain.ColorTemp;
import kibu.kuhn.brightness.domain.DisplayUnit;

public class DisplayUnitManager implements IDisplayUnitManager
{

    private static final int PROCESS_TIMEOUT = 100;

    private static final int TIMER_DELAY_MILLIS = 50;

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayUnitManager.class);

    private Map<String, DisplayUnitTimer> timers = new HashMap<>();

    private List<String> unitNames;

    private ActionListener brightnessTimerEventConsumer = this::handleBrightnessTimerEvent;
    private ActionListener colorTempTimerEventConsumer = this::handleColorTempTimerEvent;

    @PostConstruct
    public void init() {
        unitNames = queryUnitNames();
    }

    @Override
    public void updateColorTemp(ColorTemp colorTemp) {
        var timer = getTimer(null, colorTempTimerEventConsumer);
        if (timer.isRunning()) {
            LOGGER.debug("unit={}", "null");
            return;
        }
        timer.restartWith(colorTemp);
    }

    private DisplayUnitTimer getTimer(String unitName, ActionListener consumer) {
        LOGGER.debug("unitName: {}, consumer: {}", unitName, consumer);
        var timer = timers.get(unitName);
        if (timer == null) {
            timer = new DisplayUnitTimer(TIMER_DELAY_MILLIS, unitName);
            timers.put(unitName, timer);
        }
        timer.setActionConsumer(consumer);
        return timer;
    }

    @Override
    public List<DisplayUnit> getDisplayUnits() {
        // @formatter:off
		unitNames = queryUnitNames();
		//@formatter:off
		return unitNames.stream()
		                .map(DisplayUnit::new)
		                .collect(Collectors.toList());
		// @formatter:on
    }

    private List<String> queryUnitNames() {
        try {
            var commands = Arrays.asList("xrandr");
            var lines = startProcess(commands);
            // @formatter:off
			return lines.stream()
					    .filter(line -> line.contains("connected") && !line.contains("disconnected"))
					    .map(line -> line.split("connected")[0].trim())
					    .collect(Collectors.toList());
			// @formatter:on
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private List<String> startProcess(List<String> commands)
            throws IOException, InterruptedException, ExecutionException, TimeoutException {
        var builder = new ProcessBuilder(commands);
        var process = builder.start();
        var lines = new ArrayList<String>();
        handleStream(process.getInputStream(), lines::add);
        handleStream(process.getErrorStream(), LOGGER::error);
        process.onExit().get(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS);
        return lines;
    }

    private void handleStream(InputStream in, Consumer<String> c) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        while (true) {
            var line = reader.readLine();
            if (line == null) {
                break;
            }

            c.accept(line);
        }
    }

    private void handleBrightnessTimerEvent(ActionEvent e) {
        var timer = (DisplayUnitTimer) e.getSource();
        LOGGER.debug("Unit={}, value={}", timer.getUnitName(), timer.getValue());
        float value = ((Integer) timer.getValue()) / 100f;
        if (timer.getUnitName() == null) {
            processAllUnits(unitName -> processUnit(() -> getBrightnessCommandString(value, unitName)));
        } else {
            processUnit(() -> getBrightnessCommandString(value, timer.getUnitName()));
        }
    }

    private List<String> getBrightnessCommandString(float value, String unitName) {
        return Arrays.asList("xrandr", "--output", unitName, "--brightness", Float.toString(value));
    }

    private void processAllUnits(Consumer<String> unitConsumer) {
        unitNames.parallelStream().forEach(unitConsumer);
    }

    private void processUnit(Supplier<List<String>> commandSupplier) {
        try {
            var lines = startProcess(commandSupplier.get());
            lines.forEach(LOGGER::info);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException(e);
        }
    }

    private void handleColorTempTimerEvent(ActionEvent event) {
        var timer = (DisplayUnitTimer) event.getSource();
        var colorTemp = (ColorTemp) timer.getValue();
        var unitName = timer.getUnitName();
        LOGGER.debug("Unit={}, value={}", unitName, colorTemp);

        processColorTemp(colorTemp, unitName);
    }

    private void processColorTemp(ColorTemp colorTemp, String unitName) {
        if (unitName == null) {
            processAllUnits(uName -> processUnit(() -> getColorTempCommandString(colorTemp, uName)));
        } else {
            processUnit(() -> getColorTempCommandString(colorTemp, unitName));
        }
    }

    private List<String> getColorTempCommandString(ColorTemp colorTemp, String unitName) {
        //@formatter:off
        return Arrays.asList("xrandr",
                             "--output", unitName,
                             "--gamma", new StringBuilder().append(colorTemp.getRed() / 255f)
                                                           .append(':')
                                                           .append(colorTemp.getGreen() / 255f)
                                                           .append(':')
                                                           .append(colorTemp.getBlue() / 255f)
                                                           .toString());
        //@formatter:on
    }

    @Override
    public void updateBrightness(DisplayUnit unit) {
        var timer = getTimer(unit.getName(), brightnessTimerEventConsumer);
        if (timer.isRunning()) {
            LOGGER.debug("unit={}", unit);
            return;
        }
        timer.restartWith(unit.getValue());
    }

    @Override
    public void applyColorTemp(ColorTemp ct) {
        unitNames.forEach(uname -> processColorTemp(ct, uname));
    }

}
