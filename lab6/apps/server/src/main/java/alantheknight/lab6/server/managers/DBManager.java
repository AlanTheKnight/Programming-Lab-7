package alantheknight.lab6.server.managers;

import alantheknight.lab6.common.models.Person;
import alantheknight.lab6.common.models.Worker;
import alantheknight.lab6.common.network.Credentials;
import alantheknight.lab6.common.utils.Config;
import alantheknight.lab6.common.utils.ConfigReader;
import org.apache.commons.lang3.mutable.MutableInt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;

import static alantheknight.lab6.server.Main.logger;

public class DBManager {
    private final Config config = ConfigReader.getConfig();
    private final String databaseURL = "jdbc:postgresql://" + config.postgresConfig().host() + ":" + config.postgresConfig().port() + "/" + config.postgresConfig().database();

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseURL, config.postgresConfig().user(), config.postgresConfig().password());
    }

    public void connectSSH() throws DBConnectionException {
        if (config.serverConfig().remote_db()) {
            try {
                SSHConnectionManager.createSSHTunnel();
            } catch (SSHConnectionManager.SSHConnectionException e) {
                throw new DBConnectionException("Failed to create SSH tunnel: " + e.getMessage());
            }
        }
    }

    public Map<Integer, Worker> getAllWorkers() throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT *, (coordinates).x, (coordinates).y FROM workers JOIN persons ON workers.person_id = persons.id")
        ) {
            ResultSet result = statement.executeQuery();
            return Worker.fromResultSet(result);
        }
    }

    public Integer createPerson(Person person) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO persons (height, weight, hair_color, nationality) VALUES (?, ?, ?::hair_color_t, ?::nationality_t) RETURNING id")
        ) {
            MutableInt count = new MutableInt(1);

            person.height.addToStatement(statement, count);
            person.weight.addToStatement(statement, count);
            person.hairColor.addToStatement(statement, count);
            person.nationality.addToStatement(statement, count);

            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1);
        }
    }

    public void createWorker(Worker worker, int userId) throws SQLException {
        String query = "INSERT INTO workers (id, name, coordinates, salary, end_date, position, status, person_id, owner) VALUES (?, ?, ROW(?, ?), ?, ?, ?::position_t, ?::status_t, ?, ?) RETURNING id";
        if (worker.id.getValue() == null)
            query = "INSERT INTO workers (name, coordinates, salary, end_date, position, status, person_id, owner) VALUES (?, ROW(?, ?), ?, ?, ?::position_t, ?::status_t, ?, ?) RETURNING id";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            MutableInt count = new MutableInt(1);

            if (worker.id.getValue() != null) {
                worker.id.addToStatement(statement, count);
            }

            worker.name.addToStatement(statement, count);
            worker.coordinates.getValue().y.addToStatement(statement, count);
            worker.coordinates.getValue().x.addToStatement(statement, count);
            worker.salary.addToStatement(statement, count);
            worker.endDate.addToStatement(statement, count, Date.valueOf(worker.endDate.getValue()));
            worker.position.addToStatement(statement, count);
            worker.status.addToStatement(statement, count);
            worker.person.addToStatement(statement, count, createPerson(worker.person.getValue()));
            worker.owner.addToStatement(statement, count, userId);

            ResultSet result = statement.executeQuery();
            result.next();
            result.getInt(1);

            logger.info("Worker created with ID: {}", worker.id.getValue());
        }
    }

    public void updateWorker(Worker worker, int userId) throws SQLException, ObjectPermissionException {
        String query = "UPDATE workers SET name = ?, coordinates = ROW(?, ?), salary = ?, end_date = ?, position = ?::position_t, status = ?::status_t, person_id = ?, owner = ? WHERE id = ? AND owner = ?";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            MutableInt count = new MutableInt(1);
            worker.name.addToStatement(statement, count);
            worker.coordinates.getValue().y.addToStatement(statement, count);
            worker.coordinates.getValue().x.addToStatement(statement, count);
            worker.salary.addToStatement(statement, count);
            worker.endDate.addToStatement(statement, count, Date.valueOf(worker.endDate.getValue()));
            worker.position.addToStatement(statement, count);
            worker.status.addToStatement(statement, count);
            worker.person.addToStatement(statement, count, createPerson(worker.person.getValue()));
            worker.owner.addToStatement(statement, count, userId);
            worker.id.addToStatement(statement, count);
            statement.setInt(count.intValue(), userId);
            int updated = statement.executeUpdate();
            if (updated == 0)
                throw new ObjectPermissionException("No worker with provided ID exists or you don't have permission to update it.");
        }
    }

    public void createNewUser(Credentials credentials) throws SQLException {
        String passwordSalt = generatePasswordSalt();
        String passwordHash = hashPassword(credentials.password(), passwordSalt);

        String query = "INSERT INTO users (username, password_salt, password_hash) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, credentials.username());
            statement.setString(2, passwordSalt);
            statement.setString(3, passwordHash);

            int changed = statement.executeUpdate();
        }
    }

    private String generatePasswordSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    private String hashPassword(String password, String salt) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(hexToBytes(salt)); // Convert salt to byte array
            byte[] hashedBytes = md.digest(password.getBytes());
            hashedPassword = bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to hash password: {}", e.getMessage());
            throw new RuntimeException("Failed to hash password: " + e.getMessage());
        }
        return hashedPassword;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public Integer verifyCredentials(Credentials credentials) throws SQLException, InvalidCredentialsException {
        String query = "SELECT id, password_salt, password_hash FROM users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, credentials.username());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String passwordSalt = resultSet.getString("password_salt");
                String storedPasswordHash = resultSet.getString("password_hash");
                String hashedPassword = hashPassword(credentials.password(), passwordSalt);

                if (!hashedPassword.equals(storedPasswordHash)) {
                    throw new InvalidCredentialsException("Invalid username or password");
                }

                return resultSet.getInt("id");
            } else {
                throw new InvalidCredentialsException("Invalid username or password");
            }

        }
    }

    public void clear() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE workers CASCADE");
            statement.executeUpdate("TRUNCATE TABLE persons CASCADE");
        }
    }

    public int removeWorker(int id, int userId) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM workers WHERE id = ? AND owner = ?")) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            return statement.executeUpdate();
        }
    }

    public int removeAnyByEndDate(LocalDate endDate, int userId) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM workers WHERE id IN (SELECT id FROM workers WHERE end_date = ? AND owner = ? LIMIT 1)")) {
            statement.setDate(1, Date.valueOf(endDate));
            statement.setInt(2, userId);
            return statement.executeUpdate();
        }
    }

    public int removeGreaterId(int id, int userId) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM workers WHERE id IN (SELECT id FROM workers WHERE id > ? AND owner = ?)")) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            return statement.executeUpdate();
        }
    }

    public int removeGreaterEndDate(LocalDate endDate, int userId) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM workers WHERE id IN (SELECT id FROM workers WHERE end_date > ? AND owner = ?)")) {
            statement.setDate(1, Date.valueOf(endDate));
            statement.setInt(2, userId);
            return statement.executeUpdate();
        }
    }

    public static class DBConnectionException extends Exception {
        public DBConnectionException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends Exception {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class ObjectPermissionException extends Exception {
        public ObjectPermissionException(String message) {
            super(message);
        }
    }
}
