package kibu.kuhn.brightness.displayunit;

import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DisplayUnitTimer extends Timer
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayUnitManager.class);
    private static ActionListener dummyListener = e -> {
    };

    private String unitName;
    private Object value;
    private ActionListener listener;

    DisplayUnitTimer(int delay, String unitName) {
        super(delay, dummyListener);
        setRepeats(false);
        this.unitName = unitName;
    }

    void restartWith(Object value) {
        this.value = value;
        restart();
    }

    Object getValue() {
        return value;
    }

    String getUnitName() {
        return unitName;
    }

    ActionListener getActionConsumer() {
        return listener;
    }

    public void setActionConsumer(ActionListener c) {
        LOGGER.debug("current consumer: {}, new consumer: {}", listener, c);
        if (listener == c) {
            return;
        }
        if (listener != null) {
            removeActionListener(dummyListener);
            removeActionListener(listener);
        }
        listener = c;
        addActionListener(listener);
    }

}
