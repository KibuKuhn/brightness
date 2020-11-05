package kibu.kuhn.brightness.displayunit;

import java.awt.event.ActionListener;

import javax.swing.Timer;

class BrightnessTimer extends Timer
{
    private static final long serialVersionUID = 1L;

    private String unitName;

    BrightnessTimer(int delay, ActionListener listener, String unitName) {
        super(delay, listener);
        setRepeats(false);
        this.unitName = unitName;
    }

    private int value;

    void restartWith(int value) {
        this.value = value;
        restart();
    }

    int getValue() {
        return value;
    }

    String getUnitName() {
        return unitName;
    }

}
