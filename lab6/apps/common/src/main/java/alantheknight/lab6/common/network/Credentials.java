package alantheknight.lab6.common.network;

import java.io.Serializable;

/**
 * The Credentials class represents authentication credentials.
 */
public record Credentials(String username, String password) implements Serializable {
}
