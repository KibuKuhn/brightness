package kibu.kuhn.brightness.displayunit;

import java.awt.event.ActionEvent;
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
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import kibu.kuhn.brightness.domain.DisplayUnit;
import kibu.kuhn.brightness.event.BrightnessEvent;
import kibu.kuhn.brightness.event.IEventbus;

class DisplayUnitManager implements IDisplayUnitManager
{

    private static final int PROCESS_TIMEOUT = 100;

    private static final int TIMER_DELAY_MILLIS = 50;

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayUnitManager.class);

    private static DisplayUnitManager manager = new DisplayUnitManager();

    private Map<String, BrightnessTimer> timers = new HashMap<>();

    private List<String> unitNames;

    private DisplayUnitManager() {
        init();
    }

    private void init() {
        IEventbus.get().register(this);
    }

    @Subscribe
    void brightnessChanged(BrightnessEvent e) {
        var unit = e.getDelegate();
        var timer = getTimer(unit.getName());
        if (timer.isRunning()) {
            LOGGER.debug("unit={}", unit);
            return;
        }
        timer.restartWith(unit.getValue());
    }

    private BrightnessTimer getTimer(String unitName) {
        var timer = timers.get(unitName);
        if (timer == null) {
            timer = new BrightnessTimer(TIMER_DELAY_MILLIS, this::handleTimerEvent, unitName);
            timers.put(unitName, timer);
        }
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

    static IDisplayUnitManager get() {
        return manager;
    }

    private List<String> queryUnitNames() {
        try {
            List<String> commands = Arrays.asList("xrandr");
            List<String> lines = startProcess(commands);
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
        ProcessBuilder builder = new ProcessBuilder(commands);
        Process process = builder.start();
        List<String> lines = new ArrayList<>();
        handleStream(process.getInputStream(), lines::add);
        handleStream(process.getErrorStream(), LOGGER::error);
        process.onExit().get(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS);
        return lines;
    }

    private void handleStream(InputStream in, Consumer<String> c) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }

            c.accept(line);
        }
    }

    private void handleTimerEvent(ActionEvent e) {
        BrightnessTimer timer = (BrightnessTimer) e.getSource();
        LOGGER.debug("Unit={}, value={}", timer.getUnitName(), timer.getValue());
        float value = (timer.getValue()) / 100f;
        if (timer.getUnitName() == null) {
            processAllUnits(value);
        } else {
            processUnit(value, timer.getUnitName());
        }
    }

    private void processAllUnits(float value) {
        unitNames.parallelStream().forEach(name -> processUnit(value, name));
    }

    void processUnit(float value, String unitName) {
        List<String> commands = Arrays.asList("xrandr", "--output", unitName, "--brightness", Float.toString(value));
        processBrightness(commands);
    }

    private void processBrightness(List<String> commands) {
        try {
            List<String> lines = startProcess(commands);
            lines.forEach(LOGGER::info);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException(e);
        }

    }

}
