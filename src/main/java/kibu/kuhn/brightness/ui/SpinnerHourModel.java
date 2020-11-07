package kibu.kuhn.brightness.ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.AbstractSpinnerModel;

public class SpinnerHourModel extends AbstractSpinnerModel
{

    private static final long serialVersionUID = 1L;

    private static DateTimeFormatter pattern_AM_PM = DateTimeFormatter.ofPattern("hh:mm a");
    private static DateTimeFormatter pattern_24H = DateTimeFormatter.ofPattern("HH:mm");

    private LocalTime value;

    public SpinnerHourModel() {
        value = LocalTime.now();
    }

    @Override
    public Object getValue() {
        return TimeWrapper.of(value);
    }

    @Override
    public void setValue(Object value) {
        LocalTime lt = null;
        if (value instanceof String) {
            lt = LocalTime.parse((String) value, pattern_24H);
        } else if (value instanceof TimeWrapper) {
            lt = ((TimeWrapper) value).time;
        } else {
            throw new IllegalArgumentException("Not supported value type: " + value.getClass());
        }

        LocalTime time = this.value;
        this.value = lt;

        if (!time.equals(lt)) {
            this.fireStateChanged();
        }
    }

    @Override
    public Object getNextValue() {
        return TimeWrapper.of(value.plusMinutes(1));
    }

    @Override
    public Object getPreviousValue() {
        return TimeWrapper.of(value.minusMinutes(1));
    }

    public static class TimeWrapper implements Comparable<TimeWrapper>
    {
        private LocalTime time;

        @Override
        public String toString() {

            return time.format(pattern_24H);
        }

        public static TimeWrapper of(LocalTime value) {
            TimeWrapper wrapper = new TimeWrapper();
            wrapper.time = value;
            return wrapper;
        }

        @Override
        public int compareTo(TimeWrapper o) {
            return this.time.compareTo(o.time);
        }
    }

}
