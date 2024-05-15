package alantheknight.lab6.client;

import alantheknight.lab6.common.fields.Field;
import alantheknight.lab6.common.fields.Model;
import alantheknight.lab6.common.fields.handlers.InputHandler;
import alantheknight.lab6.common.network.Credentials;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.Types;

import static alantheknight.lab6.client.Main.stdConsole;

public class AuthManager {
    private final User user = new User();

    public Credentials getCredentials() {
        if (user.username.getValue().isEmpty() || user.password.getValue().isEmpty()) return null;
        return new Credentials(user.username.getValue(), user.password.getValue());
    }

    public void clearCredentials() {
        user.password.setValue("");
        user.username.setValue("");
    }

    public void setCredentials(Credentials credentials) {
        user.username.setValue(credentials.username());
        user.password.setValue(credentials.password());
    }

    public void inputCredentials() throws InputHandler.InputException {
        user.username.input(stdConsole);
        user.password.input(stdConsole);
    }

    public boolean isAuthorized() {
        return user.username.getValue() != null && user.password.getValue() != null;
    }

    private static class User extends Model {
        public Field<String> username = new Field<>("username", "Юзернейм", "", Types.VARCHAR, true, f -> {
            if (f.length() >= 4 && f.length() <= 50) {
                return new ImmutablePair<>(true, null);
            } else {
                return new ImmutablePair<>(false, "Длина юзернейма должна быть от 4 до 50 символов");
            }
        });

        public Field<String> password = new Field<>("password", "Пароль", "", Types.VARCHAR, true, f -> {
            if (f.length() >= 6 && f.length() <= 30) {
                if (f.matches(".*[0-9].*") && f.matches(".*[A-Z].*") && f.matches(".*[a-z].*")) {
                    return new ImmutablePair<>(true, null);
                } else {
                    return new ImmutablePair<>(false, "Пароль должен содержать хотя бы одну цифру, одну заглавную и одну строчную букву");
                }
            } else {
                return new ImmutablePair<>(false, "Длина пароля должна быть от 6 до 30 символов");
            }
        });
    }


}
