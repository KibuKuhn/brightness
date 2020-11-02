package kibu.kuhn.brightness.displayunit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import kibu.kuhn.brightness.domain.DisplayUnit;

public class DisplayUnitManagerTest {
	
	private IDisplayUnitManager manager = IDisplayUnitManager.get();
	
	@Test
	public void testQuery() {
		List<DisplayUnit> displayUnits = manager.getDisplayUnits();
		assertThat(displayUnits).isNotEmpty();
		displayUnits.forEach(unit -> {
			assertThat(unit.getValue()).isEqualTo(100); 
		});
	}
}
