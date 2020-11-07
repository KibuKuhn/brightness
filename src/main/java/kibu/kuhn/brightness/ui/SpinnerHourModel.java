package kibu.kuhn.brightness.ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.swing.AbstractSpinnerModel;

import kibu.kuhn.brightness.utils.Injection;

@Injection
public class SpinnerHourModel extends AbstractSpinnerModel implements ISpinnerHourModel
{

    private static final long serialVersionUID = 1L;

    private DateTimeFormatter timeFormatter;

    private LocalTime value;

    @Inject
    private I18n i18n;

    public SpinnerHourModel() {
        value = LocalTime.now();
        timeFormatter = DateTimeFormatter.ofPattern(i18n.get("spinnerHourModel.pattern"));
    }

    @Override
    public TimeWrapper getValue() {
        return TimeWrapper.of(value, timeFormatter);
    }

    @Override
    public void setValue(Object value) {
        LocalTime lt = null;
        if (value instanceof String) {
            lt = LocalTime.parse((String) value, timeFormatter);
        } else if (value instanceof TimeWrapper) {
            lt = ((TimeWrapper) value).time;
        } else if (value instanceof LocalTime) {
            lt = (LocalTime) value;
        } else {
            throw new IllegalArgumentException("Not supported value type: " + value.getClass());
        }

        var time = this.value;
        this.value = lt;

        if (!time.equals(lt)) {
            this.fireStateChanged();
        }
    }

    @Override
    public TimeWrapper getNextValue() {
        return TimeWrapper.of(value.plusMinutes(1), timeFormatter);
    }

    @Override
    public TimeWrapper getPreviousValue() {
        return TimeWrapper.of(value.minusMinutes(1), timeFormatter);
    }

    public static class TimeWrapper implements Comparable<TimeWrapper>
    {
        private LocalTime time;
        private DateTimeFormatter formatter;

        @Override
        public String toString() {
            return time.format(formatter);
        }

        static TimeWrapper of(LocalTime value, DateTimeFormatter formatter) {
            var wrapper = new TimeWrapper();
            wrapper.time = value;
            wrapper.formatter = formatter;
            return wrapper;
        }

        @Override
        public int compareTo(TimeWrapper o) {
            return this.time.compareTo(o.time);
        }

        public LocalTime getTime() {
            return time;
        }
    }

}
