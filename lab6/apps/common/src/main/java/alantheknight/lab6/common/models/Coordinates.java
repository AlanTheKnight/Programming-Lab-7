package alantheknight.lab6.common.models;

import alantheknight.lab6.common.fields.Field;
import alantheknight.lab6.common.fields.Model;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.Types;


/**
 * Worker's coordinates model.
 */
public class Coordinates extends Model implements Comparable<Coordinates> {
    /**
     * X coordinate (not null).
     */
    public final Field<Integer> x = new Field<>("x", "Координата x", 0, Types.INTEGER, true);
    /**
     * Y coordinate (not null, greater than -154).
     */
    public final Field<Float> y = new Field<>("y", "Координата y", 0F, Types.FLOAT, true, value -> {
        if (value <= -154) {
            return new ImmutablePair<>(false, "Поле coordinates.y должно быть больше -154");
        }
        return new ImmutablePair<>(true, null);
    });

    /**
     * Creates a new Coordinates object.
     *
     * @param x x
     * @param y y
     */
    public Coordinates(int x, Float y) {
        super();
        this.x.setValue(x);
        this.y.setValue(y);
    }

    @Override
    public boolean validate() {
        return y.getValue() != null && y.getValue() > -154;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Coordinates that = (Coordinates) obj;
        return x.equals(that.x) && y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return x.getValue().hashCode() + y.getValue().hashCode();
    }

    @Override
    public int compareTo(Coordinates coordinates) {
        if (x.getValue().equals(coordinates.x.getValue()))
            return Float.compare(y.getValue(), coordinates.y.getValue());
        return Integer.compare(x.getValue(), coordinates.x.getValue());
    }
}
