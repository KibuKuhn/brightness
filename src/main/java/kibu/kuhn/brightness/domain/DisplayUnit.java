package kibu.kuhn.brightness.domain;

public class DisplayUnit implements Cloneable
{

    private String name;
    private int value;

    public DisplayUnit(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public DisplayUnit(String name) {
        this(name, 100);
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void toAllUnits() {
        this.name = null;
    }

    @Override
    public DisplayUnit clone() {
        try {
            return (DisplayUnit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return "DisplayUnit [name=" + name + ", value=" + value + "]";
    }

}
