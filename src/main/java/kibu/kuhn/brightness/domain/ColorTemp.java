package kibu.kuhn.brightness.domain;

import java.util.Objects;

public class ColorTemp implements Comparable<ColorTemp>
{

    private int kelvin;
    private int red;
    private int green;
    private int blue;

    private ColorTemp(int kelvin, int red, int green, int blue) {
        this.kelvin = kelvin;
        this.red = red;
        this.green = green;
        this.blue = blue;
        ;
    }

    public static ColorTemp of(String line) {
        String[] split = line.split(":");
        String s0 = split[0].trim();
        int kelvin = Integer.parseInt(s0);
        String rgb = split[1].trim();
        rgb = rgb.substring(1, rgb.length() - 1);
        split = rgb.split(",");
        int red = Integer.parseInt(split[0].trim());
        int green = Integer.parseInt(split[1].trim());
        int blue = Integer.parseInt(split[2].trim());
        return new ColorTemp(kelvin, red, green, blue);
    }

    public int getKelvin() {
        return kelvin;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public int compareTo(ColorTemp ct) {
        return Integer.compare(this.kelvin, ct.kelvin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kelvin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ColorTemp other = (ColorTemp) obj;
        return kelvin == other.kelvin;
    }

    @Override
    public String toString() {
        return Integer.toString(kelvin);
    }
}
