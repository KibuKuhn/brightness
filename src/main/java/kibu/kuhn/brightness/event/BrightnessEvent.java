package kibu.kuhn.brightness.event;

import kibu.kuhn.brightness.domain.DisplayUnit;

public class BrightnessEvent extends Event<DisplayUnit>
{

    public BrightnessEvent(DisplayUnit unit) {
        super(unit);
    }

}
