package kibu.kuhn.brightness.displayunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.domain.DisplayUnit;

class DisplayUnitManager implements IDisplayUnitManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayUnitManager.class);

    private static DisplayUnitManager manager = new DisplayUnitManager();

    private DisplayUnitManager() {

    }

    @Override
    public List<DisplayUnit> getDisplayUnits() {
        // @formatter:off
		return queryUnitNames().map(DisplayUnit::new)
				               .collect(Collectors.toList());
		// @formatter:on
    }

    static IDisplayUnitManager get() {
        return manager;
    }

    private Stream<String> queryUnitNames() {
        try {
            List<String> commands = Arrays.asList("xrandr");
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();
            List<String> lines = new ArrayList<>();
            handleStream(process.getInputStream(), lines::add);
            handleStream(process.getErrorStream(), LOGGER::error);
            process.onExit().get(100, TimeUnit.MILLISECONDS);
            // @formatter:off
			return lines.stream()
					    .filter(line -> line.contains("connected") && !line.contains("disconnected"))
					    .map(line -> line.split("connected")[0].trim());
			// @formatter:on
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void handleStream(InputStream in, Consumer<String> c) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            c.accept(line);
        }
    }

}
