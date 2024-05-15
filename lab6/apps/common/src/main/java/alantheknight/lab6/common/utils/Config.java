package alantheknight.lab6.common.utils;

public record Config(String dataPath, Integer port, String address, Integer bufferSize, ClientConfig clientConfig,
                     SSHConfig sshConfig, PostgresConfig postgresConfig, ServerConfig serverConfig) {
    public record ClientConfig(Integer maxRecursionLevel, Integer responseTimeout) {
    }

    public record SSHConfig(Integer port, String host, String password, String user) {

    }

    public record PostgresConfig(Integer port, String host, String password, String user, String database) {

    }

    public record ServerConfig(Boolean remote_db) {
    }
}