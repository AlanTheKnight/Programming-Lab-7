package alantheknight.lab6.common.models;

import alantheknight.lab6.common.fields.Field;
import alantheknight.lab6.common.fields.Model;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.Types;
import java.util.Objects;

/**
 * Model for personal data of the worker.
 */
public class Person extends Model {
    /**
     * Height of the person (not null, greater than 0).
     */
    public final Field<Double> height = new Field<>("height", "Рост", 0.0, Types.DOUBLE, true, value -> {
        if (value <= 0) {
            return new ImmutablePair<>(false, "Поле person.height должно быть больше 0");
        }
        return new ImmutablePair<>(true, null);
    });

    /**
     * Weight of the person (not null, greater than 0).
     */
    public final Field<Long> weight = new Field<>("weight", "Вес", 0L, Types.BIGINT, true, value -> {
        if (value <= 0) {
            return new ImmutablePair<>(false, "Поле person.weight должно быть больше 0");
        }
        return new ImmutablePair<>(true, null);
    });

    /**
     * Hair color of the person (can be null).
     */
    public final Field<Color> hairColor = new Field<>("hairColor", "Цвет волос", null, Types.OTHER, false);

    /**
     * Nationality of the person (can be null).
     */
    public final Field<Country> nationality = new Field<>("nationality", "Национальность", null, Types.OTHER, false);

    /**
     * Creates a new person.
     *
     * @param height      height
     * @param weight      weight
     * @param hairColor   hair color
     * @param nationality nationality
     */
    public Person(double height, long weight, Color hairColor, Country nationality) {
        super();
        this.height.setValue(height);
        this.weight.setValue(weight);
        this.hairColor.setValue(hairColor);
        this.nationality.setValue(nationality);
    }

    @Override
    public boolean validate() {
        return !(height.getValue() <= 0) && weight.getValue() > 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, weight, hairColor, nationality);
    }
}
