package kibu.kuhn.brightness.displayunit;

import java.util.List;

import kibu.kuhn.brightness.domain.DisplayUnit;

public interface IDisplayUnitManager {

	static IDisplayUnitManager get() {
		return DisplayUnitManager.get();
		
	}

	List<DisplayUnit> getDisplayUnits();



}
