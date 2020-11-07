package kibu.kuhn.brightness.event;

import kibu.kuhn.brightness.domain.ColorTemp;

public class ColorTempEvent extends Event<ColorTemp>
{

    public ColorTempEvent(ColorTemp delegate) {
        super(delegate);
    }

}
