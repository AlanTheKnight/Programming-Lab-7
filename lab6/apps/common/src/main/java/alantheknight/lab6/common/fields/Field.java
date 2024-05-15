package alantheknight.lab6.common.fields;

import alantheknight.lab6.common.fields.handlers.*;
import alantheknight.lab6.common.models.Coordinates;
import alantheknight.lab6.common.models.Person;
import alantheknight.lab6.common.utils.Console;
import alantheknight.lab6.common.utils.Validatable;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Field class represents a field of a model.
 *
 * @param <T> field value type
 */
public class Field<T> implements Validatable, Serializable {
    /**
     * Field name (representing the field in the XML).
     */
    private final String name;
    /**
     * Field verbose name (for user output).
     */
    private final String verboseName;
    private final Integer targetSqlType;
    /**
     * Whether to ask for the value of the field.
     */
    private boolean askForValue = true;
    /**
     * Field value.
     */
    private T value;
    /**
     * Field checker used for field value validation.
     */
    private FieldChecker<T> checker = new DefaultFieldChecker<>();
    /**
     * Whether the field is required.
     */
    private boolean required = true;

    public Field(String name, String verboseName, T value, Integer targetSqlType) {
        this.name = name;
        this.verboseName = verboseName;
        this.value = value;
        this.targetSqlType = targetSqlType;
    }

    public Field(String name, String verboseName, T value, Integer targetSqlType, FieldChecker<T> checker) {
        this(name, verboseName, value, targetSqlType);
        this.checker = checker;
    }

    public Field(String name, String verboseName, T value, Integer targetSqlType, boolean required, FieldChecker<T> checker) {
        this(name, verboseName, value, targetSqlType, checker);
        this.required = required;
    }

    public Field(String name, String verboseName, T value, Integer targetSqlType, boolean required) {
        this(name, verboseName, value, targetSqlType);
        this.required = required;
    }

    public Field(String name, String verboseName, T value, Integer targetSqlType, boolean required, boolean askForValue) {
        this(name, verboseName, value, targetSqlType, required);
        this.askForValue = askForValue;
    }

    /**
     * Get the class of the field value.
     *
     * @return class of the field value
     */
    @SuppressWarnings("unchecked")
    public Class<T> getValueClass() {
        return (Class<T>) (this.value != null ? this.value.getClass() : null);
    }

    /**
     * Get the field value.
     *
     * @return field value
     */
    public T getValue() {
        return value;
    }

    /**
     * Set the field value.
     *
     * @param value field value
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Check if the field value is valid.
     *
     * @return a pair of a boolean indicating if the value is valid and a message
     */
    public ImmutablePair<Boolean, String> isValid() {
        if (required && value == null) {
            return new ImmutablePair<>(false, "Поле " + name + " не может быть пустым");
        }
        return checker.isValid(value);
    }

    public int getTargetSqlType() {
        return targetSqlType;
    }

    /**
     * Check if the field value is valid.
     *
     * @param value the value to check
     * @return a pair of a boolean indicating if the value is valid and a message
     */
    public ImmutablePair<Boolean, String> isValid(T value) {
        return checker.isValid(value);
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Field<?> field = (Field<?>) obj;
        return name.equals(field.name) && value.equals(field.value);
    }

    @Override
    public boolean validate() {
        return isValid().getLeft();
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        return name;
    }

    public boolean isAskForValue() {
        return askForValue;
    }

    public void setAskForValue(boolean askForValue) {
        this.askForValue = askForValue;
    }

    public String getVerboseName() {
        return verboseName;
    }

    /**
     * Input the field value from the console.
     *
     * @param console console
     * @throws InputHandler.InputException if the input is invalid
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void input(Console console) throws InputHandler.InputException {
        if (value instanceof Enum) {
            EnumInputHandler.input(console, (Field<? extends Enum>) this);
            return;
        }
        if (value instanceof Number) {
            NumberInputHandler.input(console, (Field<? extends Number>) this);
            return;
        }
        if (value instanceof String) {
            StringInputHandler.input(console, (Field<String>) this);
            return;
        }
        if (value instanceof LocalDate) {
            LocalDateInputHandler.input(console, (Field<LocalDate>) this);
            return;
        }
        if (value instanceof Model) {
            if (value instanceof Coordinates) {
                InputHandler.input(console, (Coordinates) value, Coordinates.class);
                return;
            }

            if (value instanceof Person) {
                InputHandler.input(console, (Model) value, Person.class);
                return;
            }
        }

        throw new UnsupportedOperationException("Input for " + value.getClass() + " is not supported");
    }

    public void addToStatement(PreparedStatement statement, MutableInt index, Object val) throws SQLException {
        if (!required && value == null) {
            statement.setNull(index.getAndIncrement(), targetSqlType);
            return;
        }
        statement.setObject(index.getAndIncrement(), val, targetSqlType);
    }

    public void addToStatement(PreparedStatement statement, MutableInt index) throws SQLException {
        addToStatement(statement, index, value);
    }

    /**
     * Default field checker that always returns true.
     */
    private static class DefaultFieldChecker<T> implements FieldChecker<T> {

        @Override
        public ImmutablePair<Boolean, String> isValid(T value) {
            return new ImmutablePair<>(true, null);
        }
    }
}
