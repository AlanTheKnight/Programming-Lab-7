package alantheknight.lab6.common.models;

import alantheknight.lab6.common.fields.CollectionElement;
import alantheknight.lab6.common.fields.Field;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Worker element of the collection.
 */
public class Worker extends CollectionElement {
    /**
     * Name of the worker (not null).
     */
    public final Field<String> name = new Field<>("name", "Имя", "", Types.VARCHAR, true);
    /**
     * Coordinates of the worker (not null).
     */
    public final Field<Coordinates> coordinates = new Field<>("coordinates", "Координаты", new Coordinates(0, 0.0F), Types.OTHER, true);
    /**
     * Salary of the worker (not null, greater than 0).
     */
    public final Field<Long> salary = new Field<>("salary", "Зарплата", 0L, Types.BIGINT, true, value -> {
        if (value <= 0) {
            return new ImmutablePair<>(false, "Поле worker.salary должно быть больше 0");
        }
        return new ImmutablePair<>(true, null);
    });
    /**
     * End date of the worker's contract (not null).
     */
    public final Field<LocalDate> endDate = new Field<>("endDate", "Дата окончания контракта", LocalDate.now(), Types.DATE, true);
    /**
     * Position of the worker (can be null).
     */
    public final Field<Position> position = new Field<>("position", "Должность", Position.DEVELOPER, Types.OTHER, false);
    /**
     * Status of the worker (not null).
     */
    public final Field<Status> status = new Field<>("status", "Статус", Status.RECOMMENDED_FOR_PROMOTION, Types.OTHER, true);
    /**
     * Personal data of the worker (not null).
     */
    public final Field<Person> person = new Field<>("person", "Персональные данные", new Person(0.0, 0, Color.RED, Country.NORTH_KOREA), Types.INTEGER, true);

    /**
     * Creation date of the worker record (generated automatically).
     */
    public final Field<LocalDate> creationDate = new Field<>("creationDate", "Дата создания", LocalDate.now(), Types.DATE, true, false);

    public final Field<Integer> owner = new Field<>("owner", "Владелец", null, Types.INTEGER, false, false);

    /**
     * Creates a new worker.
     *
     * @param id          id
     * @param name        name
     * @param coordinates coordinates
     * @param salary      salary
     * @param endDate     end date
     * @param position    position
     * @param status      status
     * @param person      person
     */
    public Worker(Integer id, String name, Coordinates coordinates, Long salary,
                  LocalDate endDate, Position position, Status status, Person person) {
        super(id);
        this.name.setValue(name);
        this.coordinates.setValue(coordinates);
        this.salary.setValue(salary);
        this.endDate.setValue(endDate);
        this.position.setValue(position);
        this.status.setValue(status);
        this.person.setValue(person);
        this.creationDate.setAskForValue(false); // This field is generated automatically
    }

    public Worker() {
        super();
        this.creationDate.setAskForValue(false); // This field is generated automatically
    }

    /**
     * Creates a new worker.
     *
     * @param id           id
     * @param name         name
     * @param coordinates  coordinates
     * @param salary       salary
     * @param creationDate creation date
     * @param endDate      end date
     * @param position     position
     * @param status       status
     * @param person       person
     */
    public Worker(Integer id, String name, Coordinates coordinates, Long salary, LocalDate creationDate,
                  LocalDate endDate, Position position, Status status, Person person) {
        this(id, name, coordinates, salary, endDate, position, status, person);
        this.creationDate.setValue(creationDate);
    }

    public static Map<Integer, Worker> fromResultSet(ResultSet resultSet) {
        Map<Integer, Worker> workers = new TreeMap<>();

        try {
            while (resultSet.next()) {
                String position_val = resultSet.getString("position");
                String nationality_val = resultSet.getString("nationality");
                String hair_color_val = resultSet.getString("hair_color");

                Worker worker = new Worker(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new Coordinates(resultSet.getInt("x"), resultSet.getFloat("y")),
                        resultSet.getLong("salary"),
                        resultSet.getDate("created_at").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        position_val == null ? null : Position.valueOf(position_val),
                        Status.valueOf(resultSet.getString("status")),
                        new Person(
                                resultSet.getDouble("height"),
                                resultSet.getInt("weight"),
                                hair_color_val == null ? null : Color.valueOf(hair_color_val),
                                nationality_val == null ? null : Country.valueOf(nationality_val)
                        ));
                worker.owner.setValue(resultSet.getInt("owner"));
                workers.put(worker.getId(), worker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workers;
    }
}
