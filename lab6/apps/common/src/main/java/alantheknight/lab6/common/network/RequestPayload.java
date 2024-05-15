package alantheknight.lab6.common.network;

import alantheknight.lab6.common.models.Worker;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * The RequestPayload class represents a payload of a request.
 */
public record RequestPayload(Worker element, Integer key, LocalDate endDate,
                             Credentials credentials) implements Serializable {
    public RequestPayload(LocalDate endDate, Credentials credentials) {
        this(null, null, endDate, credentials);
    }

    public RequestPayload(Worker element, Integer key, Credentials credentials) {
        this(element, key, null, credentials);
    }

    public RequestPayload(Integer key, Credentials credentials) {
        this(null, key, null, credentials);
    }

    public RequestPayload(Credentials credentials) {
        this(null, null, null, credentials);
    }

    public boolean isEmpty() {
        return element() == null && key() == null && endDate() == null && (credentials() == null || credentials().username() == null || credentials().password() == null);
    }

    public boolean hasCredentials() {
        return credentials() != null && credentials().username() != null && credentials().password() != null;
    }
}
