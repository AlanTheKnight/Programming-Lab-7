package alantheknight.lab6.common.fields;

import java.sql.Types;

/**
 * CollectionElement is a subclass of FieldsElement with an ID field.
 */
public abstract class CollectionElement extends Model implements Comparable<CollectionElement> {
    /**
     * ID of the element (null if not set).
     */
    public final Field<Integer> id = new Field<>("id", "ID", null, Types.INTEGER, true);

    protected CollectionElement() {
        super();
        id.setAskForValue(false); // Automatically generated value
    }

    protected CollectionElement(Integer id) {
        this();
        this.id.setValue(id);
    }

    public Integer getId() {
        return id.getValue();
    }

    @Override
    public int compareTo(CollectionElement arg0) {
        return id.getValue() - arg0.getId();
    }
}
