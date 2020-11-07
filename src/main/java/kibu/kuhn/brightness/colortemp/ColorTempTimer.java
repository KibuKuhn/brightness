package kibu.kuhn.brightness.colortemp;

import java.awt.event.ActionListener;

import javax.swing.Timer;

class ColorTempTimer extends Timer
{
    private static final long serialVersionUID = 1L;
    private static final int DELAY = 60000;

    ColorTempTimer(ActionListener listener) {
        super(DELAY, listener);
        setInitialDelay(0);
    }
}
