import java.util.Arrays;

public class Vec {
    double[] points;
    String className;

    public Vec(double[] points, String className) {
        this.points = points;
        this.className = className;
    }

    @Override
    public String toString() {
        return Arrays.toString(points) + ", class: " + className;
    }
}
